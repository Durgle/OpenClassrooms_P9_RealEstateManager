package com.openclassrooms.realestatemanager.data.enums

import androidx.annotation.StringRes
import com.openclassrooms.realestatemanager.R

enum class PointOfInterest(val id: Int, @StringRes val labelResId: Int) {
    PARKS_AND_GREEN_SPACES(1, R.string.poi_parks_and_green_spaces),
    SCHOOLS_AND_UNIVERSITIES(2, R.string.poi_schools_and_universities),
    SHOPS_AND_MALLS(3, R.string.poi_shops_and_malls),
    RESTAURANTS_AND_CAFES(4, R.string.poi_restaurants_and_cafes),
    PUBLIC_TRANSPORT(5, R.string.poi_public_transport),
    HEALTH_SERVICES(6, R.string.poi_health_services),
    COMMUNITY_SPACES(7, R.string.poi_community_spaces),
}
