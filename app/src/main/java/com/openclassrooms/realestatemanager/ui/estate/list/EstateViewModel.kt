package com.openclassrooms.realestatemanager.ui.estate.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.openclassrooms.realestatemanager.data.models.Estate
import com.openclassrooms.realestatemanager.data.repositories.EstateRepositoryInterface
import com.openclassrooms.realestatemanager.data.repositories.FilterRepositoryInterface
import com.openclassrooms.realestatemanager.utils.Utils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest

class EstateViewModel(
    estateRepository: EstateRepositoryInterface,
    filterRepository: FilterRepositoryInterface
) : ViewModel() {

    private val selectedEstateId = MutableStateFlow(-1L)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val estates: LiveData<List<EstateViewState>> =
        filterRepository.getEstateFilters().flatMapLatest { filters ->
            if (!filters.hasFilter()) {
                estateRepository.getEstates()
            } else {
                estateRepository.getEstatesFiltered(filters)
            }
        }.combine(selectedEstateId) { estates, selectedId ->
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
        selectedEstateId.value = -1
    }

    fun getSelectedEstate(): Long {
        return selectedEstateId.value
    }

}