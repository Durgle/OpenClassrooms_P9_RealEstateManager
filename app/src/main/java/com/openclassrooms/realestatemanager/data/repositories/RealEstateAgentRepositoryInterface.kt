package com.openclassrooms.realestatemanager.data.repositories

import com.openclassrooms.realestatemanager.data.models.RealEstateAgent
import kotlinx.coroutines.flow.Flow

interface RealEstateAgentRepositoryInterface {

    suspend fun insertRealEstateAgent(realEstateAgent: RealEstateAgent): Long

    fun getRealEstateAgent(realEstateAgentId: Long): Flow<RealEstateAgent>

    fun getRealEstateAgents(): Flow<List<RealEstateAgent>>
}