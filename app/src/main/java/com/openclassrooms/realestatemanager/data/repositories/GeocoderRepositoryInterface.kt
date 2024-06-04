package com.openclassrooms.realestatemanager.data.repositories

import android.location.Address

interface GeocoderRepositoryInterface {

    /**
     * Retrieves the geographic coordinates for a given address.
     *
     * @param address The address
     * @return The [Address] object containing the geographic coordinates
     */
    suspend fun getCoordinates(address: String): Address?
}