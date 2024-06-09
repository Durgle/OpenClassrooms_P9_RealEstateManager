package com.openclassrooms.realestatemanager.viewModels

import com.openclassrooms.realestatemanager.data.repositories.EstateRepository
import com.openclassrooms.realestatemanager.ui.estate.EstateViewModel
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class EstateViewModelTest {

    private lateinit var estateViewModel: EstateViewModel
    private lateinit var mockEstateRepository: EstateRepository
    private val selectedId = 5L
    private val newSelectedId = 8L

    @Before
    fun setUp() {
        mockEstateRepository = mockk()
        every { mockEstateRepository.getSelectedEstateId() } returns selectedId
        every { mockEstateRepository.clearSelectedEstate() } just Runs
        every { mockEstateRepository.onSelectedEstate(newSelectedId) } just Runs
        estateViewModel = EstateViewModel(mockEstateRepository)
    }

    @Test
    fun onSelectedEstate() {

        assertEquals(selectedId, estateViewModel.getSelectedEstateId())
        estateViewModel.onSelectedEstate(newSelectedId)
        verify { mockEstateRepository.onSelectedEstate(newSelectedId) }
    }

    @Test
    fun getSelectedEstateId() {
        assertEquals(selectedId, estateViewModel.getSelectedEstateId())
    }

    @Test
    fun clearSelection() {

        estateViewModel.clearSelection()
        verify { mockEstateRepository.clearSelectedEstate() }
    }
}