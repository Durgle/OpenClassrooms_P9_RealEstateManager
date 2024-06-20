package com.openclassrooms.realestatemanager.data.repositories

import android.content.ContentValues
import android.database.Cursor
import com.openclassrooms.realestatemanager.data.database.dao.EstateDao
import com.openclassrooms.realestatemanager.data.database.dao.PhotoDao
import com.openclassrooms.realestatemanager.data.database.entities.EstateEntity
import com.openclassrooms.realestatemanager.data.models.Estate
import com.openclassrooms.realestatemanager.data.database.entities.EstateWithPhotosEntity
import com.openclassrooms.realestatemanager.data.database.entities.PhotoEntity
import com.openclassrooms.realestatemanager.data.models.EstateFilter
import com.openclassrooms.realestatemanager.data.models.Photo
import com.openclassrooms.realestatemanager.data.models.RealEstateAgent
import com.openclassrooms.realestatemanager.worker.WorkManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/**
 * Repository responsible for managing estates
 *
 * @param estateDao The DAO for estate
 * @param photoDao The DAO for photo
 * @param workManager The WorkManager
 */
class EstateRepository(
    private val estateDao: EstateDao,
    private val photoDao: PhotoDao,
    private val workManager: WorkManager
) : EstateRepositoryInterface {

    private val selectedEstateId = MutableStateFlow(0L)

    /**
     * Updates the selected estate ID
     *
     * @param estateId The ID of the selected estate
     */
    override fun onSelectedEstate(estateId: Long) {
        selectedEstateId.value = estateId
    }

    /**
     * Clear the selected estate ID
     */
    override fun clearSelectedEstate() {
        selectedEstateId.value = 0L
    }

    /**
     * Retrieves the selected estate ID as flow
     *
     * @return A [Flow] that emits the selected estate ID
     */
    override fun getSelectedEstate(): Flow<Long> {
        return selectedEstateId
    }

    /**
     * Retrieves the selected estate ID
     *
     * @return The selected estate ID
     */
    override fun getSelectedEstateId(): Long {
        return selectedEstateId.value
    }

    /**
     * Inserts or updates an estate in the database.
     *
     * @param estate The estate
     * @param photoToBeRemoved A list of URIs of photos to be removed
     */
    override suspend fun upsertEstate(estate: Estate, photoToBeRemoved: List<String>?) {

        val estateEntity = EstateEntity(
            id = estate.id,
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
            entryDate = estate.entryDate,
            saleDate = estate.saleDate,
            realEstateAgentId = estate.realEstateAgent.id,
            videoUri = estate.videoUri
        )

        val estateId = if (estate.id == 0L) {
            estateDao.insertEstate(estateEntity)
        } else {
            estateDao.updateEstate(estateEntity)
            estateEntity.id
        }

        if (estate.photos != null) {
            photoDao.upsertPhotos(
                estate.photos.map { photo ->
                    PhotoEntity(
                        uri = photo.uri,
                        description = photo.description,
                        estateId = estateId
                    )
                })
        }

        if (!photoToBeRemoved.isNullOrEmpty() && estate.id != 0L) {
            photoDao.deletePhotos(photoToBeRemoved, estateId = estateId)
        }

        if (estate.id == 0L) {
            workManager.scheduleEstateNotification()
        }
    }

    /**
     * Inserts an estate in the database from content values.
     *
     * @param values The content values
     */
    override fun insertFromContentValues(values: ContentValues): Long {
        return estateDao.insert(EstateEntity.fromContentValues(values))
    }

    /**
     * Retrieves an estate with its photos from the database as a flow
     *
     * @param estateId The ID of the estate
     * @return A [Flow] emitting the [Estate] with its photos
     */
    override fun getEstate(estateId: Long): Flow<Estate> {
        return estateDao.getEstateWithPhotos(estateId).map { estateWithPhotos ->
            mapToEstate(estateWithPhotos)
        }
    }

    /**
     * Retrieves all estates with their photos from the database as a flow
     *
     * @return A [Flow] emitting a list of [Estate] with their photos.
     */
    override fun getEstates(): Flow<List<Estate>> {
        return estateDao.getEstatesWithPhotos().map { estatesWithPhotosList ->
            estatesWithPhotosList.map { estateWithPhotos ->
                mapToEstate(estateWithPhotos)
            }
        }
    }

    /**
     * Retrieves estates filtered by the given filters as a flow
     *
     * @param filters The filters
     * @return A [Flow] emitting a list of filtered [Estate]
     */
    override fun getEstatesFiltered(filters: EstateFilter): Flow<List<Estate>> {
        val list: Flow<List<EstateWithPhotosEntity>>
        if (filters.type.isNullOrEmpty()) {
            list = estateDao.getFilteredEstatesWithPhotos(
                filters.minPrice,
                filters.maxPrice,
                filters.city,
                filters.available
            )
        } else {
            list = estateDao.getFilteredEstatesWithPhotos(
                filters.type,
                filters.minPrice,
                filters.maxPrice,
                filters.city,
                filters.available
            )
        }
        return list.map { estatesWithPhotosList ->
            estatesWithPhotosList.map { estateWithPhotos ->
                mapToEstate(estateWithPhotos)
            }
        }
    }

    /**
     * Retrieves an estate by its ID as a cursor
     *
     * @param estateId The ID of the estate
     * @return A [Cursor] pointing to the estate
     */
    override fun getEstateByIdWithCursor(estateId: Long): Cursor {
        return estateDao.getEstateByIdWithCursor(estateId)
    }

    /**
     * Maps an [EstateWithPhotosEntity] to an [Estate]
     *
     * @param estateWithPhotos The entity to map
     * @return The [Estate]
     */
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
                    photo.uri,
                    photo.description,
                    photo.estateId
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
            RealEstateAgent(
                estateWithPhotos.agent.id,
                estateWithPhotos.agent.displayName
            ),
            estateWithPhotos.estate.videoUri
        )
    }

}
