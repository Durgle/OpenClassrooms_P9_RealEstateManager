package com.openclassrooms.realestatemanager.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.data.repositories.EstateRepository
import com.openclassrooms.realestatemanager.ui.estate.detail.EstateDetailViewModel
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
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EstateDetailViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val mockEstateRepository: EstateRepository = mockk()
    private lateinit var estateDetailViewModel: EstateDetailViewModel
    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = UnconfinedTestDispatcher(testScheduler)
    private val fakeEstate =
        FakeDataTest.getFakeEstate(estateId = 1L, latitude = 1.25432, longitude = 68.84562)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { mockEstateRepository.getEstate(fakeEstate.id) } returns flowOf(fakeEstate)
        estateDetailViewModel = EstateDetailViewModel(mockEstateRepository, fakeEstate.id)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getEstate() = runTest {

        val expectedViewState = FakeDataTest.getFakeEstateDetailViewState(
            estateId = fakeEstate.id,
            location = LatLng(1.25432, 68.84562)
        )
        val viewStateLiveData = estateDetailViewModel.getEstate()
        val viewState = LiveDataTestUtil.getOrAwaitValue(viewStateLiveData)

        assertEquals(expectedViewState, viewState)

    }

}