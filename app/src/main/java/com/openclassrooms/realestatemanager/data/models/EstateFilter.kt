package com.openclassrooms.realestatemanager.data.models

import com.openclassrooms.realestatemanager.data.enums.PropertyType

data class EstateFilter(
    val type: List<PropertyType>? = null,
    val minPrice: Long? = null,
    val maxPrice: Long? = null,
    val city: String? = null,
    val available: Boolean? = null
) {
    fun hasFilter(): Boolean {
        return !type.isNullOrEmpty() || minPrice != null || maxPrice != null || !city.isNullOrEmpty() || available != null
    }
}