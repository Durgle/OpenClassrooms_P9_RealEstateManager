package com.openclassrooms.realestatemanager.ui.estate.upsert

import android.content.res.Resources
import android.location.Address
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.enums.PointOfInterest
import com.openclassrooms.realestatemanager.data.enums.PropertyType
import com.openclassrooms.realestatemanager.data.models.Estate
import com.openclassrooms.realestatemanager.data.models.RealEstateAgent
import com.openclassrooms.realestatemanager.data.repositories.EstateRepositoryInterface
import com.openclassrooms.realestatemanager.data.repositories.GeocoderRepositoryInterface
import com.openclassrooms.realestatemanager.data.repositories.RealEstateAgentRepositoryInterface
import com.openclassrooms.realestatemanager.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpsertEstateViewModel(
    private val estateRepository: EstateRepositoryInterface,
    private val realEstateAgentRepository: RealEstateAgentRepositoryInterface,
    private val geocoderRepository: GeocoderRepositoryInterface,
    private val resources: Resources,
    private val estateId: Long?
) : ViewModel() {

    private val _estateForm = MutableStateFlow(UpsertEstateForm())
    private val _viewState = MutableStateFlow(UpsertEstateViewState())
    val snackBar = SingleLiveEvent<Event>()
    private val currentData: Flow<UpsertEstateForm?> = if (estateId != null) {
        estateRepository.getEstate(estateId).map { estate ->
            mapToUpsertEstateForm(estate)
        }
    } else {
        emptyFlow<UpsertEstateForm>()
    }
    private val realEstateAgents: LiveData<List<RealEstateAgent>> =
        realEstateAgentRepository.getRealEstateAgents().asLiveData()

    init {

        viewModelScope.launch {
            currentData.collect { data ->
                if (data != null) {
                    _estateForm.value = data
                }
            }
        }
    }

    private fun mapToUpsertEstateForm(estate: Estate): UpsertEstateForm {
        return UpsertEstateForm(
            estate.type,
            estate.price.toString(),
            estate.propertyArea.toString(),
            estate.numberOfBathrooms.toString(),
            estate.numberOfBedrooms.toString(),
            estate.description ?: "",
            estate.address,
            estate.additionalAddressLine ?: "",
            estate.zipCode,
            estate.city,
            estate.country,
            estate.realEstateAgent,
            estate.pointsOfInterest ?: emptyList(),
            estate.available,
            estate.entryDate,
            estate.saleDate
        )
    }

    fun save() {
        if (validateFormData()) {
            viewModelScope.launch {
                try {
                    val address = withContext(Dispatchers.IO) {
                        geocoderRepository.getCoordinates(_estateForm.value.getCompleteAddress())
                    }
                    estateRepository.insertEstate(createEstateObject(_estateForm.value, address))
                    snackBar.value = Event.SaveSuccess
                } catch (error: Throwable) {
                    Log.e("UpsertEstateViewModel", error.message ?: "")
                    snackBar.value = Event.Error(resources.getString(R.string.save_error))
                }
            }
        } else {
            snackBar.value = Event.Error(resources.getString(R.string.save_error))
        }
    }

    private fun createEstateObject(data: UpsertEstateForm, address: Address?): Estate {
        return Estate(
            estateId ?: 0,
            data.type!!,
            data.price.toLong(),
            data.surface.toLong(),
            data.numberOfBathroom.toInt(),
            data.numberOfBedroom.toInt(),
            data.description,
            null,
            data.address,
            data.additionalAddress,
            data.city,
            data.zipcode,
            data.country,
            address?.latitude,
            address?.longitude,
            data.pointsOfInterest,
            data.available,
            data.entryDate ?: System.currentTimeMillis(),
            if (data.available) null else data.saleDate ?: System.currentTimeMillis(),
            data.agent!!
        )
    }

    private fun validateFormData(): Boolean {
        val form = _estateForm.value

        val validAgent = UpsertEstateValidation.validateAgent(resources, form.agent)
        val validType = UpsertEstateValidation.validateType(resources, form.type)
        val validPrice = UpsertEstateValidation.validatePrice(resources, form.price)
        val validSurface = UpsertEstateValidation.validateSurface(resources, form.surface)
        val validBedroom =
            UpsertEstateValidation.validateNbOfBedroom(resources, form.numberOfBedroom)
        val validBathroom =
            UpsertEstateValidation.validateNbOfBathroom(resources, form.numberOfBathroom)
        val validAddress = UpsertEstateValidation.validateAddress(resources, form.address)
        val validZipcode = UpsertEstateValidation.validateZipcode(resources, form.zipcode)
        val validCity = UpsertEstateValidation.validateCity(resources, form.city)
        val validCountry = UpsertEstateValidation.validateCountry(resources, form.country)

        _viewState.value =
            UpsertEstateViewState(
                errorType = validType.message,
                errorPrice = validPrice.message,
                errorSurface = validSurface.message,
                errorNumberOfBathroom = validBathroom.message,
                errorNumberOfBedroom = validBedroom.message,
                errorAddress = validAddress.message,
                errorZipcode = validZipcode.message,
                errorCity = validCity.message,
                errorCountry = validCountry.message,
                errorAgent = validAgent.message
            )

        return validAgent is UpsertEstateValidation.ValidationResult.Valid &&
                validType is UpsertEstateValidation.ValidationResult.Valid &&
                validPrice is UpsertEstateValidation.ValidationResult.Valid &&
                validSurface is UpsertEstateValidation.ValidationResult.Valid &&
                validBedroom is UpsertEstateValidation.ValidationResult.Valid &&
                validBathroom is UpsertEstateValidation.ValidationResult.Valid &&
                validAddress is UpsertEstateValidation.ValidationResult.Valid &&
                validZipcode is UpsertEstateValidation.ValidationResult.Valid &&
                validCity is UpsertEstateValidation.ValidationResult.Valid &&
                validCountry is UpsertEstateValidation.ValidationResult.Valid
    }

    fun getErrorsViewState(): LiveData<UpsertEstateViewState> {
        return _viewState.asLiveData()
    }

    fun onAgentChanged(agent: RealEstateAgent?) {
        val result = UpsertEstateValidation.validateAgent(resources, agent)
        _viewState.value = _viewState.value.copy(errorAgent = result.message)
        _estateForm.value = _estateForm.value.copy(agent = agent)
    }

    fun onTypeChanged(type: PropertyType?) {
        val result = UpsertEstateValidation.validateType(resources, type)
        _viewState.value = _viewState.value.copy(errorType = result.message)
        _estateForm.value = _estateForm.value.copy(type = type)
    }

    fun onPriceChanged(price: String) {
        val result = UpsertEstateValidation.validatePrice(resources, price)
        _viewState.value = _viewState.value.copy(errorPrice = result.message)
        _estateForm.value = _estateForm.value.copy(price = price)
    }

    fun onSurfaceChanged(surface: String) {
        val result = UpsertEstateValidation.validateSurface(resources, surface)
        _viewState.value = _viewState.value.copy(errorSurface = result.message)
        _estateForm.value = _estateForm.value.copy(surface = surface)
    }

    fun onNumberOfBedroomChanged(numberOfBedroom: String) {
        val result = UpsertEstateValidation.validateNbOfBedroom(resources, numberOfBedroom)
        _viewState.value = _viewState.value.copy(errorNumberOfBedroom = result.message)
        _estateForm.value = _estateForm.value.copy(numberOfBedroom = numberOfBedroom)
    }

    fun onNumberOfBathroomChanged(numberOfBathroom: String) {
        val result = UpsertEstateValidation.validateNbOfBathroom(resources, numberOfBathroom)
        _viewState.value = _viewState.value.copy(errorNumberOfBathroom = result.message)
        _estateForm.value = _estateForm.value.copy(numberOfBathroom = numberOfBathroom)
    }

    fun onDescriptionChanged(description: String) {
        _estateForm.value = _estateForm.value.copy(description = description)
    }

    fun onAddressChanged(address: String) {
        val result = UpsertEstateValidation.validateAddress(resources, address)
        _viewState.value = _viewState.value.copy(errorAddress = result.message)
        _estateForm.value = _estateForm.value.copy(address = address)
    }

    fun onAdditionalAddressChanged(additionalAddress: String) {
        _estateForm.value = _estateForm.value.copy(additionalAddress = additionalAddress)
    }

    fun onZipcodeChanged(zipcode: String) {
        val result = UpsertEstateValidation.validateZipcode(resources, zipcode)
        _viewState.value = _viewState.value.copy(errorZipcode = result.message)
        _estateForm.value = _estateForm.value.copy(zipcode = zipcode)
    }

    fun onCityChanged(city: String) {
        val result = UpsertEstateValidation.validateCity(resources, city)
        _viewState.value = _viewState.value.copy(errorCity = result.message)
        _estateForm.value = _estateForm.value.copy(city = city)
    }

    fun onCountryChanged(country: String) {
        val result = UpsertEstateValidation.validateCountry(resources, country)
        _viewState.value = _viewState.value.copy(errorCountry = result.message)
        _estateForm.value = _estateForm.value.copy(country = country)
    }

    fun onAvailableChange(available: Boolean) {
        _estateForm.value = _estateForm.value.copy(available = available)
    }

    fun onPointOfInterestSelected(pointOfInterest: PointOfInterest, isSelected: Boolean) {

        val pointsOfInterest = _estateForm.value.pointsOfInterest.toMutableList()
        if (isSelected && !pointsOfInterest.contains(pointOfInterest)) {
            pointsOfInterest.add(pointOfInterest)
        } else if (!isSelected && pointsOfInterest.contains(pointOfInterest)) {
            pointsOfInterest.remove(pointOfInterest)
        }
        _estateForm.value = _estateForm.value.copy(pointsOfInterest = pointsOfInterest)
    }

    fun getRealEstateAgent(): LiveData<List<RealEstateAgent>> {
        return realEstateAgents
    }

    fun getCurrentData(): LiveData<UpsertEstateForm?> {
        return currentData.asLiveData()
    }

    sealed interface Event {
        data object SaveSuccess : Event
        data class Error(val message: String) : Event
    }

}