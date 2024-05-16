package com.openclassrooms.realestatemanager.ui.estate.filter

import com.openclassrooms.realestatemanager.data.enums.PropertyType

data class FilterEstateForm(
    val type: List<PropertyType> = emptyList(),
    val minPrice: String = "",
    val maxPrice: String = "",
    val city: String = "",
    val available: Boolean? = null
)