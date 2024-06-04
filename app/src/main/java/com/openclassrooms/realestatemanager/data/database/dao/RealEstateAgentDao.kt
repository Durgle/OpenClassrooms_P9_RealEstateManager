package com.openclassrooms.realestatemanager.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.openclassrooms.realestatemanager.data.database.entities.RealEstateAgentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RealEstateAgentDao {

    @Insert
    suspend fun insertRealEstateAgent(realEstateAgent: RealEstateAgentEntity) :Long

    @Query("SELECT * FROM real_estate_agents")
    fun getRealEstateAgents(): Flow<List<RealEstateAgentEntity>>
}