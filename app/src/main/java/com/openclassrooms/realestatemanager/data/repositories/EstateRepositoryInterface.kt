package com.openclassrooms.realestatemanager.data.repositories

import android.database.Cursor
import com.openclassrooms.realestatemanager.data.models.Estate
import com.openclassrooms.realestatemanager.data.models.EstateFilter
import kotlinx.coroutines.flow.Flow

interface EstateRepositoryInterface {

    suspend fun upsertEstate(estate: Estate, photoRemoved: List<String>? = null)

    fun getEstate(estateId: Long): Flow<Estate>

    fun getEstates(): Flow<List<Estate>>

    fun getEstatesFiltered(filters: EstateFilter): Flow<List<Estate>>

    fun getEstateByIdWithCursor(estateId: Long): Cursor
}