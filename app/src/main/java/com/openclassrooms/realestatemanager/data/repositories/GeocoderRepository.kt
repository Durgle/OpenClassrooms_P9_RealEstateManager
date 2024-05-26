package com.openclassrooms.realestatemanager.data.repositories

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale

class GeocoderRepository(private val context: Context) : GeocoderRepositoryInterface {

    override suspend fun getCoordinates(address: String): Address? {
        return withContext(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
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
