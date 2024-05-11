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
        TODO("Not yet implemented")
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
