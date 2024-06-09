package com.openclassrooms.realestatemanager.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.openclassrooms.realestatemanager.data.enums.PropertyType
import com.openclassrooms.realestatemanager.data.models.EstateFilter
import com.openclassrooms.realestatemanager.data.repositories.FilterRepository
import com.openclassrooms.realestatemanager.ui.estate.filter.FilterEstateViewModel
import com.openclassrooms.realestatemanager.ui.estate.filter.FilterViewState
import com.openclassrooms.realestatemanager.utils.LiveDataTestUtil
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FilterEstateViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private var mockFilterRepository: FilterRepository = mockk()
    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = UnconfinedTestDispatcher(testScheduler)
    private val fakeEstateFilter =
        EstateFilter(type = listOf(PropertyType.HOUSE), minPrice = 75000, available = true)

    @Before
    fun setUp() {
        every { mockFilterRepository.clearEstateFilters() } just Runs
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getCurrentFilter() = runTest {

        val expectedFilterState = FilterViewState(
            fakeEstateFilter.type.orEmpty(),
            fakeEstateFilter.minPrice?.toFloat(),
            fakeEstateFilter.maxPrice?.toFloat(),
            fakeEstateFilter.city.orEmpty(),
            fakeEstateFilter.available
        )


        every { mockFilterRepository.getEstateFilters() } returns flowOf(fakeEstateFilter)
        val filterViewModel = FilterEstateViewModel(mockFilterRepository)

        val filtersLiveData = filterViewModel.getCurrentFilter()
        val filters = LiveDataTestUtil.getOrAwaitValue(filtersLiveData)

        assertEquals(expectedFilterState, filters)
    }

    @Test
    fun clearFilters() = runTest {

        Dispatchers.setMain(testDispatcher)
        try {
            every { mockFilterRepository.getEstateFilters() } returns flowOf(fakeEstateFilter)
            val filterViewModel = FilterEstateViewModel(mockFilterRepository)

            filterViewModel.clearFilters()

            verify(exactly = 1) { mockFilterRepository.clearEstateFilters() }

        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun saveSuccess() = runTest {

        every { mockFilterRepository.getEstateFilters() } returns flowOf(fakeEstateFilter)
        every { mockFilterRepository.saveEstateFilters(any()) } just Runs
        val filterViewModel = FilterEstateViewModel(mockFilterRepository)
        val expectedEstateFilter =
            EstateFilter(listOf(PropertyType.HOUSE), 75000, 150000, "Paris", true)

        filterViewModel.onTypeSelected(PropertyType.HOUSE, true)
        filterViewModel.onPriceChanged("75000", "150000")
        filterViewModel.onAvailableChanged(true)
        filterViewModel.onCityChanged("Paris")
        filterViewModel.save()

        val snackBar = LiveDataTestUtil.getOrAwaitValue(filterViewModel.snackBar)

        assertEquals(FilterEstateViewModel.Event.SaveSuccess, snackBar)
        verify(exactly = 1) { mockFilterRepository.saveEstateFilters(expectedEstateFilter) }
    }

    @Test
    fun saveFailed() = runTest {

        every { mockFilterRepository.getEstateFilters() } returns flowOf(fakeEstateFilter)
        every { mockFilterRepository.saveEstateFilters(any()) } throws Throwable()
        val filterViewModel = FilterEstateViewModel(mockFilterRepository)
        val expectedEstateFilter = EstateFilter()

        filterViewModel.save()

        val snackBar = LiveDataTestUtil.getOrAwaitValue(filterViewModel.snackBar)

        assertEquals(FilterEstateViewModel.Event.Error(""), snackBar)
        verify(exactly = 1) { mockFilterRepository.saveEstateFilters(expectedEstateFilter) }
    }

}