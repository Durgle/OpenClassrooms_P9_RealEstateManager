package com.openclassrooms.realestatemanager.data.database.entities

import android.content.ContentValues
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "real_estate_agents")
data class RealEstateAgentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val displayName: String
) {
    companion object {
        fun fromContentValues(values: ContentValues): RealEstateAgentEntity {
            return RealEstateAgentEntity(
                id = values.getAsLong("id") ?: 0,
                displayName = values.getAsString("displayName"),
            )
        }
    }
}
