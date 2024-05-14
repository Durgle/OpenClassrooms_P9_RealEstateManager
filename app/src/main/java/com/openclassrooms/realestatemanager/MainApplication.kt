package com.openclassrooms.realestatemanager

import android.app.Application
import com.openclassrooms.realestatemanager.data.database.RealEstateManagerDatabase
import com.openclassrooms.realestatemanager.data.repositories.EstateRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MainApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { RealEstateManagerDatabase.getInstance(this, applicationScope) }
    val estateRepository: EstateRepositoryInterface by lazy {
        EstateRepository(
            database.estateDao(),
            database.photoDao()
        )
    }
}
