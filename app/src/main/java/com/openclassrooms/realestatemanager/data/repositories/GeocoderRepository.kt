package com.openclassrooms.realestatemanager.data.repositories

import android.location.Address
import android.location.Geocoder
import android.os.Build
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.IOException

/**
 * Repository used to retrieve geographic coordinates from an address
 *
 * @param geocoder The Geocoder
 */
class GeocoderRepository(
    private val geocoder: Geocoder,
    private val dispatcher: CoroutineDispatcher
) : GeocoderRepositoryInterface {

    /**
     * Retrieves the geographic coordinates for a given address.
     *
     * @param address The address
     * @return The [Address] object containing the geographic coordinates
     */
    override suspend fun getCoordinates(address: String): Address? {
        return withContext(dispatcher) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    suspendCancellableCoroutine { continuation ->
                        geocoder.getFromLocationName(address, 1) { addresses ->
                            if (addresses.isNotEmpty()) {
                                continuation.resumeWith(Result.success(addresses.first()))
                            } else {
                                continuation.resumeWith(Result.success(null))
                            }
                        }
                    }
                } else {
                    @Suppress("DEPRECATION")
                    val addresses = geocoder.getFromLocationName(address, 1)
                    if (!addresses.isNullOrEmpty()) {
                        addresses.first()
                    } else {
                        null
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }

}
