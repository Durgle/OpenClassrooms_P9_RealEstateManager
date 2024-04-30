package com.openclassrooms.realestatemanager.ui.estate.viewstate

import com.openclassrooms.realestatemanager.data.models.Photo

data class EstateDetailViewState(
    val id: Long,
    val propertyArea: String,
    val medias: List<Photo>,
    val description: String,
    val numberOfBathrooms: Int,
    val numberOfBedrooms: Int,
    val address: String
) {
    val numberOfRooms: Int
        get() = numberOfBathrooms + numberOfBedrooms
}
