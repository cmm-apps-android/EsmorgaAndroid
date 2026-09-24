package cmm.apps.esmorga.view.viewmodel.createEventForm

import app.cash.turbine.test
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.createevent.createeventinfo.CreateEventFormTitleViewModel
import cmm.apps.esmorga.view.createevent.createeventinfo.model.CreateEventFormEffect
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
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

            viewModel.onDescriptionChange("This is a good description with more than 20 chars")

            val state = awaitItem()

            assertEquals("This is a good description with more than 20 chars", state.eventDescription)
            assertNull(state.descriptionError)
            assertFalse(state.isFormValid)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given valid name and empty description then form is valid`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onEventNameChange("Valid Name")

            val state = awaitItem()

            assertEquals("Valid Name", state.eventName)
            assertNull(state.eventNameError)
            assertNull(state.eventDescription)
            assertNull(state.descriptionError)
            assertTrue(state.isFormValid)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given short description under 20 chars when description changes then state shows error`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onEventNameChange("Valid Name")
            awaitItem()

            viewModel.onDescriptionChange("Short description")
            val state = awaitItem()

            assertEquals("Short description", state.eventDescription)
            assertEquals(R.string.inline_error_invalid_length_description, state.descriptionError)
            assertFalse(state.isFormValid)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given valid form with description when next clicked then emits navigate to next screen`() = runTest {
        viewModel.onEventNameChange("Valid Name")
        viewModel.onDescriptionChange("Valid description with at least 20 chars")

        viewModel.effect.test {
            viewModel.onNextClick()

            val effect = awaitItem()
            assertTrue(effect is CreateEventFormEffect.NavigateNext)
            val navigateEffect = effect as CreateEventFormEffect.NavigateNext
            assertEquals("Valid Name", navigateEffect.eventForm.name)
            assertEquals("Valid description with at least 20 chars", navigateEffect.eventForm.description)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given valid form without description when next clicked then emits navigate with null description`() = runTest {
        viewModel.onEventNameChange("Valid Name")

        viewModel.effect.test {
            viewModel.onNextClick()

            val effect = awaitItem()
            assertTrue(effect is CreateEventFormEffect.NavigateNext)
            val navigateEffect = effect as CreateEventFormEffect.NavigateNext
            assertEquals("Valid Name", navigateEffect.eventForm.name)
            assertNull(navigateEffect.eventForm.description)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given invalid form when next clicked then does not emit navigate to next screen`() = runTest {
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
