package com.openclassrooms.realestatemanager.ui.estate.create

import com.openclassrooms.realestatemanager.data.enums.PointOfInterest
import com.openclassrooms.realestatemanager.data.enums.PropertyType
import com.openclassrooms.realestatemanager.data.models.RealEstateAgent

data class CreateEstateForm(
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
)