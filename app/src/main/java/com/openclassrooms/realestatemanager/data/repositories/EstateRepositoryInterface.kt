package com.openclassrooms.realestatemanager.data.repositories

import com.openclassrooms.realestatemanager.data.models.Estate
import com.openclassrooms.realestatemanager.data.models.EstateFilter
import kotlinx.coroutines.flow.Flow

interface EstateRepositoryInterface {

    suspend fun insertEstate(estate: Estate): Long

    suspend fun updateEstate(estate: Estate)

    fun getEstate(estateId: Long): Flow<Estate>

    fun getEstates(): Flow<List<Estate>>

    fun getEstatesFiltered(filters: EstateFilter): Flow<List<Estate>>
}