package com.openclassrooms.realestatemanager.data.database.converters

import androidx.room.TypeConverter
import com.openclassrooms.realestatemanager.data.enums.PointOfInterest

class PointOfInterestConverter {

    @TypeConverter
    fun fromPropertyTypeList(propertyTypeList: List<PointOfInterest>?): String {
        return propertyTypeList?.joinToString(separator = ",") { it.name } ?: ""
    }

    @TypeConverter
    fun toPropertyTypeList(data: String): List<PointOfInterest>? {
        if (data.isEmpty()) return null
        val typeNames = data.split(",")
        return typeNames.map { value -> PointOfInterest.valueOf(value) }
    }

}