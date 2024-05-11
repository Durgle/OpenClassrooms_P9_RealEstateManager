package com.openclassrooms.realestatemanager.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.openclassrooms.realestatemanager.data.database.entities.EstateEntity
import com.openclassrooms.realestatemanager.data.database.entities.EstateWithPhotosEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EstateDao {

    @Insert
    suspend fun insertEstate(estate: EstateEntity): Long

    @Update
    suspend fun updateEstate(estate: EstateEntity)

    @Query("SELECT * FROM estates")
    fun getAllEstates(): Flow<List<EstateEntity>>

    @Query("SELECT * FROM estates WHERE id = :estateId")
    fun getEstateById(estateId: Long): Flow<EstateEntity>

    @Transaction
    @Query("SELECT * FROM estates WHERE id = :estateId")
    fun getEstateWithPhotos(estateId: Long): Flow<EstateWithPhotosEntity>

    @Transaction
    @Query("SELECT * FROM estates")
    fun getEstatesWithPhotos(): Flow<List<EstateWithPhotosEntity>>
}