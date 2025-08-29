package cmm.apps.esmorga.view.viewmodel.createEventForm

import app.cash.turbine.test
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.createevent.CreateEventFormTitleViewModel
import cmm.apps.esmorga.view.createevent.model.CreateEventFormEffect
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CreateEventFormTitleViewModelTest {

    private lateinit var viewModel: CreateEventFormTitleViewModel

    @Before
    fun setup() {
        viewModel = CreateEventFormTitleViewModel()
    }
    @Test

    fun `given initial state when event name changes with invalid length then state updates with error`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onEventNameChange("ab")

            val stateAfterChange = awaitItem()
            val stateAfterValidation = awaitItem()

            assertEquals("ab", stateAfterValidation.eventName)
            assertEquals(R.string.inline_error_invalid_length_name, stateAfterValidation.eventNameError)
            assertFalse(stateAfterValidation.isFormValid)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given initial state when description changes with valid text but name is empty then form is still invalid`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onDescriptionChange("This is a good description")

            val state = awaitItem()

            assertEquals("This is a good description", state.eventDescription)
            assertNull(state.descriptionError)
            assertFalse(state.isFormValid)

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
            assertTrue(effect is CreateEventFormEffect.NavigateNext)
            val navigateEffect = effect as CreateEventFormEffect.NavigateNext
            assertEquals("Valid Name", navigateEffect.eventForm.name)
            assertEquals("Valid description", navigateEffect.eventForm.description)

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
    fun `given create form screen when back clicked then navigate to previous screen`() = runTest {
        viewModel.effect.test {
            viewModel.onBackClick()
            val effect = awaitItem()
            assertEquals(CreateEventFormEffect.NavigateBack, effect)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
