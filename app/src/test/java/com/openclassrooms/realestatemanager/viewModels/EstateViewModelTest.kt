package com.openclassrooms.realestatemanager.viewModels

import com.openclassrooms.realestatemanager.ui.estate.EstateViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test

class EstateViewModelTest {

    private lateinit var estateViewModel: EstateViewModel

    @Before
    fun setUp() {
        estateViewModel = EstateViewModel()
    }

    @Test
    fun onSelectedEstate() {
        val estateId = 5L

        assertNotEquals(estateId, estateViewModel.getSelectedEstate())
        estateViewModel.onSelectedEstate(estateId)
        assertEquals(estateId, estateViewModel.getSelectedEstate())
    }

    @Test
    fun clearSelection() {
        val estateId = 5L
        val expectedValue = -1L

        // Set selectedEstate value
        estateViewModel.onSelectedEstate(estateId)
        assertEquals(estateId, estateViewModel.getSelectedEstate())

        estateViewModel.clearSelection()
        assertEquals(expectedValue, estateViewModel.getSelectedEstate())
    }
}