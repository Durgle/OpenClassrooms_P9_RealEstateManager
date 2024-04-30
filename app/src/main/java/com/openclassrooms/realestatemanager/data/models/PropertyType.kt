package com.openclassrooms.realestatemanager.data.models

import androidx.annotation.StringRes
import com.openclassrooms.realestatemanager.R

enum class PropertyType(@StringRes val labelResId: Int) {
    APARTMENT(R.string.property_type_apartment),
    LOFT(R.string.property_type_loft),
    MANOR(R.string.property_type_manor),
    HOUSE(R.string.property_type_house),
    DUPLEX(R.string.property_type_duplex);

    @StringRes
    fun getLabel(): Int {
        return labelResId
    }
}
