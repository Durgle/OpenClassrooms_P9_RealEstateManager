package com.openclassrooms.realestatemanager.data.database.dao

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.openclassrooms.realestatemanager.data.database.entities.EstateEntity
import com.openclassrooms.realestatemanager.data.database.entities.EstateWithPhotosEntity
import com.openclassrooms.realestatemanager.data.enums.PropertyType
import kotlinx.coroutines.flow.Flow

@Dao
interface EstateDao {

    @Insert
    suspend fun insertEstate(estate: EstateEntity): Long

    @Insert
    fun insert(estate: EstateEntity): Long

    @Update
    suspend fun updateEstate(estate: EstateEntity)

    @Query("SELECT * FROM estates")
    fun getAllEstates(): Flow<List<EstateEntity>>

    @Query("SELECT * FROM estates WHERE  id = :estateId")
    fun getEstateByIdWithCursor(estateId: Long): Cursor

    @Query("SELECT * FROM estates WHERE id = :estateId")
    fun getEstateById(estateId: Long): Flow<EstateEntity>

    @Transaction
    @Query("SELECT * FROM estates WHERE id = :estateId")
    fun getEstateWithPhotos(estateId: Long): Flow<EstateWithPhotosEntity>

    @Transaction
    @Query("SELECT * FROM estates")
    fun getEstatesWithPhotos(): Flow<List<EstateWithPhotosEntity>>

    @Transaction
    @Query(
        "SELECT * FROM estates WHERE type IN (:type) " +
                "AND (:minPrice IS NULL OR price >= :minPrice) " +
                "AND (:maxPrice IS NULL OR price <= :maxPrice) " +
                "AND (:city IS NULL OR city LIKE '%' || :city || '%' )" +
                "AND (:available IS NULL OR available = :available)"
    )
    fun getFilteredEstatesWithPhotos(
        type: List<PropertyType>,
        minPrice: Long?,
        maxPrice: Long?,
        city: String?,
        available: Boolean?
    ): Flow<List<EstateWithPhotosEntity>>

    @Transaction
    @Query(
        "SELECT * FROM estates WHERE (:minPrice IS NULL OR price >= :minPrice) " +
                "AND (:maxPrice IS NULL OR price <= :maxPrice) " +
                "AND (:city IS NULL OR city LIKE '%' || :city || '%' )" +
                "AND (:available IS NULL OR available = :available)"
    )
    fun getFilteredEstatesWithPhotos(
        minPrice: Long?,
        maxPrice: Long?,
        city: String?,
        available: Boolean?
    ): Flow<List<EstateWithPhotosEntity>>
}