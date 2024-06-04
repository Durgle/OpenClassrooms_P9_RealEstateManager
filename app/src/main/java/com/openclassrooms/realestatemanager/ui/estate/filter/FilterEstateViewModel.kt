package com.openclassrooms.realestatemanager.ui.estate.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.data.enums.PropertyType
import com.openclassrooms.realestatemanager.data.models.EstateFilter
import com.openclassrooms.realestatemanager.data.repositories.FilterRepositoryInterface
import com.openclassrooms.realestatemanager.utils.SingleLiveEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class FilterEstateViewModel(
    private val filterRepository: FilterRepositoryInterface,
) : ViewModel() {

    private val _filterForm = MutableStateFlow(FilterEstateForm())
    private val _currentFilter =
        filterRepository.getEstateFilters().map { filter -> mapToFilterViewState(filter) }

    val snackBar = SingleLiveEvent<Event>()

    private fun mapToFilterViewState(filters: EstateFilter): FilterViewState {
        return FilterViewState(
            filters.type.orEmpty(),
            filters.minPrice?.toFloat(),
            filters.maxPrice?.toFloat(),
            filters.city ?: "",
            filters.available
        )
    }

    fun save() {

        viewModelScope.launch {
            val filters = _filterForm.value
            try {
                filterRepository.saveEstateFilters(
                    EstateFilter(
                        filters.type.ifEmpty { null },
                        filters.minPrice.toLongOrNull(),
                        filters.maxPrice.toLongOrNull(),
                        filters.city.ifEmpty { null },
                        filters.available
                    )
                )
                snackBar.value = Event.SaveSuccess
            } catch (error: Throwable) {
                snackBar.value = Event.Error("")
            }
        }

    }

    fun onPriceChanged(minPrice: String, maxPrice: String) {
        _filterForm.value =
            _filterForm.value.copy(minPrice = minPrice.trim(), maxPrice = maxPrice.trim())
    }

    fun onCityChanged(city: String) {
        _filterForm.value = _filterForm.value.copy(city = city.trim())
    }

    fun onAvailableChanged(available: Boolean?) {
        _filterForm.value = _filterForm.value.copy(available = available)
    }

    fun onTypeSelected(type: PropertyType, isSelected: Boolean) {

        val currentSelection = _filterForm.value.type.toMutableList()
        if (isSelected && !currentSelection.contains(type)) {
            currentSelection.add(type)
        } else if (!isSelected && currentSelection.contains(type)) {
            currentSelection.remove(type)
        }
        _filterForm.value = _filterForm.value.copy(type = currentSelection)
    }

    sealed interface Event {
        data object SaveSuccess : Event
        data class Error(val message: String) : Event
    }

    fun getCurrentFilter(): LiveData<FilterViewState> {
        return _currentFilter.asLiveData()
    }

    fun clearFilters() {
        filterRepository.clearEstateFilters()
    }

}