package com.openclassrooms.realestatemanager.data.repositories

import com.openclassrooms.realestatemanager.data.models.EstateFilter
import kotlinx.coroutines.flow.Flow

interface FilterRepositoryInterface {

    fun saveEstateFilters(filters: EstateFilter)
    fun getEstateFilters(): Flow<EstateFilter>
    fun clearEstateFilters()
}