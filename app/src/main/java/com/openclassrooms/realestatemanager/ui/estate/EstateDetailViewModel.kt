package com.openclassrooms.realestatemanager.ui.estate

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.bumptech.glide.util.Util
import com.openclassrooms.realestatemanager.data.models.Estate
import com.openclassrooms.realestatemanager.data.repositories.EstateRepository
import com.openclassrooms.realestatemanager.ui.estate.viewstate.EstateDetailViewState
import com.openclassrooms.realestatemanager.utils.Utils

class EstateDetailViewModel(
    private val estateRepository: EstateRepository,
    private val estateId: Long
) : ViewModel() {

    private val estate: LiveData<EstateDetailViewState> =
        estateRepository.getEstateById(estateId).map { estate: Estate? ->
            if (estate == null) throw IllegalStateException("Estate is null")
            else mapEstateDetail(estate)
        }

    companion object {
        fun mapEstateDetail(estate: Estate): EstateDetailViewState {
            return EstateDetailViewState(
                estate.id,
                Utils.formatPropertyArea(estate.propertyArea),
                estate.photos,
                estate.description ?: "",
                estate.numberOfBathrooms,
                estate.numberOfBedrooms,
                Utils.formatEstateAddress(
                    estate.address,
                    estate.additionalAddressLine,
                    estate.city,
                    estate.zipCode,
                    estate.country
                )
            )
        }
    }

    fun getEstate(): LiveData<EstateDetailViewState> {
        return estate;
    }

}