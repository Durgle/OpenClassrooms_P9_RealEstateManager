package com.openclassrooms.realestatemanager.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "photos",
    foreignKeys = [
        ForeignKey(entity = EstateEntity::class,
            parentColumns = ["id"],
            childColumns = ["estateId"],
            onDelete = ForeignKey.CASCADE)
    ]
)
data class PhotoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val photoPath: String,
    val description: String,
    val estateId: Long
)
