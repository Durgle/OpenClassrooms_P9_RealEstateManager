package com.openclassrooms.realestatemanager.utils

import androidx.annotation.StringRes
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.FakeData
import com.openclassrooms.realestatemanager.data.database.entities.EstateEntity
import com.openclassrooms.realestatemanager.data.database.entities.EstateWithPhotosEntity
import com.openclassrooms.realestatemanager.data.database.entities.PhotoEntity
import com.openclassrooms.realestatemanager.data.database.entities.RealEstateAgentEntity
import com.openclassrooms.realestatemanager.data.enums.PointOfInterest
import com.openclassrooms.realestatemanager.data.enums.PropertyType
import com.openclassrooms.realestatemanager.data.models.Estate
import com.openclassrooms.realestatemanager.data.models.Photo
import com.openclassrooms.realestatemanager.data.models.RealEstateAgent
import com.openclassrooms.realestatemanager.ui.estate.detail.EstateDetailViewState
import com.openclassrooms.realestatemanager.ui.estate.list.EstateViewState
import com.openclassrooms.realestatemanager.ui.estate.upsert.CurrentDataViewState
import com.openclassrooms.realestatemanager.ui.estate.upsert.PhotoViewState
import com.openclassrooms.realestatemanager.ui.estate.upsert.UpsertEstateViewState

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

        fun getFakeRealEstateAgent(id: Long): RealEstateAgent {
            return RealEstateAgent(id, "Agent $id")
        }

        fun getFakeRealEstateAgents(): List<RealEstateAgent> {
            return listOf(
                getFakeRealEstateAgent(1),
                getFakeRealEstateAgent(2),
                getFakeRealEstateAgent(3)
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
                realEstateAgent = RealEstateAgent(1L, "Agent 1"),
                videoUri = FakeData.getFakeVideo()
            )
        }

        fun getFakeEstateDetailViewState(
            estateId: Long,
            area: String = "120 sq m",
            location: LatLng? = null,
            @StringRes available: Int = R.string.estate_available,
            photos: List<Photo> = emptyList(),
            pointOfInterest: List<PointOfInterest> = listOf(
                PointOfInterest.HEALTH_SERVICES,
                PointOfInterest.RESTAURANTS_AND_CAFES
            )
        ): EstateDetailViewState {
            return EstateDetailViewState(
                id = estateId,
                propertyArea = area,
                medias = photos,
                description = "A beautiful house",
                numberOfBathrooms = 2,
                numberOfBedrooms = 3,
                address = Utils.formatEstateAddress(
                    "123 Main St",
                    null,
                    "New York",
                    "10001",
                    "United State"
                ),
                location = location,
                availability = available,
                pointOfInterest = pointOfInterest,
                videoUri = FakeData.getFakeVideo()
            )
        }

        fun getFakeEstateViewState(
            estateId: Long,
            type: PropertyType = PropertyType.HOUSE,
            price: String = "10000",
            location: LatLng? = null,
            selected: Boolean = false,
            available: Boolean = true,
            photos: Photo? = null
        ): EstateViewState {
            return EstateViewState(
                id = estateId,
                photo = photos,
                propertyType = type,
                city = "New York",
                price = price,
                available = available,
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
                realEstateAgentId = 1L,
                videoUri = FakeData.getFakeVideo()
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

        fun getFakeUpsertViewState(
            type: PropertyType = PropertyType.HOUSE,
            price: String = "10000",
            available: Boolean = true,
            photos: List<PhotoViewState> = emptyList(),
            photoRemoved: List<String> = emptyList(),
            agent: RealEstateAgent = RealEstateAgent(1L, "Agent 1")
        ): UpsertEstateViewState {
            return UpsertEstateViewState(
                currentData = CurrentDataViewState(
                    type = type,
                    price = price,
                    surface = "120",
                    numberOfBathroom = "2",
                    numberOfBedroom = "3",
                    description = "A beautiful house",
                    photo = photos,
                    photoRemoved = photoRemoved,
                    address = "123 Main St",
                    additionalAddress = "",
                    city = "New York",
                    zipcode = "10001",
                    country = "United State",
                    pointsOfInterest = listOf(
                        PointOfInterest.HEALTH_SERVICES,
                        PointOfInterest.RESTAURANTS_AND_CAFES
                    ),
                    available = available,
                    entryDate = 1717281435264,
                    saleDate = null,
                    agent = agent
                )
            )
        }
    }

}