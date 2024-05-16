package com.openclassrooms.realestatemanager.ui.estate.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.data.models.Estate
import com.openclassrooms.realestatemanager.data.repositories.EstateRepositoryInterface
import com.openclassrooms.realestatemanager.utils.Utils

class EstateDetailViewModel(
    estateRepository: EstateRepositoryInterface,
    estateId: Long
) : ViewModel() {

    private val estate: LiveData<EstateDetailViewState> =
        estateRepository.getEstate(estateId).asLiveData().map { estate ->
            mapEstateDetail(estate)
        }

    companion object {
        fun mapEstateDetail(estate: Estate): EstateDetailViewState {

            val latLng = if (estate.latitude != null && estate.longitude != null) {
                LatLng(estate.latitude, estate.longitude)
            } else {
                null
            }

            return EstateDetailViewState(
                estate.id,
                Utils.formatPropertyArea(estate.propertyArea),
                estate.photos.orEmpty(),
                estate.description ?: "",
                estate.numberOfBathrooms,
                estate.numberOfBedrooms,
                Utils.formatEstateAddress(
                    estate.address,
                    estate.additionalAddressLine,
                    estate.city,
                    estate.zipCode,
                    estate.country
                ),
                latLng
            )
        }
    }

    fun getEstate(): LiveData<EstateDetailViewState> {
        return estate
    }

}