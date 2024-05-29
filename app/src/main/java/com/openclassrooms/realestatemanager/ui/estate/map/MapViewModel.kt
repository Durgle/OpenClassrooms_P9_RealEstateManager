package com.openclassrooms.realestatemanager.ui.estate.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.data.models.Estate
import com.openclassrooms.realestatemanager.data.repositories.EstateRepositoryInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class MapViewModel(estateRepository: EstateRepositoryInterface) : ViewModel() {

    private val selectedEstateId = MutableStateFlow(0L)

    private val estates: LiveData<List<EstateMapViewState>> =
        estateRepository.getEstates().combine(selectedEstateId) { estates, selectedId ->
            estates.map { estate -> mapToViewState(estate, selectedId) }
        }.asLiveData()

    private fun mapToViewState(estate: Estate, selectedId: Long): EstateMapViewState {
        val location = if (estate.latitude != null && estate.longitude != null) {
            LatLng(estate.latitude, estate.longitude)
        } else {
            null
        }
        return EstateMapViewState(
            estate.id,
            estate.type.labelResId,
            location,
            selectedId == estate.id
        )
    }

    fun onSelectedEstate(estateId: Long) {
        selectedEstateId.value = estateId
    }

    fun clearSelection() {
        selectedEstateId.value = -1
    }

    fun getEstates(): LiveData<List<EstateMapViewState>> {
        return estates
    }

}