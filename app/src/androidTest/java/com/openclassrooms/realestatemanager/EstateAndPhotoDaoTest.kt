package com.openclassrooms.realestatemanager

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.openclassrooms.realestatemanager.data.database.RealEstateManagerDatabase
import com.openclassrooms.realestatemanager.data.database.dao.EstateDao
import com.openclassrooms.realestatemanager.data.database.dao.PhotoDao
import com.openclassrooms.realestatemanager.data.database.dao.RealEstateAgentDao
import com.openclassrooms.realestatemanager.data.database.entities.RealEstateAgentEntity
import com.openclassrooms.realestatemanager.data.enums.PropertyType
import com.openclassrooms.realestatemanager.utils.TestUtils
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class EstateAndPhotoDaoTest {

    private lateinit var estateDao: EstateDao
    private lateinit var photoDao: PhotoDao
    private lateinit var realEstateAgentDao: RealEstateAgentDao
    private lateinit var db: RealEstateManagerDatabase
    private lateinit var fakeAgent: RealEstateAgentEntity

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, RealEstateManagerDatabase::class.java
        ).build()
        estateDao = db.estateDao()
        photoDao = db.photoDao()
        realEstateAgentDao = db.realEstateAgentDao()
        fakeAgent = TestUtils.getFakeRealEstateAgentEntity(1)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeEstatesAndReadInList() = runTest {
        realEstateAgentDao.insertRealEstateAgent(fakeAgent)

        val expectedEstate1 =
            TestUtils.getFakeEstateEntity(1, PropertyType.HOUSE, 15000, fakeAgent.id)
        val expectedEstate2 =
            TestUtils.getFakeEstateEntity(2, PropertyType.LOFT, 15000, fakeAgent.id)
        estateDao.insertEstate(expectedEstate1)
        estateDao.insertEstate(expectedEstate2)

        val estates = estateDao.getAllEstates().first()

        assertEquals(2, estates.size)
        assertEquals(estates[0], expectedEstate1)
        assertEquals(estates[1], expectedEstate2)

    }

    @Test
    @Throws(Exception::class)
    fun writeReadAndUpdateEstate() = runTest {
        realEstateAgentDao.insertRealEstateAgent(fakeAgent)

        val newEstate =
            TestUtils.getFakeEstateEntity(2, PropertyType.HOUSE, 15000, fakeAgent.id)
        val updateEstate =
            TestUtils.getFakeEstateEntity(2, PropertyType.LOFT, 75000, fakeAgent.id)

        val estateIdSaved = estateDao.insertEstate(newEstate)
        val estateSaved = estateDao.getEstateById(newEstate.id).first()
        assertEquals(newEstate, estateSaved)
        assertEquals(2, estateIdSaved)

        estateDao.updateEstate(updateEstate)
        val estates = estateDao.getAllEstates().first()
        assertEquals(1, estates.size)
        assertEquals(updateEstate, estates[0])
    }

    @Test
    @Throws(Exception::class)
    fun writeEstateAndReadWithCursor() = runTest {
        realEstateAgentDao.insertRealEstateAgent(fakeAgent)

        val expectedEstate =
            TestUtils.getFakeEstateEntity(2, PropertyType.HOUSE, 15000, fakeAgent.id)
        val estateIdSaved = estateDao.insertEstate(expectedEstate)
        val cursor = estateDao.getEstateByIdWithCursor(estateIdSaved)

        assertNotNull(cursor)
        assertTrue(cursor.moveToFirst())
        val id = cursor.getLong(cursor.getColumnIndex("id"))
        val type = PropertyType.valueOf(cursor.getString(cursor.getColumnIndex("type")))
        val price = cursor.getLong(cursor.getColumnIndex("price"))
        val agentId = cursor.getLong(cursor.getColumnIndex("realEstateAgentId"))

        assertEquals(expectedEstate.id, id)
        assertEquals(expectedEstate.type, type)
        assertEquals(expectedEstate.price, price)
        assertEquals(expectedEstate.realEstateAgentId, agentId)
    }

    @Test
    @Throws(Exception::class)
    fun writePhotosAndReadInList() = runTest {
        realEstateAgentDao.insertRealEstateAgent(fakeAgent)

        val estate =
            TestUtils.getFakeEstateEntity(1, PropertyType.HOUSE, 15000, fakeAgent.id)
        val estateIdSaved = estateDao.insertEstate(estate)
        val expectedPhoto1 = TestUtils.getFakePhotoEntity("uri/photo1.png", estateIdSaved)
        val expectedPhoto2 = TestUtils.getFakePhotoEntity("uri/photo2.png", estateIdSaved)
        photoDao.insertPhotos(listOf(expectedPhoto1, expectedPhoto2))

        val photos = photoDao.getAllPhotos().first()

        assertEquals(2, photos.size)
        assertEquals(photos[0], expectedPhoto1)
        assertEquals(photos[1], expectedPhoto2)
    }

    @Test
    @Throws(Exception::class)
    fun upsertPhoto() = runTest {
        realEstateAgentDao.insertRealEstateAgent(fakeAgent)

        val estate =
            TestUtils.getFakeEstateEntity(1, PropertyType.HOUSE, 15000, fakeAgent.id)
        val estateIdSaved = estateDao.insertEstate(estate)
        val expectedPhoto1 = TestUtils.getFakePhotoEntity("uri/photo1.png", estateIdSaved)
        val expectedPhoto2 = TestUtils.getFakePhotoEntity("uri/photo2.png", estateIdSaved)
        val expectedPhoto2Update =
            TestUtils.getFakePhotoEntity("uri/photo2.png", estateIdSaved, "New description")
        val expectedPhoto3 = TestUtils.getFakePhotoEntity("uri/photo3.png", estateIdSaved)

        photoDao.upsertPhotos(listOf(expectedPhoto1, expectedPhoto2))
        val photosFirstList = photoDao.getAllPhotos().first()
        assertEquals(2, photosFirstList.size)
        assertEquals(listOf(expectedPhoto1, expectedPhoto2), photosFirstList)

        photoDao.upsertPhotos(listOf(expectedPhoto2Update, expectedPhoto3))
        val photosSecondList = photoDao.getAllPhotos().first()
        assertEquals(3, photosSecondList.size)
        assertEquals(listOf(expectedPhoto1, expectedPhoto2Update, expectedPhoto3), photosSecondList)
    }

    @Test
    @Throws(Exception::class)
    fun deletePhoto() = runTest {
        realEstateAgentDao.insertRealEstateAgent(fakeAgent)

        val estate =
            TestUtils.getFakeEstateEntity(1, PropertyType.HOUSE, 15000, fakeAgent.id)
        val estateIdSaved = estateDao.insertEstate(estate)
        val photo1 = TestUtils.getFakePhotoEntity("uri/photo1.png", estateIdSaved)
        val photo2 = TestUtils.getFakePhotoEntity("uri/photo2.png", estateIdSaved)
        val photo3 = TestUtils.getFakePhotoEntity("uri/photo3.png", estateIdSaved)
        val photo4 = TestUtils.getFakePhotoEntity("uri/photo4.png", estateIdSaved)
        photoDao.insertPhotos(listOf(photo1, photo2, photo3, photo4))

        photoDao.deletePhotos(listOf("uri/photo2.png", "uri/photo3.png"), estateIdSaved)

        val photos = photoDao.getAllPhotos().first()
        assertEquals(2, photos.size)
        assertEquals(listOf(photo1, photo4), photos)
    }

    @Test
    @Throws(Exception::class)
    fun readEstateWithPhotos() = runTest {
        realEstateAgentDao.insertRealEstateAgent(fakeAgent)
        val estate =
            TestUtils.getFakeEstateEntity(1, PropertyType.HOUSE, 15000, fakeAgent.id)
        val estateIdSaved = estateDao.insertEstate(estate)
        val photo1 = TestUtils.getFakePhotoEntity("uri/photo1.png", estateIdSaved)
        val photo2 = TestUtils.getFakePhotoEntity("uri/photo2.png", estateIdSaved)
        photoDao.insertPhotos(listOf(photo1, photo2))
        val expectedList = TestUtils.getFakeEstateWithPhotosEntity(
            estate.id,
            estate.type,
            estate.price,
            listOf(photo1, photo2),
            fakeAgent
        )

        val estates = estateDao.getEstatesWithPhotos().first()
        val estateFound = estateDao.getEstateWithPhotos(estateIdSaved).first()

        assertEquals(1, estates.size)
        assertEquals(listOf(expectedList), estates)
        assertEquals(expectedList, estateFound)
    }

    @Test
    @Throws(Exception::class)
    fun readEstateWithPhotosFiltered() = runTest {
        realEstateAgentDao.insertRealEstateAgent(fakeAgent)
        val estate1 =
            TestUtils.getFakeEstateEntity(
                1,
                PropertyType.HOUSE,
                10000,
                fakeAgent.id,
                false,
                "New York"
            )
        val estate2 =
            TestUtils.getFakeEstateEntity(
                2,
                PropertyType.LOFT,
                20000,
                fakeAgent.id,
                true,
                "New York"
            )
        val estate3 =
            TestUtils.getFakeEstateEntity(
                3,
                PropertyType.APARTMENT,
                30000,
                fakeAgent.id,
                true,
                "Paris"
            )
        val estate4 =
            TestUtils.getFakeEstateEntity(
                4,
                PropertyType.MANOR,
                40000,
                fakeAgent.id,
                false,
                "London"
            )
        val estate5 =
            TestUtils.getFakeEstateEntity(5, PropertyType.HOUSE, 50000, fakeAgent.id, true, "Tokyo")
        val photo1 = TestUtils.getFakePhotoList(1)
        val photo2 = TestUtils.getFakePhotoList(2)
        val photo3 = TestUtils.getFakePhotoList(3)
        val photo4 = TestUtils.getFakePhotoList(4)
        val photo5 = TestUtils.getFakePhotoList(5)
        estateDao.insertEstate(estate1)
        estateDao.insertEstate(estate2)
        estateDao.insertEstate(estate3)
        estateDao.insertEstate(estate4)
        estateDao.insertEstate(estate5)
        photoDao.insertPhotos(photo1)
        photoDao.insertPhotos(photo2)
        photoDao.insertPhotos(photo3)
        photoDao.insertPhotos(photo4)
        photoDao.insertPhotos(photo5)
        val estateWithPhoto1 = TestUtils.getFakeEstateWithPhotosEntity(estate1, photo1, fakeAgent)
        val estateWithPhoto2 = TestUtils.getFakeEstateWithPhotosEntity(estate2, photo2, fakeAgent)
        val estateWithPhoto3 = TestUtils.getFakeEstateWithPhotosEntity(estate3, photo3, fakeAgent)
        val estateWithPhoto4 = TestUtils.getFakeEstateWithPhotosEntity(estate4, photo4, fakeAgent)
        val estateWithPhoto5 = TestUtils.getFakeEstateWithPhotosEntity(estate5, photo5, fakeAgent)

        val filterMinPriceList = estateDao.getFilteredEstatesWithPhotos(
            15000,
            null,
            null,
            null
        ).first()
        assertEquals(4, filterMinPriceList.size)
        assertEquals(
            listOf(estateWithPhoto2, estateWithPhoto3, estateWithPhoto4, estateWithPhoto5),
            filterMinPriceList
        )

        val filterMaxPriceList = estateDao.getFilteredEstatesWithPhotos(
            null,
            35000,
            null,
            null
        ).first()
        assertEquals(3, filterMaxPriceList.size)
        assertEquals(
            listOf(estateWithPhoto1, estateWithPhoto2, estateWithPhoto3),
            filterMaxPriceList
        )

        val filterCityList = estateDao.getFilteredEstatesWithPhotos(
            null,
            null,
            "New York",
            null
        ).first()
        assertEquals(2, filterCityList.size)
        assertEquals(listOf(estateWithPhoto1, estateWithPhoto2), filterCityList)

        val filterAvailableList = estateDao.getFilteredEstatesWithPhotos(
            null,
            null,
            null,
            false
        ).first()
        assertEquals(2, filterAvailableList.size)
        assertEquals(listOf(estateWithPhoto1, estateWithPhoto4), filterAvailableList)

        val filterTypeList = estateDao.getFilteredEstatesWithPhotos(
            listOf(PropertyType.HOUSE, PropertyType.LOFT),
            null,
            null,
            null,
            null
        ).first()
        assertEquals(3, filterTypeList.size)
        assertEquals(listOf(estateWithPhoto1, estateWithPhoto2, estateWithPhoto5), filterTypeList)

        val filterCombinedList = estateDao.getFilteredEstatesWithPhotos(
            listOf(PropertyType.HOUSE, PropertyType.LOFT),
            25000,
            null,
            null,
            true
        ).first()
        assertEquals(1, filterCombinedList.size)
        assertEquals(listOf(estateWithPhoto5), filterCombinedList)
    }

}