package com.openclassrooms.realestatemanager.ui.estate

interface OnEstateSelectedListener {

    fun onSelection(estateId: Long)

    fun clearSelection()
}