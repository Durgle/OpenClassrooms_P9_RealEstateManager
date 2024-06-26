package com.openclassrooms.realestatemanager.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.openclassrooms.realestatemanager.data.database.entities.PhotoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {

    @Insert
    suspend fun insertPhotos(photos: List<PhotoEntity>): List<Long>

    @Upsert(entity = PhotoEntity::class)
    suspend fun upsertPhotos(photos: List<PhotoEntity>)

    @Query("SELECT * FROM photos")
    fun getAllPhotos(): Flow<List<PhotoEntity>>

    @Query("DELETE FROM photos WHERE uri IN (:uriList) AND estateId = :estateId")
    suspend fun deletePhotos(uriList: List<String>, estateId: Long)
}