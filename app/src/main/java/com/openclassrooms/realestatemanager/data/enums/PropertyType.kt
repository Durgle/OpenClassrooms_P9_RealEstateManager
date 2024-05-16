package com.openclassrooms.realestatemanager.data.enums

import androidx.annotation.StringRes
import com.openclassrooms.realestatemanager.R

enum class PropertyType(val id: Int, @StringRes val labelResId: Int) {
    APARTMENT(1, R.string.property_type_apartment),
    LOFT(2, R.string.property_type_loft),
    MANOR(3, R.string.property_type_manor),
    HOUSE(4, R.string.property_type_house),
    DUPLEX(5, R.string.property_type_duplex);
}
