package com.openclassrooms.realestatemanager.ui.estate.list

import com.openclassrooms.realestatemanager.data.models.Photo
import com.openclassrooms.realestatemanager.data.enums.PropertyType

data class EstateViewState(
    val id: Long,
    val photo: Photo?,
    val propertyType: PropertyType,
    val city: String,
    val price: String,
    val available: Boolean,
    val selected: Boolean
)
