package com.openclassrooms.realestatemanager.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.maps.model.LatLng
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
        FakeDataTest.getFakeEstate(estateId = 1L, latitude = 10.00, longitude = 15.15335),
        FakeDataTest.getFakeEstate(estateId = 2L, latitude = 20.00, longitude = 25.15335),
        FakeDataTest.getFakeEstate(estateId = 3L, latitude = 30.00, longitude = 35.15335),
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { mockEstateRepository.getEstates() } returns flowOf(fakeEstates)
        estateListViewModel = EstateListViewModel(mockEstateRepository, mockFilterRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getEstates() = runTest(testDispatcher) {

        val expectedEstateMapViewState = listOf(
            FakeDataTest.getFakeEstateViewState(estateId = 1L, location = LatLng(10.00, 15.15335)),
            FakeDataTest.getFakeEstateViewState(estateId = 2L, location = LatLng(20.00, 25.15335)),
            FakeDataTest.getFakeEstateViewState(estateId = 3L, location = LatLng(30.00, 35.15335))
        )

        val estatesLiveData = estateListViewModel.getEstates()
        val estates = LiveDataTestUtil.getOrAwaitValue(estatesLiveData)

        assertEquals(expectedEstateMapViewState, estates)
    }

}