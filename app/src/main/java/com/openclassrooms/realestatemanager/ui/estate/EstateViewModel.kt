package com.openclassrooms.realestatemanager.ui.estate

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class EstateViewModel() : ViewModel() {

    private val selectedEstateId = MutableStateFlow(-1L)

    fun onSelectedEstate(estateId: Long) {
        selectedEstateId.value = estateId
    }

    fun clearSelection() {
        selectedEstateId.value = -1
    }

    fun getSelectedEstate(): Long {
        return selectedEstateId.value
    }

}