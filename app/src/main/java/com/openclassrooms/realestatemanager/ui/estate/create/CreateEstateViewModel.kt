package com.openclassrooms.realestatemanager.ui.estate.create

import android.content.res.Resources
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
import com.openclassrooms.realestatemanager.data.repositories.RealEstateAgentRepositoryInterface
import com.openclassrooms.realestatemanager.utils.SingleLiveEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CreateEstateViewModel(
    private val estateRepository: EstateRepositoryInterface,
    private val realEstateAgentRepository: RealEstateAgentRepositoryInterface,
    private val resources: Resources
) : ViewModel() {

    private val _estateForm = MutableStateFlow(CreateEstateForm())
    private val _errorsForm = MutableStateFlow(CreateEstateErrorsViewState())

    val snackBar = SingleLiveEvent<Event>()

    private val realEstateAgents: LiveData<List<RealEstateAgent>> =
        realEstateAgentRepository.getRealEstateAgents().asLiveData()

    fun save() {
        if (validateFormData()) {
            viewModelScope.launch {
                try {
                    val data = _estateForm.value
                    estateRepository.insertEstate(
                        Estate(
                            0,
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
                            null, null,
                            data.pointsOfInterest,
                            true,
                            System.currentTimeMillis(),
                            null,
                            data.agent!!.id
                        )
                    )
                    snackBar.value = Event.SaveSuccess
                } catch (error: Throwable) {
                    Log.e("CreateEstateViewModel", error.message ?: "")
                    snackBar.value = Event.Error(resources.getString(R.string.save_error))
                }
            }
        } else {
            snackBar.value = Event.Error(resources.getString(R.string.save_error))
        }
    }

    private fun validateFormData(): Boolean {
        val form = _estateForm.value

        val validAgent = CreateEstateValidation.validateAgent(resources, form.agent)
        val validType = CreateEstateValidation.validateType(resources, form.type)
        val validPrice = CreateEstateValidation.validatePrice(resources, form.price)
        val validSurface = CreateEstateValidation.validateSurface(resources, form.surface)
        val validBedroom =
            CreateEstateValidation.validateNbOfBedroom(resources, form.numberOfBedroom)
        val validBathroom =
            CreateEstateValidation.validateNbOfBathroom(resources, form.numberOfBathroom)
        val validAddress = CreateEstateValidation.validateAddress(resources, form.address)
        val validZipcode = CreateEstateValidation.validateZipcode(resources, form.zipcode)
        val validCity = CreateEstateValidation.validateCity(resources, form.city)
        val validCountry = CreateEstateValidation.validateCountry(resources, form.country)

        _errorsForm.value =
            CreateEstateErrorsViewState(
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

        return validAgent is CreateEstateValidation.ValidationResult.Valid &&
                validType is CreateEstateValidation.ValidationResult.Valid &&
                validPrice is CreateEstateValidation.ValidationResult.Valid &&
                validSurface is CreateEstateValidation.ValidationResult.Valid &&
                validBedroom is CreateEstateValidation.ValidationResult.Valid &&
                validBathroom is CreateEstateValidation.ValidationResult.Valid &&
                validAddress is CreateEstateValidation.ValidationResult.Valid &&
                validZipcode is CreateEstateValidation.ValidationResult.Valid &&
                validCity is CreateEstateValidation.ValidationResult.Valid &&
                validCountry is CreateEstateValidation.ValidationResult.Valid

    }

    fun getErrorsViewState(): LiveData<CreateEstateErrorsViewState> {
        return _errorsForm.asLiveData()
    }

    fun onAgentChanged(agent: RealEstateAgent?) {
        val result = CreateEstateValidation.validateAgent(resources, agent)
        _errorsForm.value = _errorsForm.value.copy(errorAgent = result.message)
        _estateForm.value = _estateForm.value.copy(agent = agent)
    }

    fun onTypeChanged(type: PropertyType?) {
        val result = CreateEstateValidation.validateType(resources, type)
        _errorsForm.value = _errorsForm.value.copy(errorType = result.message)
        _estateForm.value = _estateForm.value.copy(type = type)
    }

    fun onPriceChanged(price: String) {
        val result = CreateEstateValidation.validatePrice(resources, price)
        _errorsForm.value = _errorsForm.value.copy(errorPrice = result.message)
        _estateForm.value = _estateForm.value.copy(price = price)
    }

    fun onSurfaceChanged(surface: String) {
        val result = CreateEstateValidation.validateSurface(resources, surface)
        _errorsForm.value = _errorsForm.value.copy(errorSurface = result.message)
        _estateForm.value = _estateForm.value.copy(surface = surface)
    }

    fun onNumberOfBedroomChanged(numberOfBedroom: String) {
        val result = CreateEstateValidation.validateNbOfBedroom(resources, numberOfBedroom)
        _errorsForm.value = _errorsForm.value.copy(errorNumberOfBedroom = result.message)
        _estateForm.value = _estateForm.value.copy(numberOfBedroom = numberOfBedroom)
    }

    fun onNumberOfBathroomChanged(numberOfBathroom: String) {
        val result = CreateEstateValidation.validateNbOfBathroom(resources, numberOfBathroom)
        _errorsForm.value = _errorsForm.value.copy(errorNumberOfBathroom = result.message)
        _estateForm.value = _estateForm.value.copy(numberOfBathroom = numberOfBathroom)
    }

    fun onDescriptionChanged(description: String) {
        _estateForm.value = _estateForm.value.copy(description = description)
    }

    fun onAddressChanged(address: String) {
        val result = CreateEstateValidation.validateAddress(resources, address)
        _errorsForm.value = _errorsForm.value.copy(errorAddress = result.message)
        _estateForm.value = _estateForm.value.copy(address = address)
    }

    fun onAdditionalAddressChanged(additionalAddress: String) {
        _estateForm.value = _estateForm.value.copy(additionalAddress = additionalAddress)
    }

    fun onZipcodeChanged(zipcode: String) {
        val result = CreateEstateValidation.validateZipcode(resources, zipcode)
        _errorsForm.value = _errorsForm.value.copy(errorZipcode = result.message)
        _estateForm.value = _estateForm.value.copy(zipcode = zipcode)
    }

    fun onCityChanged(city: String) {
        val result = CreateEstateValidation.validateCity(resources, city)
        _errorsForm.value = _errorsForm.value.copy(errorCity = result.message)
        _estateForm.value = _estateForm.value.copy(city = city)
    }

    fun onCountryChanged(country: String) {
        val result = CreateEstateValidation.validateCountry(resources, country)
        _errorsForm.value = _errorsForm.value.copy(errorCountry = result.message)
        _estateForm.value = _estateForm.value.copy(country = country)
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

    sealed interface Event {
        data object SaveSuccess : Event
        data class Error(val message: String) : Event
    }

}