package com.openclassrooms.realestatemanager.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.openclassrooms.realestatemanager.data.enums.PropertyType
import com.openclassrooms.realestatemanager.data.models.EstateFilter
import com.openclassrooms.realestatemanager.data.repositories.EstateRepository
import com.openclassrooms.realestatemanager.data.repositories.FilterRepository
import com.openclassrooms.realestatemanager.ui.estate.list.EstateListViewModel
import com.openclassrooms.realestatemanager.utils.FakeDataTest
import com.openclassrooms.realestatemanager.utils.LiveDataTestUtil
import io.mockk.every
import io.mockk.mockk
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
class EstateListViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private var mockEstateRepository: EstateRepository = mockk()
    private var mockFilterRepository: FilterRepository = mockk()
    private lateinit var estateListViewModel: EstateListViewModel
    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = UnconfinedTestDispatcher(testScheduler)
    private val fakeEstates = listOf(
        FakeDataTest.getFakeEstate(estateId = 1L, price = 15000, type = PropertyType.HOUSE, available = false, latitude = null, longitude = null),
        FakeDataTest.getFakeEstate(estateId = 2L, price = 25000, type = PropertyType.HOUSE, available = true, latitude = null, longitude = null),
        FakeDataTest.getFakeEstate(estateId = 3L, price = 35000, type = PropertyType.LOFT, available = true, latitude = null, longitude = null),
        FakeDataTest.getFakeEstate(estateId = 4L, price = 7000, type = PropertyType.LOFT, available = true, latitude = null, longitude = null),
        FakeDataTest.getFakeEstate(estateId = 5L, price = 9000, type = PropertyType.HOUSE, available = false, latitude = null, longitude = null)
    )
    private val fakeEstatesFiltered = listOf(
        FakeDataTest.getFakeEstate(estateId = 2L, price = 25000, type = PropertyType.HOUSE, available = true, latitude = null, longitude = null)
    )
    private val estateFilter =
        EstateFilter(type = listOf(PropertyType.HOUSE), minPrice = 15000, available = true)
    private val selectedEstateId = 5L

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getEstates() = runTest(testDispatcher) {

        every { mockEstateRepository.getEstates() } returns flowOf(fakeEstates)
        every { mockEstateRepository.getSelectedEstate() } returns flowOf(selectedEstateId)
        every { mockFilterRepository.getEstateFilters() } returns flowOf(EstateFilter())
        estateListViewModel = EstateListViewModel(mockEstateRepository, mockFilterRepository)

        val expectedEstateMapViewState = listOf(
            FakeDataTest.getFakeEstateViewState(estateId = 1L, price = "$15,000", type = PropertyType.HOUSE, available = false),
            FakeDataTest.getFakeEstateViewState(estateId = 2L, price = "$25,000", type = PropertyType.HOUSE, available = true),
            FakeDataTest.getFakeEstateViewState(estateId = 3L, price = "$35,000", type = PropertyType.LOFT, available = true),
            FakeDataTest.getFakeEstateViewState(estateId = 4L, price = "$7,000", type = PropertyType.LOFT, available = true),
            FakeDataTest.getFakeEstateViewState(estateId = 5L, price = "$9,000", type = PropertyType.HOUSE, available = false, selected = true),
        )

        val estatesLiveData = estateListViewModel.getEstates()
        val estates = LiveDataTestUtil.getOrAwaitValue(estatesLiveData)

        assertEquals(expectedEstateMapViewState, estates)
    }

    @Test
    fun getEstatesWithFilter() = runTest(testDispatcher) {

        every { mockEstateRepository.getEstates() } returns flowOf(fakeEstates)
        every { mockEstateRepository.getSelectedEstate() } returns flowOf(selectedEstateId)
        every { mockEstateRepository.getEstatesFiltered(estateFilter) } returns flowOf(fakeEstatesFiltered)
        every { mockFilterRepository.getEstateFilters() } returns flowOf(estateFilter)
        estateListViewModel = EstateListViewModel(mockEstateRepository, mockFilterRepository)

        val expectedEstateMapViewState = listOf(
            FakeDataTest.getFakeEstateViewState(estateId = 2L, price = "$25,000", type = PropertyType.HOUSE, available = true)
        )

        val estatesLiveData = estateListViewModel.getEstates()
        val estates = LiveDataTestUtil.getOrAwaitValue(estatesLiveData)

        assertEquals(expectedEstateMapViewState, estates)
    }

}