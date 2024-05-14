package com.openclassrooms.realestatemanager.ui.estate.create


data class CreateEstateErrorsViewState(
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
    val errorPointOfInterest: String = ""
)

