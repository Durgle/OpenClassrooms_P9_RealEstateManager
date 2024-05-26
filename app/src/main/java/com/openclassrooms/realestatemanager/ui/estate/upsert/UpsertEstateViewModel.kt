package com.openclassrooms.realestatemanager.ui.estate.upsert

import android.content.res.Resources
import android.location.Address
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.enums.PointOfInterest
import com.openclassrooms.realestatemanager.data.enums.PropertyType
import com.openclassrooms.realestatemanager.data.models.Estate
import com.openclassrooms.realestatemanager.data.models.Photo
import com.openclassrooms.realestatemanager.data.models.RealEstateAgent
import com.openclassrooms.realestatemanager.data.repositories.EstateRepositoryInterface
import com.openclassrooms.realestatemanager.data.repositories.GeocoderRepositoryInterface
import com.openclassrooms.realestatemanager.data.repositories.RealEstateAgentRepositoryInterface
import com.openclassrooms.realestatemanager.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpsertEstateViewModel(
    private val estateRepository: EstateRepositoryInterface,
    private val realEstateAgentRepository: RealEstateAgentRepositoryInterface,
    private val geocoderRepository: GeocoderRepositoryInterface,
    private val resources: Resources,
    private val estateId: Long?
) : ViewModel() {

    private val _viewState = MutableStateFlow(UpsertEstateViewState())
    val snackBar = SingleLiveEvent<Event>()
    private val realEstateAgents: LiveData<List<RealEstateAgent>> =
        realEstateAgentRepository.getRealEstateAgents().asLiveData()

    init {

        if (estateId != null) {
            viewModelScope.launch {
                estateRepository.getEstate(estateId).collect { estate ->
                    _viewState.value = UpsertEstateViewState(
                        currentData = CurrentDataViewState(
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
                            estate.saleDate,
                            estate.photos?.map { photo ->
                                PhotoViewState(
                                    photo.uri,
                                    photo.description
                                )
                            } ?: emptyList()
                        )
                    )
                }
            }
        } else {
            _viewState.value = UpsertEstateViewState()
        }
    }

    fun save() {
        if (validateFormData()) {
            viewModelScope.launch {
                try {
                    val address = withContext(Dispatchers.IO) {
                        geocoderRepository.getCoordinates(_viewState.value.getCompleteAddress())
                    }
                    estateRepository.upsertEstate(createEstateObject(_viewState.value, address))
                    snackBar.value = Event.SaveSuccess(estateId == null)
                } catch (error: Throwable) {
                    Log.e("UpsertEstateViewModel", error.message ?: "")
                    snackBar.value = Event.Error(resources.getString(R.string.save_general_error))
                }
            }
        } else {
            snackBar.value = Event.Error(resources.getString(R.string.save_error))
        }
    }

    private fun createEstateObject(data: UpsertEstateViewState, address: Address?): Estate {
        val currentData = data.currentData
        val saleDate =
            if (currentData.available) null else currentData.saleDate ?: System.currentTimeMillis()
        return Estate(
            estateId ?: 0,
            currentData.type!!,
            currentData.price.toLong(),
            currentData.surface.toLong(),
            currentData.numberOfBathroom.toInt(),
            currentData.numberOfBedroom.toInt(),
            currentData.description,
            currentData.photo.map { photoViewState ->
                Photo(photoViewState.uri, photoViewState.description, estateId ?: 0)
            },
            currentData.address,
            currentData.additionalAddress,
            currentData.city,
            currentData.zipcode,
            currentData.country,
            address?.latitude,
            address?.longitude,
            currentData.pointsOfInterest,
            currentData.available,
            currentData.entryDate ?: System.currentTimeMillis(),
            saleDate,
            currentData.agent!!
        )
    }

    private fun validateFormData(): Boolean {
        val form = _viewState.value.currentData

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

        var validPhotos = true
        form.photo.map { photoViewState ->
            val validPhoto =
                UpsertEstateValidation.validatePhotoDesc(resources, photoViewState.description)
            if (validPhoto !is UpsertEstateValidation.ValidationResult.Valid) {
                validPhotos = false
                photoViewState.copy(error = validPhoto.message)
            } else {
                photoViewState
            }
        }

        _viewState.value = _viewState.value.copy(
            errors = ErrorViewState(
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
                validCountry is UpsertEstateValidation.ValidationResult.Valid && validPhotos
    }

    fun getViewState(): LiveData<UpsertEstateViewState> {
        return _viewState.asLiveData()
    }

    fun onAgentChanged(agent: RealEstateAgent?) {
        val result = UpsertEstateValidation.validateAgent(resources, agent)
        _viewState.value = _viewState.value.copy(
            currentData = _viewState.value.currentData.copy(agent = agent),
            errors = _viewState.value.errors.copy(errorAgent = result.message)
        )
    }

    fun onTypeChanged(type: PropertyType?) {
        val result = UpsertEstateValidation.validateType(resources, type)
        _viewState.value = _viewState.value.copy(
            currentData = _viewState.value.currentData.copy(type = type),
            errors = _viewState.value.errors.copy(errorType = result.message)
        )
    }

    fun onPriceChanged(price: String) {
        val result = UpsertEstateValidation.validatePrice(resources, price)
        _viewState.value = _viewState.value.copy(
            currentData = _viewState.value.currentData.copy(price = price),
            errors = _viewState.value.errors.copy(errorPrice = result.message)
        )
    }

    fun onSurfaceChanged(surface: String) {
        val result = UpsertEstateValidation.validateSurface(resources, surface)
        _viewState.value = _viewState.value.copy(
            currentData = _viewState.value.currentData.copy(surface = surface),
            errors = _viewState.value.errors.copy(errorSurface = result.message)
        )
    }

    fun onNumberOfBedroomChanged(numberOfBedroom: String) {
        val result = UpsertEstateValidation.validateNbOfBedroom(resources, numberOfBedroom)
        _viewState.value = _viewState.value.copy(
            currentData = _viewState.value.currentData.copy(numberOfBedroom = numberOfBedroom),
            errors = _viewState.value.errors.copy(errorNumberOfBedroom = result.message)
        )
    }

    fun onNumberOfBathroomChanged(numberOfBathroom: String) {
        val result = UpsertEstateValidation.validateNbOfBathroom(resources, numberOfBathroom)
        _viewState.value = _viewState.value.copy(
            currentData = _viewState.value.currentData.copy(numberOfBathroom = numberOfBathroom),
            errors = _viewState.value.errors.copy(errorNumberOfBathroom = result.message)
        )
    }

    fun onDescriptionChanged(description: String) {
        _viewState.value = _viewState.value.copy(
            currentData = _viewState.value.currentData.copy(description = description)
        )
    }

    fun onAddressChanged(address: String) {
        val result = UpsertEstateValidation.validateAddress(resources, address)
        _viewState.value = _viewState.value.copy(
            currentData = _viewState.value.currentData.copy(address = address),
            errors = _viewState.value.errors.copy(errorAddress = result.message)
        )
    }

    fun onAdditionalAddressChanged(additionalAddress: String) {
        _viewState.value = _viewState.value.copy(
            currentData = _viewState.value.currentData.copy(additionalAddress = additionalAddress)
        )
    }

    fun onZipcodeChanged(zipcode: String) {
        val result = UpsertEstateValidation.validateZipcode(resources, zipcode)
        _viewState.value = _viewState.value.copy(
            currentData = _viewState.value.currentData.copy(zipcode = zipcode),
            errors = _viewState.value.errors.copy(errorZipcode = result.message)
        )
    }

    fun onCityChanged(city: String) {
        val result = UpsertEstateValidation.validateCity(resources, city)
        _viewState.value = _viewState.value.copy(
            currentData = _viewState.value.currentData.copy(city = city),
            errors = _viewState.value.errors.copy(errorCity = result.message)
        )
    }

    fun onCountryChanged(country: String) {
        val result = UpsertEstateValidation.validateCountry(resources, country)
        _viewState.value = _viewState.value.copy(
            currentData = _viewState.value.currentData.copy(country = country),
            errors = _viewState.value.errors.copy(errorCountry = result.message)
        )
    }

    fun onAvailableChange(available: Boolean) {
        _viewState.value = _viewState.value.copy(
            currentData = _viewState.value.currentData.copy(available = available)
        )
    }

    fun onPointOfInterestSelected(pointOfInterest: PointOfInterest, isSelected: Boolean) {

        val pointsOfInterest = _viewState.value.currentData.pointsOfInterest.toMutableList()
        if (isSelected && !pointsOfInterest.contains(pointOfInterest)) {
            pointsOfInterest.add(pointOfInterest)
        } else if (!isSelected && pointsOfInterest.contains(pointOfInterest)) {
            pointsOfInterest.remove(pointOfInterest)
        }
        _viewState.value = _viewState.value.copy(
            currentData = _viewState.value.currentData.copy(pointsOfInterest = pointsOfInterest)
        )
    }

    fun onPhotoDescriptionChanged(photo: PhotoViewState, description: String) {
        val photos = _viewState.value.currentData.photo.toMutableList()
        val result = UpsertEstateValidation.validatePhotoDesc(resources, description)
        photos.apply {
            val index = indexOfFirst { it.uri == photo.uri }
            if (index != -1) {
                val updatedPhoto =
                    this[index].copy(description = description, error = result.message)
                set(index, updatedPhoto)
            }
        }
        _viewState.value = _viewState.value.copy(
            currentData = _viewState.value.currentData.copy(photo = photos)
        )
    }

    fun getRealEstateAgent(): LiveData<List<RealEstateAgent>> {
        return realEstateAgents
    }

    fun removePhoto(photo: PhotoViewState) {
        val photos = _viewState.value.currentData.photo.toMutableList()
        photos.remove(photo)
        _viewState.value = _viewState.value.copy(
            currentData = _viewState.value.currentData.copy(photo = photos)
        )
    }

    fun addPhoto(uri: Uri) {
        val photos = _viewState.value.currentData.photo.toMutableList()
        photos.add(
            PhotoViewState(
                uri = uri.toString(),
                error = resources.getString(R.string.form_error_not_empty)
            )
        )
        _viewState.value = _viewState.value.copy(
            currentData = _viewState.value.currentData.copy(photo = photos)
        )
    }

    sealed interface Event {
        data class SaveSuccess(val newEstate: Boolean) : Event
        data class Error(val message: String) : Event
    }

}