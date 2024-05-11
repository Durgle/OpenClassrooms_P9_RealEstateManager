package com.openclassrooms.realestatemanager.ui.estate.detail

import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.data.models.Photo

data class EstateDetailViewState(
    val id: Long,
    val propertyArea: String,
    val medias: List<Photo>,
    val description: String,
    val numberOfBathrooms: Int,
    val numberOfBedrooms: Int,
    val address: String,
    val location: LatLng?
) {
    val numberOfRooms: Int
        get() = numberOfBathrooms + numberOfBedrooms
}
