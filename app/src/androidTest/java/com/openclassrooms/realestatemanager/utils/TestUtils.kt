package com.openclassrooms.realestatemanager.utils


import android.content.ContentValues
import com.openclassrooms.realestatemanager.data.FakeData
import com.openclassrooms.realestatemanager.data.database.entities.EstateEntity
import com.openclassrooms.realestatemanager.data.database.entities.EstateWithPhotosEntity
import com.openclassrooms.realestatemanager.data.database.entities.PhotoEntity
import com.openclassrooms.realestatemanager.data.database.entities.RealEstateAgentEntity
import com.openclassrooms.realestatemanager.data.enums.PointOfInterest
import com.openclassrooms.realestatemanager.data.enums.PropertyType

class TestUtils {

    companion object {

        fun getFakeRealEstateAgentEntity(id: Long): RealEstateAgentEntity {
            return RealEstateAgentEntity(id, "Agent $id")
        }

        fun getFakeEstateEntity(
            id: Long,
            type: PropertyType,
            price: Long,
            agentId: Long,
            available: Boolean = true,
            city: String = "New York"
        ): EstateEntity {
            return EstateEntity(
                id = id,
                type = type,
                price = price,
                propertyArea = 120,
                numberOfBathrooms = 2,
                numberOfBedrooms = 3,
                description = "A beautiful house",
                address = "123 Main St",
                additionalAddressLine = null,
                city = city,
                zipCode = "10001",
                country = "United State",
                latitude = null,
                longitude = null,
                pointsOfInterest = listOf(
                    PointOfInterest.HEALTH_SERVICES,
                    PointOfInterest.RESTAURANTS_AND_CAFES
                ),
                available = available,
                entryDate = 1717281435264,
                saleDate = null,
                realEstateAgentId = agentId,
                videoUri = FakeData.getFakeVideo()
            )
        }

        fun getFakeEstateWithPhotosEntity(
            estateId: Long,
            type: PropertyType,
            price: Long,
            photos: List<PhotoEntity>,
            agent: RealEstateAgentEntity
        ): EstateWithPhotosEntity {
            return EstateWithPhotosEntity(
                estate = getFakeEstateEntity(estateId, type, price, agent.id),
                photos = photos,
                agent = agent
            )
        }

        fun getFakeEstateWithPhotosEntity(
            estate: EstateEntity,
            photos: List<PhotoEntity>,
            agent: RealEstateAgentEntity
        ): EstateWithPhotosEntity {
            return EstateWithPhotosEntity(
                estate = estate,
                photos = photos,
                agent = agent
            )
        }

        fun getFakePhotoEntity(
            uri: String,
            estateId: Long,
            description: String = "Room"
        ): PhotoEntity {
            return PhotoEntity(uri, description, estateId)
        }

        fun getFakePhotoList(estateId: Long): List<PhotoEntity> {
            return listOf(
                getFakePhotoEntity("uri1", estateId),
                getFakePhotoEntity("uri2", estateId)
            )
        }

        fun generateEstateEntity(id:Long, agentId:Long): ContentValues {
            val values = ContentValues()
            values.put("id",id)
            values.put("type",PropertyType.LOFT.name)
            values.put("price",120000)
            values.put("propertyArea",250)
            values.put("numberOfBathrooms",1)
            values.put("numberOfBedrooms",2)
            values.put("description","description")
            values.put("address","123 Main St")
            values.put("additionalAddressLine","")
            values.put("city","New York")
            values.put("zipCode","10001")
            values.put("country","United State")
            values.put("latitude",1.0)
            values.put("longitude", 2.5)
            values.put("pointsOfInterest", PointOfInterest.RESTAURANTS_AND_CAFES.name)
            values.put("available", true)
            values.put("entryDate", 1532655)
            values.put("saleDate", 0)
            values.put("realEstateAgentId", agentId)
            values.put("videoUri", FakeData.getFakeVideo())
            return values
        }

        fun generateAgentEntity(id:Long): ContentValues {
            val values = ContentValues()
            values.put("id",id)
            values.put("displayName","Agent 1")
            return values
        }

    }

}