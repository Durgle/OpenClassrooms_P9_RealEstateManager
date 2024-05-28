package com.openclassrooms.realestatemanager.ui.estate.map

import androidx.annotation.StringRes
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.data.models.Photo
import com.openclassrooms.realestatemanager.data.enums.PropertyType

data class EstateMapViewState(
    val id: Long,
    @StringRes val type: Int,
    val location: LatLng?,
    val selected: Boolean
)
