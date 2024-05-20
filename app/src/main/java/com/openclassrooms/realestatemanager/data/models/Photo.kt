package com.openclassrooms.realestatemanager.data.models

data class Photo(
    val uri: String,
    val description: String,
    val estateId: Long = 0
)
