package com.openclassrooms.realestatemanager

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.openclassrooms.realestatemanager.data.database.dao.RealEstateAgentDao
import com.openclassrooms.realestatemanager.database.TestDatabase
import com.openclassrooms.realestatemanager.utils.TestUtils
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RealEstateAgentDaoTest {

    private lateinit var realEstateAgentDao: RealEstateAgentDao
    private lateinit var db: TestDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, TestDatabase::class.java
        ).build()
        realEstateAgentDao = db.realEstateAgentDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeAgentAndReadInList() = runTest {
        val expectedAgent1 = TestUtils.getFakeRealEstateAgentEntity(1)
        val expectedAgent2 = TestUtils.getFakeRealEstateAgentEntity(2)

        realEstateAgentDao.insertRealEstateAgent(expectedAgent1)
        realEstateAgentDao.insertRealEstateAgent(expectedAgent2)

        val agents = realEstateAgentDao.getRealEstateAgents().first()

        assertEquals(2, agents.size)
        assertEquals(expectedAgent1, agents[0])
        assertEquals(expectedAgent2, agents[1])
    }

}