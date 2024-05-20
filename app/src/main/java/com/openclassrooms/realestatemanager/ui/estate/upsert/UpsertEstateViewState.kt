package com.openclassrooms.realestatemanager.ui.estate.upsert

import com.openclassrooms.realestatemanager.data.enums.PointOfInterest
import com.openclassrooms.realestatemanager.data.enums.PropertyType
import com.openclassrooms.realestatemanager.data.models.RealEstateAgent
import com.openclassrooms.realestatemanager.utils.Utils

data class UpsertEstateViewState(
    val currentData: CurrentDataViewState = CurrentDataViewState(),
    val errors: ErrorViewState = ErrorViewState(),
) {
    fun getCompleteAddress(): String {
        return Utils.formatEstateAddress(
            currentData.address,
            currentData.additionalAddress,
            currentData.city,
            currentData.zipcode,
            currentData.country
        )
    }
}

data class CurrentDataViewState(
    val type: PropertyType? = null,
    val price: String = "",
    val surface: String = "",
    val numberOfBathroom: String = "",
    val numberOfBedroom: String = "",
    val description: String = "",
    val address: String = "",
    val additionalAddress: String = "",
    val zipcode: String = "",
    val city: String = "",
    val country: String = "",
    val agent: RealEstateAgent? = null,
    val pointsOfInterest: List<PointOfInterest> = emptyList(),
    val available: Boolean = true,
    val entryDate: Long? = null,
    val saleDate: Long? = null,
    val photo: List<PhotoViewState> = emptyList(),
)

data class PhotoViewState(
    val uri: String,
    val description: String = "",
    val error: String = ""
)

data class ErrorViewState(
    val errorType: String = "",
    val errorPrice: String = "",
    val errorSurface: String = "",
    val errorNumberOfBathroom: String = "",
    val errorNumberOfBedroom: String = "",
    val errorDescription: String = "",
    val errorAddress: String = "",
    val errorAdditionalAddress: String = "",
    val errorZipcode: String = "",
    val errorCity: String = "",
    val errorCountry: String = "",
    val errorAgent: String = "",
)

