package com.openclassrooms.realestatemanager.repositories

import android.database.Cursor
import com.openclassrooms.realestatemanager.data.database.dao.EstateDao
import com.openclassrooms.realestatemanager.data.database.dao.PhotoDao
import com.openclassrooms.realestatemanager.data.enums.PropertyType
import com.openclassrooms.realestatemanager.data.models.EstateFilter
import com.openclassrooms.realestatemanager.data.repositories.EstateRepository
import com.openclassrooms.realestatemanager.utils.FakeDataTest
import com.openclassrooms.realestatemanager.worker.WorkManager
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EstateRepositoryTest {

    private lateinit var mockEstateDao: EstateDao
    private lateinit var mockPhotoDao: PhotoDao
    private lateinit var mockWorkManager: WorkManager
    private lateinit var estateRepository: EstateRepository
    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = UnconfinedTestDispatcher(testScheduler)

    @Before
    fun setUp() {
        mockEstateDao = mockk()
        mockPhotoDao = mockk()
        mockWorkManager = mockk()
        estateRepository = EstateRepository(mockEstateDao, mockPhotoDao, mockWorkManager)
    }

    @Test
    fun upsertEstateWithNonExistentEstate() = runTest(testDispatcher) {
        val estateIdSaved = 1L
        val expectedEstateEntity = FakeDataTest.getFakeEstateEntity(estateId = 0L)
        val expectedPhotoEntities = listOf(
            FakeDataTest.getFakePhotoEntity(uri = "Photo 1", estateId = estateIdSaved),
            FakeDataTest.getFakePhotoEntity(uri = "Photo 2", estateId = estateIdSaved)
        )
        val estate = FakeDataTest.getFakeEstate(
            estateId = 0L, photos = listOf(
                FakeDataTest.getFakePhoto(uri = "Photo 1", estateId = 0L),
                FakeDataTest.getFakePhoto(uri = "Photo 2", estateId = 0L),
            )
        )

        coEvery { mockEstateDao.insertEstate(any()) } returns estateIdSaved
        coEvery { mockPhotoDao.upsertPhotos(any()) } just Runs
        coEvery { mockPhotoDao.deletePhotos(any(), any()) } just Runs
        every { mockWorkManager.scheduleEstateNotification() } just Runs

        Dispatchers.setMain(testDispatcher)
        try {
            estateRepository.upsertEstate(estate, null)

            coVerifyOrder {
                mockEstateDao.insertEstate(expectedEstateEntity)
                mockPhotoDao.upsertPhotos(expectedPhotoEntities)
            }
            coVerify(exactly = 0) { mockPhotoDao.deletePhotos(any(), any()) }
            verify { mockWorkManager.scheduleEstateNotification() }
            confirmVerified(mockEstateDao, mockPhotoDao, mockWorkManager)
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun upsertEstateWithExistentEstate() = runTest(testDispatcher) {
        val estateId = 1L
        val estate = FakeDataTest.getFakeEstate(
            estateId = estateId, photos = listOf(
                FakeDataTest.getFakePhoto(uri = "Photo 1", estateId = estateId),
                FakeDataTest.getFakePhoto(uri = "Photo 2", estateId = estateId),
            )
        )
        val expectedEstateEntity =
            FakeDataTest.getFakeEstateEntity(estateId = estateId)
        val expectedPhotoEntities = listOf(
            FakeDataTest.getFakePhotoEntity(uri = "Photo 1", estateId = estateId),
            FakeDataTest.getFakePhotoEntity(uri = "Photo 2", estateId = estateId)
        )

        coEvery { mockEstateDao.updateEstate(any()) } just Runs
        coEvery { mockPhotoDao.upsertPhotos(any()) } just Runs
        coEvery { mockPhotoDao.deletePhotos(any(), any()) } just Runs
        every { mockWorkManager.scheduleEstateNotification() } just Runs

        Dispatchers.setMain(testDispatcher)
        try {
            estateRepository.upsertEstate(estate, null)

            coVerifyOrder {
                mockEstateDao.updateEstate(expectedEstateEntity)
                mockPhotoDao.upsertPhotos(expectedPhotoEntities)
            }
            coVerify(exactly = 0) { mockPhotoDao.deletePhotos(any(), any()) }
            verify(exactly = 0) { mockWorkManager.scheduleEstateNotification() }
            confirmVerified(mockEstateDao, mockPhotoDao, mockWorkManager)
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun upsertEstateWithPhotoToBeRemoved() = runTest(testDispatcher) {

        val estateId = 1L
        val estate = FakeDataTest.getFakeEstate(
            estateId = estateId, photos = listOf(
                FakeDataTest.getFakePhoto(uri = "Photo 1", estateId = estateId),
                FakeDataTest.getFakePhoto(uri = "Photo 2", estateId = estateId),
            )
        )
        val expectedEstateEntity =
            FakeDataTest.getFakeEstateEntity(estateId = estateId)
        val expectedPhotoEntities = listOf(
            FakeDataTest.getFakePhotoEntity(uri = "Photo 1", estateId = estateId),
            FakeDataTest.getFakePhotoEntity(uri = "Photo 2", estateId = estateId)
        )
        val photoToBeRemoved = listOf("photo uri 1", "photo uri 2")

        coEvery { mockEstateDao.updateEstate(any()) } just Runs
        coEvery { mockPhotoDao.upsertPhotos(any()) } just Runs
        coEvery { mockPhotoDao.deletePhotos(any(), any()) } just Runs
        every { mockWorkManager.scheduleEstateNotification() } just Runs

        Dispatchers.setMain(testDispatcher)
        try {
            estateRepository.upsertEstate(estate, photoToBeRemoved)

            coVerifyOrder {
                mockEstateDao.updateEstate(expectedEstateEntity)
                mockPhotoDao.upsertPhotos(expectedPhotoEntities)
            }
            coVerify(exactly = 1) { mockPhotoDao.deletePhotos(photoToBeRemoved, estateId) }
            verify(exactly = 0) { mockWorkManager.scheduleEstateNotification() }
            confirmVerified(mockEstateDao, mockPhotoDao, mockWorkManager)
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun getEstate() = runTest(testDispatcher) {
        val estateId = 1L
        val estateWithPhotosEntity = FakeDataTest.getFakeEstateWithPhotosEntity(
            estateId = estateId, photos = listOf(
                FakeDataTest.getFakePhotoEntity("Photos 1", estateId)
            )
        )
        val expectedEstate = FakeDataTest.getFakeEstate(
            estateId = estateId, photos = listOf(
                FakeDataTest.getFakePhoto("Photos 1", estateId)
            )
        )

        coEvery { mockEstateDao.getEstateWithPhotos(estateId) } returns flowOf(
            estateWithPhotosEntity
        )

        Dispatchers.setMain(testDispatcher)
        try {
            val estate = estateRepository.getEstate(estateId).first()

            assertEquals(expectedEstate, estate)
            verify(exactly = 1) { mockEstateDao.getEstateWithPhotos(estateId) }
            confirmVerified(mockEstateDao)
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun getEstates() = runTest(testDispatcher) {

        val estateWithPhotosEntities = listOf(
            FakeDataTest.getFakeEstateWithPhotosEntity(estateId = 1L),
            FakeDataTest.getFakeEstateWithPhotosEntity(
                estateId = 2L, photos = listOf(
                    FakeDataTest.getFakePhotoEntity("Photo 1", 2L)
                )
            ),
            FakeDataTest.getFakeEstateWithPhotosEntity(estateId = 3L),
        )
        val expectedEstates = listOf(
            FakeDataTest.getFakeEstate(estateId = 1L),
            FakeDataTest.getFakeEstate(
                estateId = 2L, photos = listOf(
                    FakeDataTest.getFakePhoto("Photo 1", 2L)
                )
            ),
            FakeDataTest.getFakeEstate(estateId = 3L),
        )

        coEvery { mockEstateDao.getEstatesWithPhotos() } returns flowOf(estateWithPhotosEntities)

        Dispatchers.setMain(testDispatcher)
        try {
            val estates = estateRepository.getEstates().first()

            assertEquals(expectedEstates, estates)
            verify(exactly = 1) { mockEstateDao.getEstatesWithPhotos() }
            confirmVerified(mockEstateDao)
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun getEstateByIdWithCursor() {

        val estateId = 1L
        val mockCursor: Cursor = mockk()

        coEvery { mockEstateDao.getEstateByIdWithCursor(estateId) } returns mockCursor

        val cursor = estateRepository.getEstateByIdWithCursor(estateId)

        assertEquals(mockCursor, cursor)
    }

    @Test
    fun getEstatesFiltered() = runTest {
        val filters = EstateFilter(
            type = listOf(PropertyType.HOUSE),
        )
        val estateWithPhotosEntities = listOf(
            FakeDataTest.getFakeEstateWithPhotosEntity(1L),
            FakeDataTest.getFakeEstateWithPhotosEntity(2L),
            FakeDataTest.getFakeEstateWithPhotosEntity(3L),
        )
        val expectedEstates = listOf(
            FakeDataTest.getFakeEstate(1L),
            FakeDataTest.getFakeEstate(2L),
            FakeDataTest.getFakeEstate(3L),
        )

        coEvery {
            mockEstateDao.getFilteredEstatesWithPhotos(
                filters.type,
                filters.minPrice,
                filters.maxPrice,
                filters.city,
                filters.available
            )
        } returns flowOf(estateWithPhotosEntities)

        Dispatchers.setMain(testDispatcher)
        try {
            val estates = estateRepository.getEstatesFiltered(filters).first()

            assertEquals(expectedEstates, estates)
            verify(exactly = 1) {
                mockEstateDao.getFilteredEstatesWithPhotos(
                    filters.type,
                    filters.minPrice,
                    filters.maxPrice,
                    filters.city,
                    filters.available
                )
            }
            confirmVerified(mockEstateDao)
        } finally {
            Dispatchers.resetMain()
        }
    }

}