package com.openclassrooms.realestatemanager.ui.estate

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.data.repositories.EstateRepositoryInterface

class EstateViewModel(private val estateRepository: EstateRepositoryInterface) : ViewModel() {

    fun onSelectedEstate(estateId: Long) {
        estateRepository.onSelectedEstate(estateId)
    }

    fun clearSelection() {
        estateRepository.clearSelectedEstate()
    }

    fun getSelectedEstateId(): Long {
        return estateRepository.getSelectedEstateId()
    }

}