package com.openclassrooms.realestatemanager.ui.estate.list

import android.annotation.SuppressLint
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.data.models.Estate
import com.openclassrooms.realestatemanager.data.repositories.EstateRepositoryInterface
import com.openclassrooms.realestatemanager.data.repositories.FilterRepositoryInterface
import com.openclassrooms.realestatemanager.data.repositories.LocationRepository
import com.openclassrooms.realestatemanager.utils.Utils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest

class EstateListViewModel(
    private val estateRepository: EstateRepositoryInterface,
    filterRepository: FilterRepositoryInterface,
    private val locationRepository: LocationRepository,
) : ViewModel() {

    private val selectedEstateId = estateRepository.getSelectedEstate()

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
        val location = if (estate.latitude != null && estate.longitude != null) {
            LatLng(estate.latitude, estate.longitude)
        } else {
            null
        }
        return EstateViewState(
            id = estate.id,
            photo = photo,
            propertyType = estate.type,
            city = estate.city,
            price = Utils.formatPrice(estate.price),
            available = estate.available,
            selected = selectedId == estate.id,
            location = location
        )
    }

    fun getEstates(): LiveData<List<EstateViewState>> {
        return estates
    }

    fun getCurrentLocation(): LiveData<Location> {
        return locationRepository.locationLiveData
    }

    @SuppressLint("MissingPermission")
    fun refreshLocation(granted: Boolean) {
        if (granted) {
            locationRepository.startLocationRequest();
        } else {
            locationRepository.stopLocationRequest();
        }
    }

}