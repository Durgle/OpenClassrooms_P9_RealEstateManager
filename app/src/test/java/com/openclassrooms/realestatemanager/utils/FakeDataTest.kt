package com.openclassrooms.realestatemanager.utils

import com.openclassrooms.realestatemanager.data.database.entities.EstateEntity
import com.openclassrooms.realestatemanager.data.database.entities.EstateWithPhotosEntity
import com.openclassrooms.realestatemanager.data.database.entities.PhotoEntity
import com.openclassrooms.realestatemanager.data.database.entities.RealEstateAgentEntity
import com.openclassrooms.realestatemanager.data.enums.PointOfInterest
import com.openclassrooms.realestatemanager.data.enums.PropertyType
import com.openclassrooms.realestatemanager.data.models.Estate
import com.openclassrooms.realestatemanager.data.models.Photo
import com.openclassrooms.realestatemanager.data.models.RealEstateAgent

class FakeDataTest {

    companion object {

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

        fun getFakeEstate(estateId: Long = 0L): Estate {
            return Estate(
                id = estateId,
                type = PropertyType.HOUSE,
                price = 100000,
                propertyArea = 120,
                numberOfBathrooms = 2,
                numberOfBedrooms = 3,
                description = "A beautiful house",
                photos = listOf(Photo("uri1", "Living Room", estateId)),
                address = "123 Main St",
                additionalAddressLine = null,
                city = "New York",
                zipCode = "10001",
                country = "United State",
                latitude = 40.7128,
                longitude = -74.0060,
                pointsOfInterest = listOf(
                    PointOfInterest.HEALTH_SERVICES,
                    PointOfInterest.RESTAURANTS_AND_CAFES
                ),
                available = true,
                entryDate = 1717281435264,
                saleDate = null,
                realEstateAgent = RealEstateAgent(1L, "Agent 1")
            )
        }

        fun getFakeEstateEntity(estateId: Long = 0L): EstateEntity {
            return EstateEntity(
                id = estateId,
                type = PropertyType.HOUSE,
                price = 100000,
                propertyArea = 120,
                numberOfBathrooms = 2,
                numberOfBedrooms = 3,
                description = "A beautiful house",
                address = "123 Main St",
                additionalAddressLine = null,
                city = "New York",
                zipCode = "10001",
                country = "United State",
                latitude = 40.7128,
                longitude = -74.0060,
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

        fun getFakePhotoEntities(estateId: Long = 0L): List<PhotoEntity> {
            return listOf(PhotoEntity("uri1", "Living Room", estateId))
        }

        fun getFakeEstateWithPhotosEntity(estateId: Long = 0L): EstateWithPhotosEntity {
            return EstateWithPhotosEntity(
                estate = getFakeEstateEntity(estateId),
                photos = getFakePhotoEntities(estateId),
                agent = RealEstateAgentEntity(1L, "Agent 1")
            )
        }

        fun getFakeEstateWithPhotosEntities(): List<EstateWithPhotosEntity> {
            val date = System.currentTimeMillis()
            return listOf(
                getFakeEstateWithPhotosEntity(1L),
                getFakeEstateWithPhotosEntity(2L),
                getFakeEstateWithPhotosEntity(3L)
            )
        }

        fun getFakeEstates(): List<Estate> {
            return listOf(
                getFakeEstate(1L),
                getFakeEstate(2L),
                getFakeEstate(3L)
            )
        }
    }

}