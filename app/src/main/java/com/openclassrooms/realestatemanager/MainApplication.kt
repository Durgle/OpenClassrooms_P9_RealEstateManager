package com.openclassrooms.realestatemanager

import android.app.Application
import com.openclassrooms.realestatemanager.data.database.RealEstateManagerDatabase
import com.openclassrooms.realestatemanager.data.repositories.EstateRepository
import com.openclassrooms.realestatemanager.data.repositories.EstateRepositoryInterface
import com.openclassrooms.realestatemanager.data.repositories.FilterRepository
import com.openclassrooms.realestatemanager.data.repositories.FilterRepositoryInterface
import com.openclassrooms.realestatemanager.data.repositories.GeocoderRepository
import com.openclassrooms.realestatemanager.data.repositories.GeocoderRepositoryInterface
import com.openclassrooms.realestatemanager.data.repositories.RealEstateAgentRepository
import com.openclassrooms.realestatemanager.data.repositories.RealEstateAgentRepositoryInterface
import com.openclassrooms.realestatemanager.worker.WorkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MainApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { RealEstateManagerDatabase.getInstance(this, applicationScope) }
    private val workManager: WorkManager by lazy {
        WorkManager(applicationContext)
    }

    val estateRepository: EstateRepositoryInterface by lazy {
        EstateRepository(
            database.estateDao(),
            database.photoDao(),
            workManager
        )
    }
    val realEstateAgentRepository: RealEstateAgentRepositoryInterface by lazy {
        RealEstateAgentRepository(database.realEstateAgentDao())
    }
    val filterRepository: FilterRepositoryInterface by lazy {
        FilterRepository(this)
    }
    val geocoderRepository: GeocoderRepositoryInterface by lazy {
        GeocoderRepository(this)
    }
}
