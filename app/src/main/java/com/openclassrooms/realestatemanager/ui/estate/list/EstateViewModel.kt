package com.openclassrooms.realestatemanager.ui.estate.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.openclassrooms.realestatemanager.data.models.Estate
import com.openclassrooms.realestatemanager.data.repositories.EstateRepositoryInterface
import com.openclassrooms.realestatemanager.utils.Utils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class EstateViewModel(estateRepository: EstateRepositoryInterface) : ViewModel() {

    private val selectedEstateId = MutableStateFlow(0L)

    private val estates: LiveData<List<EstateViewState>> =
        estateRepository.getEstates().combine(selectedEstateId) { estates, selectedId ->
            estates.map { estate -> mapToEstateViewState(estate, selectedId) }
        }.asLiveData()

    private fun mapToEstateViewState(estate: Estate, selectedId: Long): EstateViewState {
        val photo = if (!estate.photos.isNullOrEmpty()) {
            estate.photos.first()
        } else {
            null
        }
        return EstateViewState(
            estate.id,
            photo,
            estate.type,
            estate.city,
            Utils.formatPrice(estate.price),
            selectedId == estate.id
        )
    }

    fun onSelectedEstate(estateId: Long) {
        selectedEstateId.value = estateId
    }

    fun getEstates(): LiveData<List<EstateViewState>> {
        return estates
    }

    fun clearSelection() {
        selectedEstateId.value = 0
    }

}