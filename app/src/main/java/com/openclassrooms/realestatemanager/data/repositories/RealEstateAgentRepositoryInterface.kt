package com.openclassrooms.realestatemanager.data.repositories

import android.content.ContentValues
import com.openclassrooms.realestatemanager.data.models.RealEstateAgent
import kotlinx.coroutines.flow.Flow

interface RealEstateAgentRepositoryInterface {

    /**
     * Retrieves the list of real estate agents
     *
     * @return A [Flow] emitting the list of [RealEstateAgent]
     */
    fun getRealEstateAgents(): Flow<List<RealEstateAgent>>

    /**
     * Inserts an real estate agents in the database from content values.
     *
     * @param values The content values
     * @return Real Estate Agent id
     */
    fun insertFromContentValues(values: ContentValues): Long
}