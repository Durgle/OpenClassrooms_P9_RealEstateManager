package com.openclassrooms.realestatemanager.data.database.entities

import android.content.ContentValues
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.openclassrooms.realestatemanager.data.enums.PointOfInterest
import com.openclassrooms.realestatemanager.data.enums.PropertyType
import com.openclassrooms.realestatemanager.utils.Utils

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
    @ColumnInfo(index = true) val realEstateAgentId: Long,
    val videoUri: String?
) {
    companion object {
        fun fromContentValues(values: ContentValues): EstateEntity {
            return EstateEntity(
                id = values.getAsLong("id") ?: 0,
                type = PropertyType.valueOf(values.getAsString("type")),
                price = values.getAsLong("price"),
                propertyArea = values.getAsLong("propertyArea"),
                numberOfBathrooms = values.getAsInteger("numberOfBathrooms"),
                numberOfBedrooms = values.getAsInteger("numberOfBedrooms"),
                description = values.getAsString("description"),
                address = values.getAsString("address"),
                additionalAddressLine = values.getAsString("additionalAddressLine"),
                city = values.getAsString("city"),
                zipCode = values.getAsString("zipCode"),
                country = values.getAsString("country"),
                latitude = values.getAsDouble("latitude"),
                longitude = values.getAsDouble("longitude"),
                pointsOfInterest = values.getAsString("pointsOfInterest")?.let { Utils.parsePointOfInterests(it) },
                available = values.getAsBoolean("available"),
                entryDate = values.getAsLong("entryDate"),
                saleDate = values.getAsLong("saleDate"),
                realEstateAgentId = values.getAsLong("realEstateAgentId"),
                videoUri = values.getAsString("videoUri")
            )
        }
    }
}
