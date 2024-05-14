package com.openclassrooms.realestatemanager.data.repositories

import com.openclassrooms.realestatemanager.data.database.dao.RealEstateAgentDao
import com.openclassrooms.realestatemanager.data.database.entities.RealEstateAgentEntity
import com.openclassrooms.realestatemanager.data.models.RealEstateAgent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RealEstateAgentRepository(private val realEstateAgentDao: RealEstateAgentDao) :
    RealEstateAgentRepositoryInterface {

    override suspend fun insertRealEstateAgent(realEstateAgent: RealEstateAgent): Long {
        return realEstateAgentDao.insertRealEstateAgent(mapToRealEstateAgentEntity(realEstateAgent))
    }

    override fun getRealEstateAgent(realEstateAgentId: Long): Flow<RealEstateAgent> {
        return realEstateAgentDao.getRealEstateAgentById(realEstateAgentId).map { realEstateAgent ->
            mapToRealEstateAgent(realEstateAgent)
        }
    }

    override fun getRealEstateAgents(): Flow<List<RealEstateAgent>> {
        return realEstateAgentDao.getRealEstateAgents().map { realEstateAgentList ->
            realEstateAgentList.map { realEstateAgent ->
                mapToRealEstateAgent(realEstateAgent)
            }
        }
    }

    private fun mapToRealEstateAgentEntity(realEstateAgent: RealEstateAgent): RealEstateAgentEntity {
        return RealEstateAgentEntity(
            realEstateAgent.id,
            realEstateAgent.displayName
        )
    }

    private fun mapToRealEstateAgent(realEstateAgentEntity: RealEstateAgentEntity): RealEstateAgent {
        return RealEstateAgent(
            realEstateAgentEntity.id,
            realEstateAgentEntity.displayName
        )
    }

}
