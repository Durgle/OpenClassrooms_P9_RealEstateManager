package com.openclassrooms.realestatemanager.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "photos",
    foreignKeys = [
        ForeignKey(
            entity = EstateEntity::class,
            parentColumns = ["id"],
            childColumns = ["estateId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    primaryKeys = ["uri", "estateId"]
)
data class PhotoEntity(
    val uri: String,
    val description: String,
    val estateId: Long
)
