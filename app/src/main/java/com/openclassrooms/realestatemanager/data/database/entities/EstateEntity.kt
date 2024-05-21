package com.openclassrooms.realestatemanager.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.openclassrooms.realestatemanager.data.enums.PointOfInterest
import com.openclassrooms.realestatemanager.data.enums.PropertyType

@Entity(
    tableName = "estates",
    foreignKeys = [
        ForeignKey(entity = RealEstateAgentEntity::class,
            parentColumns = ["id"],
            childColumns = ["realEstateAgentId"],
            onDelete = ForeignKey.CASCADE)
    ]
)
data class EstateEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: PropertyType,
    val price: Long,
    val propertyArea: Long,
    val numberOfBathrooms: Int,
    val numberOfBedrooms: Int,
    val description: String?,
    val address: String,
    val additionalAddressLine: String?,
    val city: String,
    val zipCode: String,
    val country: String,
    val latitude: Double?,
    val longitude: Double?,
    val pointsOfInterest: List<PointOfInterest>?,
    val available: Boolean,
    val entryDate: Long,
    val saleDate: Long?,
    @ColumnInfo(index = true) val realEstateAgentId: Long
)
