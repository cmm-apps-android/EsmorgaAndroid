package cmm.apps.esmorga.view.viewmodel.createEvent

import app.cash.turbine.test
import cmm.apps.esmorga.view.createevent.CreateEventFormViewModel
import cmm.apps.esmorga.view.createevent.model.CreateEventFormEffect
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import cmm.apps.esmorga.view.R

@OptIn(ExperimentalCoroutinesApi::class)
class CreateEventFormViewModelTest {

    private lateinit var viewModel: CreateEventFormViewModel

    @Before
    fun setup() {
        viewModel = CreateEventFormViewModel()
    }

    @Test
    fun `given initial state when event name changes with invalid length then state updates with error`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onEventNameChange("ab")

            awaitItem()
            val state = awaitItem()

            assertEquals("ab", state.eventName)
            assertEquals(R.string.inline_error_invalid_length_name, state.eventNameError)
            assertFalse(state.isFormValid)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given initial state when description changes with valid text then state updates without error`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onDescriptionChange("This is a good description")

            awaitItem()
            val state = awaitItem()

            assertEquals("This is a good description", state.description)
            assertNull(state.descriptionError)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given valid form when next clicked then emits navigate to step 2 effect`() = runTest {
        viewModel.onEventNameChange("Valid Name")
        viewModel.onDescriptionChange("Valid description")

        viewModel.effect.test {
            viewModel.onNextClick()

            val effect = awaitItem()
            assertEquals(CreateEventFormEffect.NavigateEventType, effect)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given invalid form when next clicked then does not emit navigate to step 2 effect`() = runTest {
        viewModel.effect.test {
            viewModel.onNextClick()
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when back clicked then emits navigate back effect`() = runTest {
        viewModel.effect.test {
            viewModel.onBackClick()
            val effect = awaitItem()
            assertEquals(CreateEventFormEffect.NavigateBack, effect)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
