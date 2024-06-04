package com.openclassrooms.realestatemanager.repositories

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.openclassrooms.realestatemanager.data.enums.PropertyType
import com.openclassrooms.realestatemanager.data.models.EstateFilter
import com.openclassrooms.realestatemanager.data.repositories.FilterRepository
import com.openclassrooms.realestatemanager.data.repositories.FilterRepository.Companion.KEY_FILTERS
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FilterRepositoryTest {

    private lateinit var mockContext: Context
    private lateinit var mockSharedPreferences: SharedPreferences
    private lateinit var mockEditor: SharedPreferences.Editor
    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = UnconfinedTestDispatcher(testScheduler)

    @Before
    fun setUp() {
        mockContext = mockk()
        mockSharedPreferences = mockk()
        mockEditor = mockk()

        every { mockEditor.remove(any()) } returns mockEditor
        every { mockEditor.apply() } just Runs
        every { mockSharedPreferences.edit() } returns mockEditor
        every { mockSharedPreferences.registerOnSharedPreferenceChangeListener(any()) } just Runs
        every { mockContext.getSharedPreferences(any(), any()) } returns mockSharedPreferences
    }

    @Test
    fun clearEstateFilters() {

        every { mockSharedPreferences.getString(any(), null) } returns null
        val filterRepository = FilterRepository(mockContext)

        filterRepository.clearEstateFilters()

        verifyOrder {
            mockEditor.remove(KEY_FILTERS)
            mockEditor.apply()
        }
    }

    @Test
    fun saveEstateFilters() {

        every { mockEditor.putString(any(), any()) } returns mockEditor
        every { mockSharedPreferences.getString(any(), null) } returns null
        val filterRepository = FilterRepository(mockContext)
        val estateFilter = EstateFilter(
            type = listOf(PropertyType.HOUSE, PropertyType.LOFT),
            available = true
        )
        val expectedParams = Gson().toJson(estateFilter)

        filterRepository.saveEstateFilters(estateFilter)

        verifyOrder {
            mockEditor.putString(KEY_FILTERS, expectedParams)
            mockEditor.apply()
        }
    }

    @Test
    fun getEstateFiltersWithFiltersSaved() = runTest(testDispatcher){

        val expectedFilters = EstateFilter(
            type = listOf(PropertyType.HOUSE, PropertyType.LOFT),
            available = true
        )
        val filtersSaved = Gson().toJson(expectedFilters)
        every { mockEditor.putString(any(), any()) } returns mockEditor
        every { mockSharedPreferences.getString(any(), null) } returns filtersSaved
        val filterRepository = FilterRepository(mockContext)

        val result = filterRepository.getEstateFilters().first()

        assertEquals(expectedFilters, result)
    }

    @Test
    fun getEstateFiltersWithoutFiltersSaved() = runTest(testDispatcher){

        val expectedResult = EstateFilter()
        every { mockEditor.putString(any(), any()) } returns mockEditor
        every { mockSharedPreferences.getString(any(), null) } returns null
        val filterRepository = FilterRepository(mockContext)

        val result = filterRepository.getEstateFilters().first()

        assertEquals(expectedResult, result)
    }

}
