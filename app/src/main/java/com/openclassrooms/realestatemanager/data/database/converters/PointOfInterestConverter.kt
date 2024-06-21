package com.openclassrooms.realestatemanager.data.database.converters

import androidx.room.TypeConverter
import com.openclassrooms.realestatemanager.data.enums.PointOfInterest

/**
 * Converter class for converting a list of [PointOfInterest] objects to a string and vice versa
 */
class PointOfInterestConverter {

    /**
     * Converts a list of [PointOfInterest] objects to a comma-separated string
     *
     * @param propertyTypeList The list of [PointOfInterest]
     * @return A comma-separated string representation of the list, or an empty string if the list is null
     */
    @TypeConverter
    fun fromPointOfInterestList(propertyTypeList: List<PointOfInterest>?): String {
        return propertyTypeList?.joinToString(separator = ",") { it.name } ?: ""
    }

    /**
     * Converts a comma-separated string to a list of [PointOfInterest]
     *
     * @param data The comma-separated string
     * @return A list of [PointOfInterest] objects, or null if the input string is empty
     */
    @TypeConverter
    fun toPointOfInterestList(data: String): List<PointOfInterest>? {
        if (data.isEmpty()) return null
        val typeNames = data.split(",")
        return typeNames.map { value -> PointOfInterest.valueOf(value) }
    }

}