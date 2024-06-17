package com.openclassrooms.realestatemanager

import android.content.res.Resources
import com.openclassrooms.realestatemanager.data.enums.PointOfInterest
import com.openclassrooms.realestatemanager.utils.Utils
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

class UtilsUnitTest {

    @Test
    fun testConvertEuroToDollar() {
        val euros = 100
        val expectedDollars = 123

        assertEquals(expectedDollars, Utils.convertEuroToDollar(euros))
    }

    @Test
    fun testConvertDollarToEuro() {
        val dollars = 123
        val expectedEuros = 100

        assertEquals(expectedEuros, Utils.convertDollarToEuro(dollars))
    }

    @Test
    fun testGetTodayDate() {
        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val expectedDate = dateFormat.format(Date())

        assertEquals(expectedDate, Utils.getTodayDate())
    }

    @Test
    fun testFormatPrice() {
        val price: Long = 100000
        val expectedPrice = "$100,000"

        assertEquals(expectedPrice, Utils.formatPrice(price))
    }

    @Test
    fun testFormatPropertyArea() {
        val propertyArea: Long = 250
        val expectedPropertyArea = "250 sq m"

        assertEquals(expectedPropertyArea, Utils.formatPropertyArea(propertyArea))
    }

    @Test
    fun testFormatEstateAddress() {
        val address = "4369 Columbia Mine Road"
        val additionalAddress = "Apt 101"
        val city = "Newton"
        val zipCode = "25266"
        val country = "United States"
        val expectedAddress = "4369 Columbia Mine Road" + System.lineSeparator() +
                "Apt 101" + System.lineSeparator() + "Newton" + System.lineSeparator() +
                "25266" + System.lineSeparator() + "United States"

        assertEquals(
            expectedAddress,
            Utils.formatEstateAddress(address, additionalAddress, city, zipCode, country)
        )
    }

    @Test
    fun testFormatPointOfInterests() {
        val pointOfInterests: MutableList<PointOfInterest> = ArrayList()
        pointOfInterests.add(PointOfInterest.COMMUNITY_SPACES)
        pointOfInterests.add(PointOfInterest.RESTAURANTS_AND_CAFES)
        val labelResId1 = PointOfInterest.COMMUNITY_SPACES.labelResId
        val labelResId2 = PointOfInterest.RESTAURANTS_AND_CAFES.labelResId
        val mockResources = mockk<Resources>()
        val expected = "- Community Spaces" + System.lineSeparator() + "- Restaurants and Coffee"

        every { mockResources.getString(labelResId1) } returns "Community Spaces"
        every { mockResources.getString(labelResId2) } returns "Restaurants and Coffee"

        assertEquals(
            expected,
            Utils.formatPointOfInterests(mockResources, pointOfInterests)
        )
    }

    @Test
    fun testParsePointOfInterests() {
        val data = "COMMUNITY_SPACES,RESTAURANTS_AND_CAFES"
        val expectedPointOfSales: MutableList<PointOfInterest> = ArrayList()
        expectedPointOfSales.add(PointOfInterest.COMMUNITY_SPACES)
        expectedPointOfSales.add(PointOfInterest.RESTAURANTS_AND_CAFES)

        assertEquals(expectedPointOfSales, Utils.parsePointOfInterests(data))
    }
}