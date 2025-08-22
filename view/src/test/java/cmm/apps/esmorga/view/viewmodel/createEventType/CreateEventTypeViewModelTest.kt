package cmm.apps.esmorga.view.viewmodel.createEventType

import app.cash.turbine.test
import cmm.apps.esmorga.view.createeventtype.CreateEventTypeViewModel
import cmm.apps.esmorga.view.createeventtype.EventType
import cmm.apps.esmorga.view.createeventtype.model.CreateEventTypeScreenEffect
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreateEventTypeViewModelTest {
    private lateinit var viewModel: CreateEventTypeViewModel

    @Before
    fun setup() {
        viewModel = CreateEventTypeViewModel(
            eventName = "Initial Name",
            description = "Initial Description"
        )
    }

    @Test
    fun `given initial state when initialized then contains provided eventName description and default event type`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()

            assertEquals("Initial Name", state.eventName)
            assertEquals("Initial Description", state.description)
            assertEquals(EventType.Party, state.selectedEventType)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given initial state when event type selected then updates selected event type in state`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onEventTypeSelected(EventType.Sport)

            val updatedState = awaitItem()
            assertEquals(EventType.Sport, updatedState.selectedEventType)

            viewModel.onEventTypeSelected(EventType.Charity)

            val secondUpdate = awaitItem()
            assertEquals(EventType.Charity, secondUpdate.selectedEventType)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when back clicked then emits navigate back effect`() = runTest {
        viewModel.effect.test {
            viewModel.onBackClick()

            val effect = awaitItem()
            assertEquals(CreateEventTypeScreenEffect.NavigateBack, effect)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given selected event type when next clicked then emits navigate next effect with correct data`() = runTest {
        viewModel.onEventTypeSelected(EventType.Games)

        viewModel.effect.test {
            viewModel.onNextClick()

            val effect = awaitItem()
            assertTrue(effect is CreateEventTypeScreenEffect.NavigateNext)
            val navigateNext = effect as CreateEventTypeScreenEffect.NavigateNext

            assertEquals("Initial Name", navigateNext.eventName)
            assertEquals("Initial Description", navigateNext.description)
            assertEquals(EventType.Games.backendValue, navigateNext.eventType)

            cancelAndIgnoreRemainingEvents()
        }
    }
}