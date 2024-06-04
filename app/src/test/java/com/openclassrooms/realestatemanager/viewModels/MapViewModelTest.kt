package com.openclassrooms.realestatemanager.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.data.repositories.EstateRepository
import com.openclassrooms.realestatemanager.ui.estate.map.MapViewModel
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
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MapViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private var mockEstateRepository: EstateRepository = mockk()
    private lateinit var mapViewModel: MapViewModel
    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = UnconfinedTestDispatcher(testScheduler)
    private val fakeEstates = listOf(
        FakeDataTest.getFakeEstate(estateId = 1L, latitude = 10.00, longitude = 15.15335),
        FakeDataTest.getFakeEstate(estateId = 2L, latitude = 20.00, longitude = 25.15335),
        FakeDataTest.getFakeEstate(estateId = 3L, latitude = 30.00, longitude = 35.15335),
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { mockEstateRepository.getEstates() } returns flowOf(fakeEstates)
        mapViewModel = MapViewModel(mockEstateRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getEstates() = runTest(testDispatcher) {

        val expectedEstateMapViewState = listOf(
            FakeDataTest.getFakeEstateMapViewState(estateId = 1L, location = LatLng(10.00,15.15335)),
            FakeDataTest.getFakeEstateMapViewState(estateId = 2L, location = LatLng(20.00,25.15335)),
            FakeDataTest.getFakeEstateMapViewState(estateId = 3L, location = LatLng(30.00,35.15335))
        )

        val estatesLiveData = mapViewModel.getEstates()
        val estates = LiveDataTestUtil.getOrAwaitValue(estatesLiveData)

        assertEquals(expectedEstateMapViewState, estates)
    }

    @Test
    fun onSelectedEstate() = runTest(testDispatcher) {

        val selectedEstateId = 2L
        val expectedEstateMapViewState = listOf(
            FakeDataTest.getFakeEstateMapViewState(estateId = 1L, location = LatLng(10.00,15.15335)),
            FakeDataTest.getFakeEstateMapViewState(estateId = 2L, location = LatLng(20.00,25.15335), selected = true),
            FakeDataTest.getFakeEstateMapViewState(estateId = 3L, location = LatLng(30.00,35.15335))
        )

        mapViewModel.onSelectedEstate(selectedEstateId)
        val estatesLiveData = mapViewModel.getEstates()
        val estates = LiveDataTestUtil.getOrAwaitValue(estatesLiveData)

        assertEquals(expectedEstateMapViewState, estates)
    }

    @Test
    fun clearSelection() = runTest(testDispatcher) {

        val selectedEstateId = 3L
        val expectedEstateMapViewState = listOf(
            FakeDataTest.getFakeEstateMapViewState(estateId = 1L, location = LatLng(10.00,15.15335)),
            FakeDataTest.getFakeEstateMapViewState(estateId = 2L, location = LatLng(20.00,25.15335)),
            FakeDataTest.getFakeEstateMapViewState(estateId = 3L, location = LatLng(30.00,35.15335))
        )

        mapViewModel.onSelectedEstate(selectedEstateId)
        val estatesLiveData = mapViewModel.getEstates()
        val estates = LiveDataTestUtil.getOrAwaitValue(estatesLiveData)
        assertNotEquals(expectedEstateMapViewState, estates)

        mapViewModel.clearSelection()
        val estatesLiveDataAfterClear = mapViewModel.getEstates()
        val estatesAfterClear = LiveDataTestUtil.getOrAwaitValue(estatesLiveDataAfterClear)
        assertEquals(expectedEstateMapViewState, estatesAfterClear)

    }

}