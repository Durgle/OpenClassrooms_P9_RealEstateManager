package com.openclassrooms.realestatemanager.data.models

import java.sql.Timestamp

data class Estate(
    val id: Long,
    val type: PropertyType,
    val price: Long,
    val propertyArea: Long,
    val numberOfBathrooms: Int,
    val numberOfBedrooms: Int,
    val description: String?,
    val photos: List<Photo>,
    val address: String,
    val additionalAddressLine: String?,
    val city: String,
    val zipCode: String,
    val country: String,
    val pointsOfInterest: List<String>?,
    val available: Boolean,
    val entryDate: Timestamp,
    val saleDate: Timestamp?,
    val realEstateAgent: RealEstateAgent
)
