package com.openclassrooms.realestatemanager.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "real_estate_agents")
data class RealEstateAgentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val displayName: String
)
