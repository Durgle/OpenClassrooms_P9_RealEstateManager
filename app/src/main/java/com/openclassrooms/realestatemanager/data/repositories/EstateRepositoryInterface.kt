package com.openclassrooms.realestatemanager.data.repositories

import android.database.Cursor
import com.openclassrooms.realestatemanager.data.models.Estate
import com.openclassrooms.realestatemanager.data.models.EstateFilter
import kotlinx.coroutines.flow.Flow

interface EstateRepositoryInterface {

    /**
     * Inserts or updates an estate in the database.
     *
     * @param estate The estate
     * @param photoToBeRemoved A list of URIs of photos to be removed
     */
    suspend fun upsertEstate(estate: Estate, photoToBeRemoved: List<String>? = null)

    /**
     * Retrieves an estate with its photos from the database as a flow
     *
     * @param estateId The ID of the estate
     * @return A [Flow] emitting the [Estate] with its photos
     */
    fun getEstate(estateId: Long): Flow<Estate>

    /**
     * Retrieves all estates with their photos from the database as a flow
     *
     * @return A [Flow] emitting a list of [Estate] with their photos.
     */
    fun getEstates(): Flow<List<Estate>>

    /**
     * Retrieves estates filtered by the given filters as a flow
     *
     * @param filters The filters
     * @return A [Flow] emitting a list of filtered [Estate]
     */
    fun getEstatesFiltered(filters: EstateFilter): Flow<List<Estate>>

    /**
     * Retrieves an estate by its ID as a cursor
     *
     * @param estateId The ID of the estate
     * @return A [Cursor] pointing to the estate
     */
    fun getEstateByIdWithCursor(estateId: Long): Cursor
}