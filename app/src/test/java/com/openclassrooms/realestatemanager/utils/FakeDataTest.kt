package com.openclassrooms.realestatemanager.utils

import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.data.database.entities.EstateEntity
import com.openclassrooms.realestatemanager.data.database.entities.EstateWithPhotosEntity
import com.openclassrooms.realestatemanager.data.database.entities.PhotoEntity
import com.openclassrooms.realestatemanager.data.database.entities.RealEstateAgentEntity
import com.openclassrooms.realestatemanager.data.enums.PointOfInterest
import com.openclassrooms.realestatemanager.data.enums.PropertyType
import com.openclassrooms.realestatemanager.data.models.Estate
import com.openclassrooms.realestatemanager.data.models.Photo
import com.openclassrooms.realestatemanager.data.models.RealEstateAgent
import com.openclassrooms.realestatemanager.ui.estate.list.EstateViewState

class FakeDataTest {

    companion object {

        private fun getFakeRealEstateAgentEntity(): RealEstateAgentEntity {
            return RealEstateAgentEntity(1, "Agent 1")
        }

        fun getFakeRealEstateAgentEntities(): List<RealEstateAgentEntity> {
            return listOf(
                RealEstateAgentEntity(1, "Agent 1"),
                RealEstateAgentEntity(2, "Agent 2"),
                RealEstateAgentEntity(3, "Agent 3")
            )
        }

        fun getFakeRealEstateAgents(): List<RealEstateAgent> {
            return listOf(
                RealEstateAgent(1, "Agent 1"),
                RealEstateAgent(2, "Agent 2"),
                RealEstateAgent(3, "Agent 3")
            )
        }

        fun getFakeEstate(
            estateId: Long,
            type: PropertyType = PropertyType.HOUSE,
            price: Long = 10000,
            latitude: Double? = 40.7128,
            longitude: Double? = -74.0060,
            available: Boolean = true,
            photos: List<Photo> = emptyList()
        ): Estate {
            return Estate(
                id = estateId,
                type = type,
                price = price,
                propertyArea = 120,
                numberOfBathrooms = 2,
                numberOfBedrooms = 3,
                description = "A beautiful house",
                photos = photos,
                address = "123 Main St",
                additionalAddressLine = null,
                city = "New York",
                zipCode = "10001",
                country = "United State",
                latitude = latitude,
                longitude = longitude,
                pointsOfInterest = listOf(
                    PointOfInterest.HEALTH_SERVICES,
                    PointOfInterest.RESTAURANTS_AND_CAFES
                ),
                available = available,
                entryDate = 1717281435264,
                saleDate = null,
                realEstateAgent = RealEstateAgent(1L, "Agent 1")
            )
        }

        fun getFakeEstateViewState(
            estateId: Long,
            type: PropertyType = PropertyType.HOUSE,
            price: String = "10000",
            location: LatLng? = null,
            selected: Boolean = false
        ): EstateViewState {
            return EstateViewState(
                id = estateId,
                photo = getFakePhoto("Photo 1",estateId),
                propertyType = type,
                city = "New York",
                price = price,
                available = true,
                location = location,
                selected = selected
            )
        }

        fun getFakeEstateEntity(
            estateId: Long,
            type: PropertyType = PropertyType.HOUSE,
            price: Long = 10000,
            latitude: Double? = 40.7128,
            longitude: Double? = -74.0060
        ): EstateEntity {
            return EstateEntity(
                id = estateId,
                type = type,
                price = price,
                propertyArea = 120,
                numberOfBathrooms = 2,
                numberOfBedrooms = 3,
                description = "A beautiful house",
                address = "123 Main St",
                additionalAddressLine = null,
                city = "New York",
                zipCode = "10001",
                country = "United State",
                latitude = latitude,
                longitude = longitude,
                pointsOfInterest = listOf(
                    PointOfInterest.HEALTH_SERVICES,
                    PointOfInterest.RESTAURANTS_AND_CAFES
                ),
                available = true,
                entryDate = 1717281435264,
                saleDate = null,
                realEstateAgentId = 1L
            )
        }

        fun getFakeEstateWithPhotosEntity(
            estateId: Long,
            type: PropertyType = PropertyType.HOUSE,
            price: Long = 10000,
            latitude: Double? = 40.7128,
            longitude: Double? = -74.0060,
            photos: List<PhotoEntity> = emptyList()
        ): EstateWithPhotosEntity {
            return EstateWithPhotosEntity(
                estate = getFakeEstateEntity(estateId, type, price, latitude, longitude),
                photos = photos,
                agent = getFakeRealEstateAgentEntity()
            )
        }

        fun getFakePhotoEntity(uri: String, estateId: Long): PhotoEntity {
            return PhotoEntity(uri, "Living Room", estateId)
        }

        fun getFakePhoto(uri: String, estateId: Long): Photo {
            return Photo(uri, "Living Room", estateId)
        }
    }

}