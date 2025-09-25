package cmm.apps.esmorga.view.viewmodel.createEventType

import app.cash.turbine.test
import cmm.apps.esmorga.domain.event.model.CreateEventForm
import cmm.apps.esmorga.domain.event.model.EventType
import cmm.apps.esmorga.view.createeventtype.CreateEventFormTypeViewModel
import cmm.apps.esmorga.view.createeventtype.model.CreateEventTypeScreenEffect
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CreateEventFormTypeViewModelTest {
    private lateinit var viewModel: CreateEventFormTypeViewModel
    private lateinit var initialForm: CreateEventForm

    @Before
    fun setup() {
        initialForm = CreateEventForm(
            name = "Initial Name",
            description = "Initial Description",
            type = EventType.PARTY
        )
        viewModel = CreateEventFormTypeViewModel(initialForm)
    }

    @Test
    fun `given create event form type screen, when screen started, then default type is selected`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(EventType.PARTY, state.type)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given initial state when event type selected then updates selected event type`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onEventTypeSelected(EventType.SPORT)
            val updatedState = awaitItem()
            assertEquals(EventType.SPORT, updatedState.type)

            viewModel.onEventTypeSelected(EventType.CHARITY)
            val secondUpdate = awaitItem()
            assertEquals(EventType.CHARITY, secondUpdate.type)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given create event type screen when back clicked then navigate to previous screen`() = runTest {
        viewModel.effect.test {
            viewModel.onBackClick()
            val effect = awaitItem()
            assertEquals(CreateEventTypeScreenEffect.NavigateBack, effect)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given selected event type when next button is clicked then navigate to next screen with correct data`() = runTest {
        viewModel.onEventTypeSelected(EventType.GAMES)

        viewModel.effect.test {
            viewModel.onNextClick()
            val effect = awaitItem()
            assertTrue(effect is CreateEventTypeScreenEffect.NavigateNext)
            val navigateNext = effect as CreateEventTypeScreenEffect.NavigateNext

            assertEquals(initialForm.name, navigateNext.eventForm.name)
            assertEquals(initialForm.description, navigateNext.eventForm.description)
            assertEquals(EventType.GAMES, navigateNext.eventForm.type)

            cancelAndIgnoreRemainingEvents()
        }
    }
}