package cmm.apps.esmorga.view.viewmodel.createEventLocation

import app.cash.turbine.test
import cmm.apps.esmorga.view.createevent.CreateEventFlowSession
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.createevent.createeventlocation.CreateEventFormLocationViewModel
import cmm.apps.esmorga.view.createevent.createeventlocation.model.CreateEventFormLocationEffect
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CreateEventFormLocationViewModelTest {

    private lateinit var viewModel: CreateEventFormLocationViewModel
    private lateinit var flowViewModel: CreateEventFlowSession

    @Before
    fun setup() {
        flowViewModel = CreateEventFlowSession()
        viewModel = CreateEventFormLocationViewModel(flowViewModel)
    }

    @Test
    fun `given initial state when location is blank then shows error and button disabled`() = runTest {
        viewModel.onLocationChanged("")

        val state = viewModel.uiState.value
        assertEquals(R.string.inline_error_location_required, state.locationError)
        assertFalse(state.isButtonEnabled)
    }

    @Test
    fun `given valid location when location changes then button is enabled and no error`() = runTest {
        viewModel.onLocationChanged("Madrid")

        val state = viewModel.uiState.value
        assertNull(state.locationError)
        assertTrue(state.isButtonEnabled)
    }

    @Test
    fun `given invalid coordinates format then shows error and button is disabled`() = runTest {
        viewModel.onLocationChanged("Madrid")
        viewModel.onCoordinatesChanged("invalid_format")

        val state = viewModel.uiState.value
        assertEquals(R.string.inline_error_coordinates_invalid, state.coordinatesError)
        assertFalse(state.isButtonEnabled)
    }

    @Test
    fun `given coordinates with correct format then button is enabled`() = runTest {
        viewModel.onLocationChanged("Madrid")
        viewModel.onCoordinatesChanged(" 41.40338 , 2.17403 ")

        val state = viewModel.uiState.value
        assertNull(state.coordinatesError)
        assertTrue(state.isButtonEnabled)
    }

    @Test
    fun `given zero capacity then shows error and button is disabled`() = runTest {
        viewModel.onLocationChanged("Madrid")
        viewModel.onMaxCapacityChanged("0")

        val state = viewModel.uiState.value
        assertEquals(R.string.inline_error_max_capacity_invalid, state.capacityError)
        assertFalse(state.isButtonEnabled)
    }

    @Test
    fun `given valid capacity then button is enabled`() = runTest {
        viewModel.onLocationChanged("Madrid")
        viewModel.onMaxCapacityChanged("150")

        val state = viewModel.uiState.value
        assertNull(state.capacityError)
        assertTrue(state.isButtonEnabled)
    }

    @Test
    fun `given valid data when next clicked then emits navigate next effect with all data`() = runTest {
        val expectedLocation = "Barcelona"
        val expectedLat = 41.3851
        val expectedLong = 2.1734
        val expectedCoordsInput = "$expectedLat, $expectedLong"
        val expectedCapacityInput = "100"
        val expectedCapacityInt = 100

        viewModel.onLocationChanged(expectedLocation)
        viewModel.onCoordinatesChanged(expectedCoordsInput)
        viewModel.onMaxCapacityChanged(expectedCapacityInput)

        viewModel.effect.test {
            viewModel.onNextClick()

            val effect = awaitItem()
            assertEquals(CreateEventFormLocationEffect.NavigateNext, effect)
            val form = flowViewModel.eventForm.value
            assertEquals(expectedLocation, form.location?.name)
            assertEquals(expectedLat, form.location?.lat)
            assertEquals(expectedLong, form.location?.long)
            assertEquals(expectedCapacityInt, form.maxCapacity)
        }
    }

    @Test
    fun `given location screen when back clicked then emits navigate back`() = runTest {
        viewModel.onLocationChanged("A Coruna")
        viewModel.onCoordinatesChanged("43.3623, -8.4115")
        viewModel.onMaxCapacityChanged("250")

        viewModel.effect.test {
            viewModel.onBackClick()
            assertEquals(CreateEventFormLocationEffect.NavigateBack, awaitItem())
            val form = flowViewModel.eventForm.value
            assertEquals("A Coruna", form.location?.name)
            assertEquals(43.3623, form.location?.lat)
            assertEquals(-8.4115, form.location?.long)
            assertEquals(250, form.maxCapacity)
        }
    }

    @Test
    fun `given location with more than max length when changed then state does not update`() = runTest {
        val maxLength = CreateEventFormLocationViewModel.LOCATION_NAME_MAX_LENGTH
        val longText = "a".repeat(maxLength + 1)

        viewModel.onLocationChanged(longText)

        val state = viewModel.uiState.value
        assertTrue(state.localizationName.length <= maxLength)
    }

    @Test
    fun `given capacity higher than 5000 then shows error and button is disabled`() = runTest {
        val maxCapacity = CreateEventFormLocationViewModel.MAX_CAPACITY_LIMIT
        viewModel.onLocationChanged("Madrid")
        viewModel.onMaxCapacityChanged((maxCapacity + 1).toString())

        val state = viewModel.uiState.value
        assertEquals(R.string.inline_error_max_capacity_invalid, state.capacityError)
        assertFalse(state.isButtonEnabled)
    }

    @Test
    fun `given capacity of 5000 then no error is displayed and button is enabled`() = runTest {
        viewModel.onLocationChanged("Madrid")
        viewModel.onMaxCapacityChanged("5000")

        val state = viewModel.uiState.value
        assertNull(state.capacityError)
        assertTrue(state.isButtonEnabled)
    }

    @Test
    fun `given coordinates with only spaces then should be treated as empty and no error`() = runTest {
        viewModel.onLocationChanged("Madrid")
        viewModel.onCoordinatesChanged("   ")

        val state = viewModel.uiState.value
        assertNull(state.coordinatesError)
        assertTrue(state.isButtonEnabled)
    }

    @Test
    fun `given invalid coordinates with text and numbers then shows error`() = runTest {
        viewModel.onLocationChanged("Madrid")
        viewModel.onCoordinatesChanged("40.123, longitude")

        val state = viewModel.uiState.value
        assertEquals(R.string.inline_error_coordinates_invalid, state.coordinatesError)
        assertFalse(state.isButtonEnabled)
    }

    @Test
    fun `given persisted location data when initialized then state is restored from shared flow`() {
        flowViewModel.updateLocation(
            location = cmm.apps.esmorga.domain.event.model.EventLocation(
                name = "Bilbao",
                lat = 43.2630,
                long = -2.9350
            ),
            maxCapacity = 200
        )

        viewModel = CreateEventFormLocationViewModel(flowViewModel)

        val state = viewModel.uiState.value
        assertEquals("Bilbao", state.localizationName)
        assertEquals("43.263, -2.935", state.localizationCoordinates)
        assertEquals("200", state.eventMaxCapacity)
        assertTrue(state.isButtonEnabled)
    }

    @Test
    fun `given location data persisted on back when viewModel recreated then state is restored`() {
        viewModel.onLocationChanged("Vigo")
        viewModel.onCoordinatesChanged("42.2406, -8.7207")
        viewModel.onMaxCapacityChanged("400")
        viewModel.onBackClick()

        val restoredViewModel = CreateEventFormLocationViewModel(flowViewModel)
        val state = restoredViewModel.uiState.value

        assertEquals("Vigo", state.localizationName)
        assertEquals("42.2406, -8.7207", state.localizationCoordinates)
        assertEquals("400", state.eventMaxCapacity)
        assertTrue(state.isButtonEnabled)
    }
}