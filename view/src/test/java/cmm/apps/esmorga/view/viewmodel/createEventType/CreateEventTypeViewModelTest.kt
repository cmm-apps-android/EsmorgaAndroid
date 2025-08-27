package cmm.apps.esmorga.view.viewmodel.createEventType

import app.cash.turbine.test
import cmm.apps.esmorga.domain.event.model.EventType
import cmm.apps.esmorga.view.createevent.model.CreateEventFormUiModel
import cmm.apps.esmorga.view.createeventtype.CreateEventTypeViewModel
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
    private lateinit var initialForm: CreateEventFormUiModel

    @Before
    fun setup() {
        initialForm = CreateEventFormUiModel(
            name = "Initial Name",
            description = "Initial Description",
            type = EventType.PARTY
        )
        viewModel = CreateEventTypeViewModel(initialForm)
    }

    @Test
    fun `given initial state when initialized then type is correct`() = runTest {
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
    fun `given selected event type when next clicked then emits navigate next effect with correct form`() = runTest {
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