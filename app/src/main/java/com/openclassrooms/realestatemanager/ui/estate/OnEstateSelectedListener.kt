package com.openclassrooms.realestatemanager.ui.estate

interface OnEstateSelectedListener {

    /**
     * Called when an estate has been selected
     *
     * @param estateId The ID of the selected estate
     */
    fun onSelection(estateId: Long)
}