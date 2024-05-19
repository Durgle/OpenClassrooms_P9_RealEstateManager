package com.openclassrooms.realestatemanager.data.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class EstateWithPhotosEntity(
    @Embedded val estate: EstateEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "estateId"
    )
    val photos: List<PhotoEntity>?,
    @Relation(
        parentColumn = "realEstateAgentId",
        entityColumn = "id"
    )
    val agent: RealEstateAgentEntity
)
