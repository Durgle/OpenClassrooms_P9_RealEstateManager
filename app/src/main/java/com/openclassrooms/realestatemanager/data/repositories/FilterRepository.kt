package com.openclassrooms.realestatemanager.data.repositories

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.openclassrooms.realestatemanager.data.models.EstateFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Repository responsible for managing estate filters
 *
 * @param context The application context
 */
class FilterRepository(private val context: Context) : FilterRepositoryInterface {

    private val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val estateFiltersStateFlow = MutableStateFlow(EstateFilter())
    private val sharedPrefsListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == KEY_FILTERS) {
                estateFiltersStateFlow.value = readSharedPreference()
            }
        }

    /**
     * Saves the estate filters to shared preferences
     *
     * @param filters The [EstateFilter] to save
     */
    override fun saveEstateFilters(filters: EstateFilter) {
        val editor = sharedPreferences.edit()
        val jsonFilters = Gson().toJson(filters)
        editor.putString(KEY_FILTERS, jsonFilters)
        editor.apply()
    }

    /**
     * Retrieves the estate filters from shared preferences as a flow
     *
     * @return A [Flow] emitting the current [EstateFilter]
     */
    override fun getEstateFilters(): Flow<EstateFilter> {
        return estateFiltersStateFlow
    }

    /**
     * Clears the stored estate filters from shared preferences
     */
    override fun clearEstateFilters() {
        val editor = sharedPreferences.edit()
        editor.remove(KEY_FILTERS)
        editor.apply()
    }

    /**
     * Reads the estate filters from shared preferences
     * If no filters are found, it returns an empty [EstateFilter] object
     *
     * @return The [EstateFilter]
     */
    private fun readSharedPreference(): EstateFilter {
        val jsonFilters = sharedPreferences.getString(KEY_FILTERS, null)
        return if (jsonFilters == null) {
            EstateFilter()
        } else {
            Gson().fromJson(jsonFilters, EstateFilter::class.java)
        }
    }

    init {
        estateFiltersStateFlow.value = readSharedPreference()
        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPrefsListener)
    }

    companion object {
        private const val PREFS_NAME = "EstateFilterPrefs"
        const val KEY_FILTERS = "estate_filters"
    }
}
