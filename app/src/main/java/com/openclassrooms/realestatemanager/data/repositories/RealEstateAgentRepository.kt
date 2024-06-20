package com.openclassrooms.realestatemanager.data.repositories

import android.content.ContentValues
import com.openclassrooms.realestatemanager.data.database.dao.RealEstateAgentDao
import com.openclassrooms.realestatemanager.data.database.entities.RealEstateAgentEntity
import com.openclassrooms.realestatemanager.data.models.RealEstateAgent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository for accessing real estate agent data.
 *
 * @param realEstateAgentDao The DAO for real estate agents
 */
class RealEstateAgentRepository(
    private val realEstateAgentDao: RealEstateAgentDao
) : RealEstateAgentRepositoryInterface {

    /**
     * Retrieves the list of real estate agents
     *
     * @return A [Flow] emitting the list of [RealEstateAgent]
     */
    override fun getRealEstateAgents(): Flow<List<RealEstateAgent>> {
        return realEstateAgentDao.getRealEstateAgents().map { realEstateAgentList ->
            realEstateAgentList.map { realEstateAgent ->
                mapToRealEstateAgent(realEstateAgent)
            }
        }
    }

    override fun insertFromContentValues(values: ContentValues): Long {
        return realEstateAgentDao.insert(RealEstateAgentEntity.fromContentValues(values))
    }

    /**
     * Maps a [RealEstateAgentEntity] to a [RealEstateAgent]
     *
     * @param realEstateAgentEntity The entity to map
     * @return The [RealEstateAgent]
     */
    private fun mapToRealEstateAgent(realEstateAgentEntity: RealEstateAgentEntity): RealEstateAgent {
        return RealEstateAgent(
            realEstateAgentEntity.id,
            realEstateAgentEntity.displayName
        )
    }

}
