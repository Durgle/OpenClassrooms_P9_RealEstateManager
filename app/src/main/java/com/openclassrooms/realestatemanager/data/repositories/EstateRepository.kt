package com.openclassrooms.realestatemanager.data.repositories

import com.openclassrooms.realestatemanager.data.database.dao.EstateDao
import com.openclassrooms.realestatemanager.data.database.dao.PhotoDao
import com.openclassrooms.realestatemanager.data.database.entities.EstateEntity
import com.openclassrooms.realestatemanager.data.models.Estate
import com.openclassrooms.realestatemanager.data.database.entities.EstateWithPhotosEntity
import com.openclassrooms.realestatemanager.data.database.entities.PhotoEntity
import com.openclassrooms.realestatemanager.data.models.Photo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EstateRepository(private val estateDao: EstateDao, private val photoDao: PhotoDao) :
    EstateRepositoryInterface {

    override suspend fun insertEstate(estate: Estate): Long {
        val estateSavedId = estateDao.insertEstate(
            EstateEntity(
                type = estate.type,
                price = estate.price,
                propertyArea = estate.propertyArea,
                numberOfBathrooms = estate.numberOfBathrooms,
                numberOfBedrooms = estate.numberOfBedrooms,
                description = estate.description,
                address = estate.address,
                additionalAddressLine = estate.additionalAddressLine,
                city = estate.city,
                zipCode = estate.zipCode,
                country = estate.country,
                latitude = estate.latitude,
                longitude = estate.longitude,
                pointsOfInterest = estate.pointsOfInterest,
                available = estate.available,
                entryDate = System.currentTimeMillis(),
                saleDate = null,
                realEstateAgentId = estate.realEstateAgentId
            )
        )
        if (estate.photos != null) {
            photoDao.insertPhotos(estate.photos.map { photoDomain ->
                PhotoEntity(
                    photoPath = photoDomain.photoPath,
                    description = photoDomain.description,
                    estateId = estateSavedId
                )
            })
        }
        return estateSavedId
    }

    override suspend fun updateEstate(estate: Estate) {
        TODO("Not yet implemented")
    }

    override fun getEstate(estateId: Long): Flow<Estate> {
        return estateDao.getEstateWithPhotos(estateId).map { estateWithPhotos ->
            mapToEstate(estateWithPhotos)
        }
    }

    override fun getEstates(): Flow<List<Estate>> {
        return estateDao.getEstatesWithPhotos().map { estatesWithPhotosList ->
            estatesWithPhotosList.map { estateWithPhotos ->
                mapToEstate(estateWithPhotos)
            }
        }
    }

    private fun mapToEstate(estateWithPhotos: EstateWithPhotosEntity): Estate {
        return Estate(
            estateWithPhotos.estate.id,
            estateWithPhotos.estate.type,
            estateWithPhotos.estate.price,
            estateWithPhotos.estate.propertyArea,
            estateWithPhotos.estate.numberOfBathrooms,
            estateWithPhotos.estate.numberOfBedrooms,
            estateWithPhotos.estate.description,
            estateWithPhotos.photos?.map { photo ->
                Photo(
                    photo.id,
                    photo.photoPath,
                    photo.description
                )
            },
            estateWithPhotos.estate.address,
            estateWithPhotos.estate.additionalAddressLine,
            estateWithPhotos.estate.city,
            estateWithPhotos.estate.zipCode,
            estateWithPhotos.estate.country,
            estateWithPhotos.estate.latitude,
            estateWithPhotos.estate.longitude,
            estateWithPhotos.estate.pointsOfInterest,
            estateWithPhotos.estate.available,
            estateWithPhotos.estate.entryDate,
            estateWithPhotos.estate.saleDate,
            estateWithPhotos.estate.realEstateAgentId
        )
    }


}
