package com.openclassrooms.realestatemanager.ui.estate

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.openclassrooms.realestatemanager.data.models.Estate
import com.openclassrooms.realestatemanager.data.repositories.EstateRepository
import com.openclassrooms.realestatemanager.ui.estate.viewstate.EstateViewState
import com.openclassrooms.realestatemanager.utils.Utils

class EstateViewModel(private val estateRepository: EstateRepository) : ViewModel() {

    private val estates: LiveData<List<EstateViewState>> =
        estateRepository.getEstates().map { estateList: List<Estate> ->
            val list: MutableList<EstateViewState> = ArrayList()
            for (estate in estateList) {
                list.add(mapEstate(estate))
            }
            list
        }

    companion object {
        fun mapEstate(estate: Estate): EstateViewState {
            return EstateViewState(
                estate.id,
                estate.photos[0],
                estate.type,
                estate.city,
                Utils.formatPrice(estate.price),
                false
            )
        }
    }

    fun getEstates(): LiveData<List<EstateViewState>> {
        return estates;
    }

}