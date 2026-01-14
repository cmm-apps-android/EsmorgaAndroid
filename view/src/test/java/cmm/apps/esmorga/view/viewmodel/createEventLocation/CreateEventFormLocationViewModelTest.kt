package cmm.apps.esmorga.view.viewmodel.createEventLocation

import app.cash.turbine.test
import cmm.apps.esmorga.domain.event.model.CreateEventForm
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.createeventlocation.CreateEventFormLocationViewModel
import cmm.apps.esmorga.view.createeventlocation.model.CreateEventFormLocationEffect
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CreateEventFormLocationViewModelTest {

    private lateinit var viewModel: CreateEventFormLocationViewModel

    @Before
    fun setup() {
        viewModel = CreateEventFormLocationViewModel(
            eventForm = CreateEventForm()
        )
    }

    @Test
    fun `given initial state when location is blank then shows error and button disabled`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onLocationChanged("")

            val state = awaitItem()
            assertEquals("", state.localizationName)
            assertEquals(R.string.inline_error_location_required, state.locationError)
            assertFalse(state.isButtonEnabled)
            assertFalse(state.isFormValid)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given valid location when location changes then button is enabled and no error`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onLocationChanged("Madrid")

            val state = awaitItem()
            assertEquals("Madrid", state.localizationName)
            assertNull(state.locationError)
            assertTrue(state.isButtonEnabled)
            assertTrue(state.isFormValid)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given user writes and clears location then shows required error`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onLocationChanged("Madrid")
            awaitItem()

            viewModel.onLocationChanged("")

            val state = awaitItem()
            assertEquals(R.string.inline_error_location_required, state.locationError)
            assertFalse(state.isFormValid)

            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `given valid location when next clicked then emits navigate next effect`() = runTest {
        viewModel.onLocationChanged("Barcelona")
        viewModel.onCoordinatesChanged("41.3851, 2.1734")

        viewModel.effect.test {
            viewModel.onNextClick()

            val effect = awaitItem()
            assertTrue(effect is CreateEventFormLocationEffect.NavigateNext)

            val navigateEffect = effect as CreateEventFormLocationEffect.NavigateNext
            assertEquals("Barcelona", navigateEffect.eventForm.location?.name)
            assertEquals(41.3851, navigateEffect.eventForm.location?.lat)
            assertEquals(2.1734, navigateEffect.eventForm.location?.long)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given location screen when back clicked then emits navigate back`() = runTest {
        viewModel.effect.test {
            viewModel.onBackClick()

            val effect = awaitItem()
            assertEquals(CreateEventFormLocationEffect.NavigateBack, effect)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
