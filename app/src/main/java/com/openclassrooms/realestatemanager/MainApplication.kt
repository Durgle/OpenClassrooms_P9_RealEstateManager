package com.openclassrooms.realestatemanager

import android.app.Application
import android.location.Geocoder
import android.os.Looper
import com.google.android.gms.location.LocationServices
import com.openclassrooms.realestatemanager.data.database.RealEstateManagerDatabase
import com.openclassrooms.realestatemanager.data.repositories.EstateRepository
import com.openclassrooms.realestatemanager.data.repositories.EstateRepositoryInterface
import com.openclassrooms.realestatemanager.data.repositories.FilterRepository
import com.openclassrooms.realestatemanager.data.repositories.FilterRepositoryInterface
import com.openclassrooms.realestatemanager.data.repositories.GeocoderRepository
import com.openclassrooms.realestatemanager.data.repositories.GeocoderRepositoryInterface
import com.openclassrooms.realestatemanager.data.repositories.LocationRepository
import com.openclassrooms.realestatemanager.data.repositories.RealEstateAgentRepository
import com.openclassrooms.realestatemanager.data.repositories.RealEstateAgentRepositoryInterface
import com.openclassrooms.realestatemanager.worker.WorkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.util.Locale

/**
 * The main application class
 */
class MainApplication : Application() {

    /**
     * The scope for coroutines
     */
    private val applicationScope = CoroutineScope(SupervisorJob())

    /**
     * The database instance
     */
    private val database by lazy { RealEstateManagerDatabase.getInstance(this, applicationScope) }

    /**
     * The WorkManager instance
     */
    private val workManager: WorkManager by lazy { WorkManager(applicationContext) }

    /**
     * The estate repository
     */
    val estateRepository: EstateRepositoryInterface by lazy {
        EstateRepository(
            database.estateDao(),
            database.photoDao(),
            workManager
        )
    }

    /**
     * The real estate agent repository
     */
    val realEstateAgentRepository: RealEstateAgentRepositoryInterface by lazy {
        RealEstateAgentRepository(database.realEstateAgentDao())
    }

    /**
     * The filter repository
     */
    val filterRepository: FilterRepositoryInterface by lazy { FilterRepository(this) }

    /**
     * The geocoder repository
     */
    val geocoderRepository: GeocoderRepositoryInterface by lazy {
        GeocoderRepository(Geocoder(this, Locale.getDefault()), Dispatchers.IO)
    }

    /**
     * The location repository
     */
    val locationRepository: LocationRepository by lazy {
        LocationRepository(
            LocationServices.getFusedLocationProviderClient(this),
            Looper.getMainLooper()
        )
    }
}
