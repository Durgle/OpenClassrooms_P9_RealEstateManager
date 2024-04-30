package com.openclassrooms.realestatemanager.ui.estate.viewstate

import com.openclassrooms.realestatemanager.data.models.Photo
import com.openclassrooms.realestatemanager.data.models.PropertyType

data class EstateViewState(
    val id: Long,
    val photo: Photo,
    val propertyType: PropertyType,
    val city: String,
    val price: String,
    val selected: Boolean
)
