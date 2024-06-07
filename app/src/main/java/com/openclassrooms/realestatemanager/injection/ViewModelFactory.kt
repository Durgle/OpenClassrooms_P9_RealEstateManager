package com.openclassrooms.realestatemanager.injection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.openclassrooms.realestatemanager.MainApplication
import com.openclassrooms.realestatemanager.ui.estate.EstateViewModel
import com.openclassrooms.realestatemanager.ui.estate.upsert.UpsertEstateViewModel
import com.openclassrooms.realestatemanager.ui.estate.detail.EstateDetailFragment.Companion.ARG_ESTATE_ID
import com.openclassrooms.realestatemanager.ui.estate.detail.EstateDetailViewModel
import com.openclassrooms.realestatemanager.ui.estate.filter.FilterEstateViewModel
import com.openclassrooms.realestatemanager.ui.estate.list.EstateListViewModel

class ViewModelFactory private constructor() : ViewModelProvider.Factory {

    private val CreationExtras.application: MainApplication
        get() = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MainApplication

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory().also { instance = it }
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {

        return when {
            modelClass.isAssignableFrom(EstateListViewModel::class.java) ->
                EstateListViewModel(
                    extras.application.estateRepository,
                    extras.application.filterRepository
                ) as T

            modelClass.isAssignableFrom(EstateViewModel::class.java) ->
                EstateViewModel(extras.application.estateRepository) as T

            modelClass.isAssignableFrom(EstateDetailViewModel::class.java) -> {
                val savedStateHandle = extras.createSavedStateHandle()
                val estateId = savedStateHandle.get<Long>(ARG_ESTATE_ID)
                    ?: throw IllegalArgumentException("Estate id not passed")
                EstateDetailViewModel(extras.application.estateRepository, estateId) as T
            }

            modelClass.isAssignableFrom(UpsertEstateViewModel::class.java) -> {
                val savedStateHandle = extras.createSavedStateHandle()
                val estateId = savedStateHandle.get<Long>(ARG_ESTATE_ID)
                UpsertEstateViewModel(
                    extras.application.estateRepository,
                    extras.application.realEstateAgentRepository,
                    extras.application.geocoderRepository,
                    extras.application.resources,
                    estateId
                ) as T
            }

            modelClass.isAssignableFrom(FilterEstateViewModel::class.java) ->
                FilterEstateViewModel(extras.application.filterRepository) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }

    }

}