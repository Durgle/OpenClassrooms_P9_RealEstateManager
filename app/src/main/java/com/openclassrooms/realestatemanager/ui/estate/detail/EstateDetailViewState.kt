package com.openclassrooms.realestatemanager.ui.estate.detail

import androidx.annotation.StringRes
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.data.enums.PointOfInterest
import com.openclassrooms.realestatemanager.data.models.Photo

data class EstateDetailViewState(
    val id: Long,
    val propertyArea: String,
    val medias: List<Photo>,
    val description: String,
    val numberOfBathrooms: Int,
    val numberOfBedrooms: Int,
    val address: String,
    val location: LatLng?,
    @StringRes val availability: Int,
    val pointOfInterest: List<PointOfInterest>
) {
    val numberOfRooms: Int
        get() = numberOfBathrooms + numberOfBedrooms
}
