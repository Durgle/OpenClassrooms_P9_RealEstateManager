package com.openclassrooms.realestatemanager.data.repositories

import android.location.Address

interface GeocoderRepositoryInterface {

    suspend fun getCoordinates(address: String): Address?
}