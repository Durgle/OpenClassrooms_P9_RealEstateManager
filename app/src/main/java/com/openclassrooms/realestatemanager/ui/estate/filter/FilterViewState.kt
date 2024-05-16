package com.openclassrooms.realestatemanager.ui.estate.filter

import com.openclassrooms.realestatemanager.data.enums.PropertyType

data class FilterViewState(
    val type: List<PropertyType>,
    val minPrice: Float?,
    val maxPrice: Float?,
    val city: String,
    val available: Boolean?
)

