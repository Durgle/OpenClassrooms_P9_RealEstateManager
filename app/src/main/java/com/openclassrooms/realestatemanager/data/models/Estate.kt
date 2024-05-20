package com.openclassrooms.realestatemanager.data.models

import com.openclassrooms.realestatemanager.data.enums.PointOfInterest
import com.openclassrooms.realestatemanager.data.enums.PropertyType

data class Estate(
    val id: Long = 0,
    val type: PropertyType,
    val price: Long,
    val propertyArea: Long,
    val numberOfBathrooms: Int,
    val numberOfBedrooms: Int,
    val description: String?,
    val photos: List<Photo>?,
    val address: String,
    val additionalAddressLine: String?,
    val city: String,
    val zipCode: String,
    val country: String,
    val latitude: Double?,
    val longitude: Double?,
    val pointsOfInterest: List<PointOfInterest>?,
    val available: Boolean,
    val entryDate: Long,
    val saleDate: Long?,
    val realEstateAgent: RealEstateAgent
)