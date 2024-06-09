package com.openclassrooms.realestatemanager.viewModels

import android.content.res.Resources
import android.location.Address
import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.openclassrooms.realestatemanager.data.enums.PointOfInterest
import com.openclassrooms.realestatemanager.data.enums.PropertyType
import com.openclassrooms.realestatemanager.data.models.Estate
import com.openclassrooms.realestatemanager.data.repositories.EstateRepository
import com.openclassrooms.realestatemanager.data.repositories.GeocoderRepository
import com.openclassrooms.realestatemanager.data.repositories.RealEstateAgentRepository
import com.openclassrooms.realestatemanager.ui.estate.upsert.CurrentDataViewState
import com.openclassrooms.realestatemanager.ui.estate.upsert.ErrorViewState
import com.openclassrooms.realestatemanager.ui.estate.upsert.PhotoViewState
import com.openclassrooms.realestatemanager.ui.estate.upsert.UpsertEstateValidation
import com.openclassrooms.realestatemanager.ui.estate.upsert.UpsertEstateViewModel
import com.openclassrooms.realestatemanager.ui.estate.upsert.UpsertEstateViewState
import com.openclassrooms.realestatemanager.utils.FakeDataTest
import com.openclassrooms.realestatemanager.utils.LiveDataTestUtil
import com.openclassrooms.realestatemanager.utils.Utils
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
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
class UpsertEstateViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val mockEstateRepository: EstateRepository = mockk()
    private val mockRealEstateAgentRepository: RealEstateAgentRepository = mockk()
    private val mockGeocoderRepository: GeocoderRepository = mockk()
    private val mockResources: Resources = mockk()
    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = UnconfinedTestDispatcher(testScheduler)

    private val estateId = 5L
    private val newEstateId = 0L
    private val latitude = 1.56522
    private val longitude = 45.6545687
    private val fakeEstate = FakeDataTest.getFakeEstate(estateId = estateId)
    private val newFakeEstate = FakeDataTest.getFakeEstate(estateId = newEstateId)
    private val fakeAgents = listOf(
        FakeDataTest.getFakeRealEstateAgent(1L),
        FakeDataTest.getFakeRealEstateAgent(2L),
        FakeDataTest.getFakeRealEstateAgent(3L),
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        val mockAddress: Address = mockk()
        every { mockEstateRepository.getEstate(estateId) } returns flowOf(fakeEstate)
        every { mockEstateRepository.getEstate(newEstateId) } returns flowOf(newFakeEstate)
        coEvery { mockEstateRepository.upsertEstate(any(), any()) } just Runs
        every { mockAddress.latitude } returns latitude
        every { mockAddress.longitude } returns longitude
        coEvery { mockGeocoderRepository.getCoordinates(any()) } returns mockAddress
        every { mockRealEstateAgentRepository.getRealEstateAgents() } returns flowOf(fakeAgents)
    }

    private fun instantiateViewModel(estateId: Long? = null): UpsertEstateViewModel {
        return UpsertEstateViewModel(
            mockEstateRepository,
            mockRealEstateAgentRepository,
            mockGeocoderRepository,
            mockResources,
            estateId,
            testDispatcher
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getRealEstateAgent() = runTest {
        val upsertEstateViewModel = instantiateViewModel()

        val realEstateAgentLiveData = upsertEstateViewModel.getRealEstateAgent()
        val realEstateAgent = LiveDataTestUtil.getOrAwaitValue(realEstateAgentLiveData)

        assertEquals(fakeAgents, realEstateAgent)
    }

    @Test
    fun getViewStateNewEstate() = runTest {
        val upsertEstateViewModel = instantiateViewModel()
        val expectedViewState = UpsertEstateViewState()

        val viewStateLiveData = upsertEstateViewModel.getViewState()
        val viewState = LiveDataTestUtil.getOrAwaitValue(viewStateLiveData)

        assertEquals(expectedViewState, viewState)
    }

    @Test
    fun getViewStateExistingEstate() = runTest {
        val upsertEstateViewModel = instantiateViewModel(estateId)
        val expectedViewState = FakeDataTest.getFakeUpsertViewState()

        val viewStateLiveData = upsertEstateViewModel.getViewState()
        val viewState = LiveDataTestUtil.getOrAwaitValue(viewStateLiveData)

        assertEquals(expectedViewState, viewState)
    }

    @Test
    fun onAgentChangedValid() = runTest {
        val upsertEstateViewModel = instantiateViewModel()
        val expectedAgent = FakeDataTest.getFakeRealEstateAgent(1L)
        val expectedViewState = UpsertEstateViewState(
            CurrentDataViewState(agent = expectedAgent),
            ErrorViewState()
        )
        mockkObject(UpsertEstateValidation)
        every {
            UpsertEstateValidation.validateAgent(
                mockResources,
                expectedAgent
            )
        } returns UpsertEstateValidation.ValidationResult.Valid

        upsertEstateViewModel.onAgentChanged(expectedAgent)
        val viewStateLiveData = upsertEstateViewModel.getViewState()
        val viewState = LiveDataTestUtil.getOrAwaitValue(viewStateLiveData)

        assertEquals(expectedViewState, viewState)
    }

    @Test
    fun onTypeChangedValid() = runTest {
        val upsertEstateViewModel = instantiateViewModel()
        val expectedPropertyType = PropertyType.DUPLEX
        val expectedViewState = UpsertEstateViewState(
            CurrentDataViewState(type = expectedPropertyType),
            ErrorViewState()
        )
        mockkObject(UpsertEstateValidation)
        every {
            UpsertEstateValidation.validateType(mockResources, expectedPropertyType)
        } returns UpsertEstateValidation.ValidationResult.Valid

        upsertEstateViewModel.onTypeChanged(expectedPropertyType)
        val viewStateLiveData = upsertEstateViewModel.getViewState()
        val viewState = LiveDataTestUtil.getOrAwaitValue(viewStateLiveData)

        assertEquals(expectedViewState, viewState)
    }

    @Test
    fun onPriceChangedValid() = runTest {
        val upsertEstateViewModel = instantiateViewModel()
        val expectedPrice = "1500"
        val expectedViewState = UpsertEstateViewState(
            CurrentDataViewState(price = expectedPrice),
            ErrorViewState()
        )
        mockkObject(UpsertEstateValidation)
        every {
            UpsertEstateValidation.validatePrice(mockResources, expectedPrice)
        } returns UpsertEstateValidation.ValidationResult.Valid

        upsertEstateViewModel.onPriceChanged(expectedPrice)
        val viewStateLiveData = upsertEstateViewModel.getViewState()
        val viewState = LiveDataTestUtil.getOrAwaitValue(viewStateLiveData)

        assertEquals(expectedViewState, viewState)
    }

    @Test
    fun onPriceChangedInvalid() = runTest {
        val upsertEstateViewModel = instantiateViewModel()
        val expectedPrice = "price"
        val errorMessage = "Wrong data"
        val expectedViewState = UpsertEstateViewState(
            CurrentDataViewState(price = expectedPrice),
            ErrorViewState(errorPrice = errorMessage)
        )
        mockkObject(UpsertEstateValidation)
        every {
            UpsertEstateValidation.validatePrice(mockResources, expectedPrice)
        } returns UpsertEstateValidation.ValidationResult.Invalid(errorMessage)

        upsertEstateViewModel.onPriceChanged(expectedPrice)
        val viewStateLiveData = upsertEstateViewModel.getViewState()
        val viewState = LiveDataTestUtil.getOrAwaitValue(viewStateLiveData)

        assertEquals(expectedViewState, viewState)
    }

    @Test
    fun onSurfaceChangedValid() = runTest {
        val upsertEstateViewModel = instantiateViewModel()
        val expectedSurface = "170"
        val expectedViewState = UpsertEstateViewState(
            CurrentDataViewState(surface = expectedSurface),
            ErrorViewState()
        )
        mockkObject(UpsertEstateValidation)
        every {
            UpsertEstateValidation.validateSurface(mockResources, expectedSurface)
        } returns UpsertEstateValidation.ValidationResult.Valid

        upsertEstateViewModel.onSurfaceChanged(expectedSurface)
        val viewStateLiveData = upsertEstateViewModel.getViewState()
        val viewState = LiveDataTestUtil.getOrAwaitValue(viewStateLiveData)

        assertEquals(expectedViewState, viewState)
    }

    @Test
    fun onSurfaceChangedInvalid() = runTest {
        val upsertEstateViewModel = instantiateViewModel()
        val expectedSurface = "surface"
        val errorMessage = "Wrong data"
        val expectedViewState = UpsertEstateViewState(
            CurrentDataViewState(surface = expectedSurface),
            ErrorViewState(errorSurface = errorMessage)
        )
        mockkObject(UpsertEstateValidation)
        every {
            UpsertEstateValidation.validateSurface(mockResources, expectedSurface)
        } returns UpsertEstateValidation.ValidationResult.Invalid(errorMessage)

        upsertEstateViewModel.onSurfaceChanged(expectedSurface)
        val viewStateLiveData = upsertEstateViewModel.getViewState()
        val viewState = LiveDataTestUtil.getOrAwaitValue(viewStateLiveData)

        assertEquals(expectedViewState, viewState)
    }

    @Test
    fun onNumberOfBedroomChangedValid() = runTest {
        val upsertEstateViewModel = instantiateViewModel()
        val expectedNumberOfBedroom = "2"
        val expectedViewState = UpsertEstateViewState(
            CurrentDataViewState(numberOfBedroom = expectedNumberOfBedroom),
            ErrorViewState()
        )
        mockkObject(UpsertEstateValidation)
        every {
            UpsertEstateValidation.validateNbOfBedroom(mockResources, expectedNumberOfBedroom)
        } returns UpsertEstateValidation.ValidationResult.Valid

        upsertEstateViewModel.onNumberOfBedroomChanged(expectedNumberOfBedroom)
        val viewStateLiveData = upsertEstateViewModel.getViewState()
        val viewState = LiveDataTestUtil.getOrAwaitValue(viewStateLiveData)

        assertEquals(expectedViewState, viewState)
    }

    @Test
    fun onNumberOfBedroomChangedInvalid() = runTest {
        val upsertEstateViewModel = instantiateViewModel()
        val expectedNumberOfBedroom = "bed"
        val errorMessage = "Wrong data"
        val expectedViewState = UpsertEstateViewState(
            CurrentDataViewState(numberOfBedroom = expectedNumberOfBedroom),
            ErrorViewState(errorNumberOfBedroom = errorMessage)
        )
        mockkObject(UpsertEstateValidation)
        every {
            UpsertEstateValidation.validateNbOfBedroom(mockResources, expectedNumberOfBedroom)
        } returns UpsertEstateValidation.ValidationResult.Invalid(errorMessage)

        upsertEstateViewModel.onNumberOfBedroomChanged(expectedNumberOfBedroom)
        val viewStateLiveData = upsertEstateViewModel.getViewState()
        val viewState = LiveDataTestUtil.getOrAwaitValue(viewStateLiveData)

        assertEquals(expectedViewState, viewState)
    }

    @Test
    fun onNumberOfBathroomChangedValid() = runTest {
        val upsertEstateViewModel = instantiateViewModel()
        val expectedNumberOfBathroom = "5"
        val expectedViewState = UpsertEstateViewState(
            CurrentDataViewState(numberOfBathroom = expectedNumberOfBathroom),
            ErrorViewState()
        )
        mockkObject(UpsertEstateValidation)
        every {
            UpsertEstateValidation.validateNbOfBathroom(mockResources, expectedNumberOfBathroom)
        } returns UpsertEstateValidation.ValidationResult.Valid

        upsertEstateViewModel.onNumberOfBathroomChanged(expectedNumberOfBathroom)
        val viewStateLiveData = upsertEstateViewModel.getViewState()
        val viewState = LiveDataTestUtil.getOrAwaitValue(viewStateLiveData)

        assertEquals(expectedViewState, viewState)
    }

    @Test
    fun onNumberOfBathroomChangedInvalid() = runTest {
        val upsertEstateViewModel = instantiateViewModel()
        val expectedNumberOfBathroom = "bath"
        val errorMessage = "Wrong data"
        val expectedViewState = UpsertEstateViewState(
            CurrentDataViewState(numberOfBathroom = expectedNumberOfBathroom),
            ErrorViewState(errorNumberOfBathroom = errorMessage)
        )
        mockkObject(UpsertEstateValidation)
        every {
            UpsertEstateValidation.validateNbOfBathroom(mockResources, expectedNumberOfBathroom)
        } returns UpsertEstateValidation.ValidationResult.Invalid(errorMessage)

        upsertEstateViewModel.onNumberOfBathroomChanged(expectedNumberOfBathroom)
        val viewStateLiveData = upsertEstateViewModel.getViewState()
        val viewState = LiveDataTestUtil.getOrAwaitValue(viewStateLiveData)

        assertEquals(expectedViewState, viewState)
    }

    @Test
    fun onDescriptionChangedValid() = runTest {
        val upsertEstateViewModel = instantiateViewModel()
        val expectedDescription = "A high house"
        val expectedViewState = UpsertEstateViewState(
            CurrentDataViewState(description = expectedDescription),
            ErrorViewState()
        )

        upsertEstateViewModel.onDescriptionChanged(expectedDescription)
        val viewStateLiveData = upsertEstateViewModel.getViewState()
        val viewState = LiveDataTestUtil.getOrAwaitValue(viewStateLiveData)

        assertEquals(expectedViewState, viewState)
    }

    @Test
    fun onAddressChangedValid() = runTest {
        val upsertEstateViewModel = instantiateViewModel()
        val expectedAddress = "123 Main St"
        val expectedViewState = UpsertEstateViewState(
            CurrentDataViewState(address = expectedAddress),
            ErrorViewState()
        )
        mockkObject(UpsertEstateValidation)
        every {
            UpsertEstateValidation.validateAddress(mockResources, expectedAddress)
        } returns UpsertEstateValidation.ValidationResult.Valid

        upsertEstateViewModel.onAddressChanged(expectedAddress)
        val viewStateLiveData = upsertEstateViewModel.getViewState()
        val viewState = LiveDataTestUtil.getOrAwaitValue(viewStateLiveData)

        assertEquals(expectedViewState, viewState)
    }

    @Test
    fun onAddressChangedInvalid() = runTest {
        val upsertEstateViewModel = instantiateViewModel()
        val expectedAddress = ""
        val errorMessage = "Wrong data"
        val expectedViewState = UpsertEstateViewState(
            CurrentDataViewState(address = expectedAddress),
            ErrorViewState(errorAddress = errorMessage)
        )
        mockkObject(UpsertEstateValidation)
        every {
            UpsertEstateValidation.validateAddress(mockResources, expectedAddress)
        } returns UpsertEstateValidation.ValidationResult.Invalid(errorMessage)

        upsertEstateViewModel.onAddressChanged(expectedAddress)
        val viewStateLiveData = upsertEstateViewModel.getViewState()
        val viewState = LiveDataTestUtil.getOrAwaitValue(viewStateLiveData)

        assertEquals(expectedViewState, viewState)
    }

    @Test
    fun onAdditionalAddressChangedValid() = runTest {
        val upsertEstateViewModel = instantiateViewModel()
        val expectedAdditionalAddress = "Apartment 5"
        val expectedViewState = UpsertEstateViewState(
            CurrentDataViewState(additionalAddress = expectedAdditionalAddress),
            ErrorViewState()
        )

        upsertEstateViewModel.onAdditionalAddressChanged(expectedAdditionalAddress)
        val viewStateLiveData = upsertEstateViewModel.getViewState()
        val viewState = LiveDataTestUtil.getOrAwaitValue(viewStateLiveData)

        assertEquals(expectedViewState, viewState)
    }

    @Test
    fun onZipcodeChangedValid() = runTest {
        val upsertEstateViewModel = instantiateViewModel()
        val expectedZipcode = "02463"
        val expectedViewState = UpsertEstateViewState(
            CurrentDataViewState(zipcode = expectedZipcode),
            ErrorViewState()
        )
        mockkObject(UpsertEstateValidation)
        every {
            UpsertEstateValidation.validateZipcode(mockResources, expectedZipcode)
        } returns UpsertEstateValidation.ValidationResult.Valid

        upsertEstateViewModel.onZipcodeChanged(expectedZipcode)
        val viewStateLiveData = upsertEstateViewModel.getViewState()
        val viewState = LiveDataTestUtil.getOrAwaitValue(viewStateLiveData)

        assertEquals(expectedViewState, viewState)
    }

    @Test
    fun onZipcodeChangedInvalid() = runTest {
        val upsertEstateViewModel = instantiateViewModel()
        val expectedZipcode = ""
        val errorMessage = "Wrong data"
        val expectedViewState = UpsertEstateViewState(
            CurrentDataViewState(zipcode = expectedZipcode),
            ErrorViewState(errorZipcode = errorMessage)
        )
        mockkObject(UpsertEstateValidation)
        every {
            UpsertEstateValidation.validateZipcode(mockResources, expectedZipcode)
        } returns UpsertEstateValidation.ValidationResult.Invalid(errorMessage)

        upsertEstateViewModel.onZipcodeChanged(expectedZipcode)
        val viewStateLiveData = upsertEstateViewModel.getViewState()
        val viewState = LiveDataTestUtil.getOrAwaitValue(viewStateLiveData)

        assertEquals(expectedViewState, viewState)
    }

    @Test
    fun onCityChangedValid() = runTest {
        val upsertEstateViewModel = instantiateViewModel()
        val expectedCity = "New York"
        val expectedViewState = UpsertEstateViewState(
            CurrentDataViewState(city = expectedCity),
            ErrorViewState()
        )
        mockkObject(UpsertEstateValidation)
        every {
            UpsertEstateValidation.validateCity(mockResources, expectedCity)
        } returns UpsertEstateValidation.ValidationResult.Valid

        upsertEstateViewModel.onCityChanged(expectedCity)
        val viewStateLiveData = upsertEstateViewModel.getViewState()
        val viewState = LiveDataTestUtil.getOrAwaitValue(viewStateLiveData)

        assertEquals(expectedViewState, viewState)
    }

    @Test
    fun onCityChangedInvalid() = runTest {
        val upsertEstateViewModel = instantiateViewModel()
        val expectedCity = ""
        val errorMessage = "Wrong data"
        val expectedViewState = UpsertEstateViewState(
            CurrentDataViewState(city = expectedCity),
            ErrorViewState(errorCity = errorMessage)
        )
        mockkObject(UpsertEstateValidation)
        every {
            UpsertEstateValidation.validateCity(mockResources, expectedCity)
        } returns UpsertEstateValidation.ValidationResult.Invalid(errorMessage)

        upsertEstateViewModel.onCityChanged(expectedCity)
        val viewStateLiveData = upsertEstateViewModel.getViewState()
        val viewState = LiveDataTestUtil.getOrAwaitValue(viewStateLiveData)

        assertEquals(expectedViewState, viewState)
    }

    @Test
    fun onCountryChangedValid() = runTest {
        val upsertEstateViewModel = instantiateViewModel()
        val expectedCountry = "United State"
        val expectedViewState = UpsertEstateViewState(
            CurrentDataViewState(country = expectedCountry),
            ErrorViewState()
        )
        mockkObject(UpsertEstateValidation)
        every {
            UpsertEstateValidation.validateCountry(mockResources, expectedCountry)
        } returns UpsertEstateValidation.ValidationResult.Valid

        upsertEstateViewModel.onCountryChanged(expectedCountry)
        val viewStateLiveData = upsertEstateViewModel.getViewState()
        val viewState = LiveDataTestUtil.getOrAwaitValue(viewStateLiveData)

        assertEquals(expectedViewState, viewState)
    }

    @Test
    fun onCountryChangedInvalid() = runTest {
        val upsertEstateViewModel = instantiateViewModel()
        val expectedCountry = ""
        val errorMessage = "Wrong data"
        val expectedViewState = UpsertEstateViewState(
            CurrentDataViewState(country = expectedCountry),
            ErrorViewState(errorCountry = errorMessage)
        )
        mockkObject(UpsertEstateValidation)
        every {
            UpsertEstateValidation.validateCountry(mockResources, expectedCountry)
        } returns UpsertEstateValidation.ValidationResult.Invalid(errorMessage)

        upsertEstateViewModel.onCountryChanged(expectedCountry)
        val viewStateLiveData = upsertEstateViewModel.getViewState()
        val viewState = LiveDataTestUtil.getOrAwaitValue(viewStateLiveData)

        assertEquals(expectedViewState, viewState)
    }

    @Test
    fun onAvailableChangeValid() = runTest {
        val upsertEstateViewModel = instantiateViewModel()
        val expectedAvailable = true
        val expectedViewState = UpsertEstateViewState(
            CurrentDataViewState(available = expectedAvailable),
            ErrorViewState()
        )

        upsertEstateViewModel.onAvailableChange(expectedAvailable)
        val viewStateLiveData = upsertEstateViewModel.getViewState()
        val viewState = LiveDataTestUtil.getOrAwaitValue(viewStateLiveData)

        assertEquals(expectedViewState, viewState)
    }

    @Test
    fun onPointOfInterestSelected() = runTest {
        val upsertEstateViewModel = instantiateViewModel()
        val pointOfInterest1 = PointOfInterest.HEALTH_SERVICES
        val pointOfInterest2 = PointOfInterest.PARKS_AND_GREEN_SPACES
        val expectedViewState = UpsertEstateViewState(
            CurrentDataViewState(pointsOfInterest = listOf(pointOfInterest1)),
            ErrorViewState()
        )
        val expectedViewState2 = UpsertEstateViewState(
            CurrentDataViewState(pointsOfInterest = listOf(pointOfInterest1, pointOfInterest2)),
            ErrorViewState()
        )
        val expectedViewState3 = UpsertEstateViewState(
            CurrentDataViewState(pointsOfInterest = listOf(pointOfInterest2)),
            ErrorViewState()
        )

        upsertEstateViewModel.onPointOfInterestSelected(pointOfInterest1, true)
        val viewStateLiveData = upsertEstateViewModel.getViewState()
        val viewState = LiveDataTestUtil.getOrAwaitValue(viewStateLiveData)

        assertEquals(expectedViewState, viewState)

        upsertEstateViewModel.onPointOfInterestSelected(pointOfInterest2, true)
        val viewStateLiveData2 = upsertEstateViewModel.getViewState()
        val viewState2 = LiveDataTestUtil.getOrAwaitValue(viewStateLiveData2)

        assertEquals(expectedViewState2, viewState2)

        upsertEstateViewModel.onPointOfInterestSelected(pointOfInterest1, false)
        val viewStateLiveData3 = upsertEstateViewModel.getViewState()
        val viewState3 = LiveDataTestUtil.getOrAwaitValue(viewStateLiveData3)

        assertEquals(expectedViewState3, viewState3)
    }

    @Test
    fun addPhotoAndRemovedPhotoAndPhotoDescriptionChanged() = runTest {
        val mockUri: Uri = mockk()
        val photoUri = "https://photo5.png"
        val message = "This field is required"
        every { mockUri.toString() } returns photoUri
        every { mockResources.getString(any()) } returns message
        val upsertEstateViewModel = instantiateViewModel()
        val photoDescription = "Bathroom"
        val addedPhotoViewState = PhotoViewState(uri = photoUri, new = true, error = message)
        val photoViewState =
            PhotoViewState(uri = photoUri, description = photoDescription, new = true)
        val expectedViewState =
            UpsertEstateViewState(CurrentDataViewState(photo = listOf(addedPhotoViewState)))
        val expectedViewState2 =
            UpsertEstateViewState(CurrentDataViewState(photo = listOf(photoViewState)))
        val expectedViewState3 = UpsertEstateViewState()

        upsertEstateViewModel.addPhoto(mockUri)
        val viewStateLiveData = upsertEstateViewModel.getViewState()
        val viewState = LiveDataTestUtil.getOrAwaitValue(viewStateLiveData)
        assertEquals(expectedViewState, viewState)

        upsertEstateViewModel.onPhotoDescriptionChanged(addedPhotoViewState, photoDescription)
        val viewStateLiveData2 = upsertEstateViewModel.getViewState()
        val viewState2 = LiveDataTestUtil.getOrAwaitValue(viewStateLiveData2)
        assertEquals(expectedViewState2, viewState2)

        upsertEstateViewModel.removePhoto(photoViewState)
        val viewStateLiveData3 = upsertEstateViewModel.getViewState()
        val viewState3 = LiveDataTestUtil.getOrAwaitValue(viewStateLiveData3)
        assertEquals(expectedViewState3, viewState3)
    }

    @Test
    fun saveSuccess() = runTest {
        val upsertEstateViewModel = instantiateViewModel()
        val propertyType = PropertyType.LOFT
        val date = 10064555236
        val price = 15000L
        val area = 250L
        val bathrooms = 2
        val bedrooms = 3
        val description = "Big house"
        val address = "123 Main St"
        val city = "New York"
        val zipcode = "10001"
        val country = "United State"
        val pointsOfInterest = PointOfInterest.RESTAURANTS_AND_CAFES
        val available = true
        val realEstateAgent = FakeDataTest.getFakeRealEstateAgent(1L)
        mockAllValidationWithSuccess()
        mockkStatic(Utils::class)
        every { Utils.getNow() } returns date
        val expectedEstate = Estate(
            type = propertyType,
            price = price,
            propertyArea = area,
            numberOfBathrooms = bathrooms,
            numberOfBedrooms = bedrooms,
            description = description,
            photos = listOf(),
            address = address,
            additionalAddressLine = null,
            city = city,
            zipCode = zipcode,
            country = country,
            latitude = latitude,
            longitude = longitude,
            pointsOfInterest = listOf(pointsOfInterest),
            available = available,
            entryDate = date,
            saleDate = null,
            realEstateAgent = realEstateAgent
        )

        upsertEstateViewModel.onTypeChanged(propertyType)
        upsertEstateViewModel.onPriceChanged(price.toString())
        upsertEstateViewModel.onSurfaceChanged(area.toString())
        upsertEstateViewModel.onNumberOfBathroomChanged(bathrooms.toString())
        upsertEstateViewModel.onNumberOfBedroomChanged(bedrooms.toString())
        upsertEstateViewModel.onDescriptionChanged(description)
        upsertEstateViewModel.onAddressChanged(address)
        upsertEstateViewModel.onCityChanged(city)
        upsertEstateViewModel.onZipcodeChanged(zipcode)
        upsertEstateViewModel.onCountryChanged(country)
        upsertEstateViewModel.onPointOfInterestSelected(pointsOfInterest, true)
        upsertEstateViewModel.onAvailableChange(available)
        upsertEstateViewModel.onAgentChanged(realEstateAgent)
        upsertEstateViewModel.save()

        coVerify { mockEstateRepository.upsertEstate(expectedEstate, emptyList()) }
        val snackBar = LiveDataTestUtil.getOrAwaitValue(upsertEstateViewModel.snackBar)

        assertEquals(UpsertEstateViewModel.Event.SaveSuccess, snackBar)
    }

    private fun mockAllValidationWithSuccess() {
        mockkObject(UpsertEstateValidation)
        every {
            UpsertEstateValidation.validateType(
                mockResources,
                any()
            )
        } returns UpsertEstateValidation.ValidationResult.Valid
        every {
            UpsertEstateValidation.validatePrice(
                mockResources,
                any()
            )
        } returns UpsertEstateValidation.ValidationResult.Valid
        every {
            UpsertEstateValidation.validateSurface(
                mockResources,
                any()
            )
        } returns UpsertEstateValidation.ValidationResult.Valid
        every {
            UpsertEstateValidation.validateNbOfBathroom(
                mockResources,
                any()
            )
        } returns UpsertEstateValidation.ValidationResult.Valid
        every {
            UpsertEstateValidation.validateNbOfBedroom(
                mockResources,
                any()
            )
        } returns UpsertEstateValidation.ValidationResult.Valid
        every {
            UpsertEstateValidation.validateAddress(
                mockResources,
                any()
            )
        } returns UpsertEstateValidation.ValidationResult.Valid
        every {
            UpsertEstateValidation.validateZipcode(
                mockResources,
                any()
            )
        } returns UpsertEstateValidation.ValidationResult.Valid
        every {
            UpsertEstateValidation.validateCity(
                mockResources,
                any()
            )
        } returns UpsertEstateValidation.ValidationResult.Valid
        every {
            UpsertEstateValidation.validateCountry(
                mockResources,
                any()
            )
        } returns UpsertEstateValidation.ValidationResult.Valid
        every {
            UpsertEstateValidation.validateAgent(
                mockResources,
                any()
            )
        } returns UpsertEstateValidation.ValidationResult.Valid
    }

}