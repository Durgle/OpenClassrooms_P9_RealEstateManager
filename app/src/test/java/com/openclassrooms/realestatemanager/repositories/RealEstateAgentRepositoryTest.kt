package com.openclassrooms.realestatemanager.repositories

import com.openclassrooms.realestatemanager.data.database.dao.RealEstateAgentDao
import com.openclassrooms.realestatemanager.data.repositories.RealEstateAgentRepository
import com.openclassrooms.realestatemanager.utils.FakeDataTest
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RealEstateAgentRepositoryTest {

    private lateinit var mockDao: RealEstateAgentDao
    private lateinit var realEstateAgentRepository: RealEstateAgentRepository

    @Before
    fun setUp() {
        mockDao = mockk()
        realEstateAgentRepository = RealEstateAgentRepository(mockDao)
    }

    @Test
    fun getRealEstateAgents() =
        runTest {
            val agentList = FakeDataTest.getFakeRealEstateAgentEntities()
            val expectedAgents = FakeDataTest.getFakeRealEstateAgents()

            coEvery { mockDao.getRealEstateAgents() } returns flowOf(agentList)

            val agents = realEstateAgentRepository.getRealEstateAgents().first()

            assertEquals(expectedAgents, agents)
        }

}
