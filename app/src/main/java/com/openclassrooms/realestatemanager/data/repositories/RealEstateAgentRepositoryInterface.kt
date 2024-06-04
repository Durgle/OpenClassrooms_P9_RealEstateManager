package com.openclassrooms.realestatemanager.data.repositories

import com.openclassrooms.realestatemanager.data.models.RealEstateAgent
import kotlinx.coroutines.flow.Flow

interface RealEstateAgentRepositoryInterface {

    /**
     * Retrieves the list of real estate agents
     *
     * @return A [Flow] emitting the list of [RealEstateAgent]
     */
    fun getRealEstateAgents(): Flow<List<RealEstateAgent>>
}