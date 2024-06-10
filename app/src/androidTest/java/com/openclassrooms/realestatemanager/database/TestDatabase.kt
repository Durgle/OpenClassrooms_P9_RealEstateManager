package com.openclassrooms.realestatemanager.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.openclassrooms.realestatemanager.data.database.converters.PointOfInterestConverter
import com.openclassrooms.realestatemanager.data.database.dao.EstateDao
import com.openclassrooms.realestatemanager.data.database.dao.PhotoDao
import com.openclassrooms.realestatemanager.data.database.dao.RealEstateAgentDao
import com.openclassrooms.realestatemanager.data.database.entities.EstateEntity
import com.openclassrooms.realestatemanager.data.database.entities.PhotoEntity
import com.openclassrooms.realestatemanager.data.database.entities.RealEstateAgentEntity

@Database(
    entities = [EstateEntity::class, PhotoEntity::class, RealEstateAgentEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(PointOfInterestConverter::class)
abstract class TestDatabase : RoomDatabase() {

    abstract fun estateDao(): EstateDao
    abstract fun photoDao(): PhotoDao
    abstract fun realEstateAgentDao(): RealEstateAgentDao

}

