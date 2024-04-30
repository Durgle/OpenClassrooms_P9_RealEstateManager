package com.openclassrooms.realestatemanager.injection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.openclassrooms.realestatemanager.data.repositories.EstateRepository
import com.openclassrooms.realestatemanager.ui.estate.EstateDetailFragment.Companion.ARG_ESTATE_ID
import com.openclassrooms.realestatemanager.ui.estate.EstateDetailViewModel
import com.openclassrooms.realestatemanager.ui.estate.EstateViewModel

class ViewModelFactory private constructor(
    private val estateRepository: EstateRepository
) : ViewModelProvider.Factory {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(EstateRepository()).also { instance = it }
            }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {

        return when {
            modelClass.isAssignableFrom(EstateViewModel::class.java) ->
                EstateViewModel(estateRepository) as T

            modelClass.isAssignableFrom(EstateDetailViewModel::class.java) -> {
                val savedStateHandle = extras.createSavedStateHandle()
                val estateId = savedStateHandle.get<Long>(ARG_ESTATE_ID)
                estateId?.let { EstateDetailViewModel(estateRepository, it) } as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }


}