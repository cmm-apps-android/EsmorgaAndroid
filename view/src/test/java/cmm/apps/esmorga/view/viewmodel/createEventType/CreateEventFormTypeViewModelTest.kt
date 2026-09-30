package cmm.apps.esmorga.view.viewmodel.createEventType

import app.cash.turbine.test
import cmm.apps.esmorga.domain.event.model.EventType
import cmm.apps.esmorga.view.createevent.CreateEventFlowViewModel
import cmm.apps.esmorga.view.createevent.createeventtype.CreateEventFormTypeViewModel
import cmm.apps.esmorga.view.createevent.createeventtype.model.CreateEventTypeScreenEffect
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CreateEventFormTypeViewModelTest {
    private lateinit var viewModel: CreateEventFormTypeViewModel
    private lateinit var flowViewModel: CreateEventFlowViewModel

    @Before
    fun setup() {
        flowViewModel = CreateEventFlowViewModel().apply { updateType(EventType.PARTY) }
        viewModel = CreateEventFormTypeViewModel(flowViewModel)
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
    fun `given initial form with existing type when screen started then existing type is selected`() = runTest {
        val flowViewModel = CreateEventFlowViewModel().apply { updateType(EventType.CHARITY) }
        val viewModel = CreateEventFormTypeViewModel(flowViewModel)

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(EventType.CHARITY, state.type)
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
            viewModel.onEventTypeSelected(EventType.FOOD)
            viewModel.onBackClick()
            val effect = awaitItem()
            assertEquals(CreateEventTypeScreenEffect.NavigateBack, effect)
            assertEquals(EventType.FOOD, flowViewModel.eventForm.value.type)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given selected event type when next button is clicked then navigate to next screen with correct data`() = runTest {
        viewModel.onEventTypeSelected(EventType.GAMES)

        viewModel.effect.test {
            viewModel.onNextClick()
            val effect = awaitItem()
            assertEquals(CreateEventTypeScreenEffect.NavigateNext, effect)
            assertEquals(EventType.GAMES, flowViewModel.eventForm.value.type)

            cancelAndIgnoreRemainingEvents()
        }
    }
}