package com.openclassrooms.realestatemanager.data.repositories

import com.openclassrooms.realestatemanager.data.models.EstateFilter
import kotlinx.coroutines.flow.Flow

interface FilterRepositoryInterface {

    /**
     * Saves the estate filters to shared preferences
     *
     * @param filters The [EstateFilter] to save
     */
    fun saveEstateFilters(filters: EstateFilter)

    /**
     * Retrieves the estate filters from shared preferences as a flow
     *
     * @return A [Flow] emitting the current [EstateFilter]
     */
    fun getEstateFilters(): Flow<EstateFilter>

    /**
     * Clears the stored estate filters from shared preferences
     */
    fun clearEstateFilters()
}