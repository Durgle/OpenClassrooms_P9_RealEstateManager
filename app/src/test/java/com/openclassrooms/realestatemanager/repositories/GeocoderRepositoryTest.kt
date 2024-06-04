package com.openclassrooms.realestatemanager.repositories

import android.location.Address
import android.location.Geocoder
import com.openclassrooms.realestatemanager.data.repositories.GeocoderRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class GeocoderRepositoryTest {

    @Suppress("SpellCheckingInspection")
    private val addressString = "14 Faubourg Saint Honoré 75019 Paris France"
    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = UnconfinedTestDispatcher(testScheduler)
    private lateinit var mockGeocoder: Geocoder
    private lateinit var geocoderRepository: GeocoderRepository

    @Before
    fun setUp() {
        mockGeocoder = mockk()
        geocoderRepository = GeocoderRepository(mockGeocoder)
    }

    @Test
    fun getCoordinatesWithValidLocationName() =
        runTest(testDispatcher) {
            Dispatchers.setMain(testDispatcher)

            try {
                val mockAddress = mockk<Address>()
                @Suppress("DEPRECATION")
                every { mockGeocoder.getFromLocationName(any(), any()) } returns listOf(mockAddress)

                val address = geocoderRepository.getCoordinates(addressString)

                assertEquals(mockAddress, address)
            } finally {
                Dispatchers.resetMain()
            }
        }

    @Test
    fun getCoordinatesWithInvalidLocationName() =
        runTest(testDispatcher) {
            Dispatchers.setMain(testDispatcher)

            try {
                @Suppress("DEPRECATION")
                every { mockGeocoder.getFromLocationName(any(), any()) } returns listOf(null)

                val address = geocoderRepository.getCoordinates(addressString)

                assertNull(address)
            } finally {
                Dispatchers.resetMain()
            }
        }

    @Test
    fun getCoordinatesWithIOException() =
        runTest(testDispatcher) {

            Dispatchers.setMain(testDispatcher)

            try {
                @Suppress("DEPRECATION")
                every { mockGeocoder.getFromLocationName(any(), any()) } throws IOException()

                val address = geocoderRepository.getCoordinates("address causing IOException")

                assertNull(address)
            } finally {
                Dispatchers.resetMain()
            }
        }

}
