package cmm.apps.esmorga.view.viewmodel.createEventDate

import app.cash.turbine.test
import cmm.apps.esmorga.domain.event.model.CreateEventForm
import cmm.apps.esmorga.domain.event.model.EventType
import cmm.apps.esmorga.view.createeventdate.CreateEventFormDateViewModel
import cmm.apps.esmorga.view.createeventdate.model.CreateEventFormDateEffect
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.Calendar
import java.util.Date

class CreateEventFormDateViewModelTest {
    private lateinit var viewModel: CreateEventFormDateViewModel
    private lateinit var initialForm: CreateEventForm

    @Before
    fun setup() {
        initialForm = CreateEventForm(
            name = "Initial Name",
            description = "Initial Description",
            type = EventType.PARTY
        )
        viewModel = CreateEventFormDateViewModel(initialForm)
    }

    @Test
    fun `given create event form date screen, when screen started, then the button is disable`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()

            assertFalse(state.isButtonEnabled)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given create event form date screen, when time is selected, then the button is enable`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onTimeSelected("12:00")
            val updatedState = awaitItem()

            assertTrue(updatedState.isButtonEnabled)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given create event form date screen, when next button is clicked, then dateTime is not blank`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onNextClick(Date(), "12:00")
            val updatedState = awaitItem()
            assertTrue(updatedState.dateTime.isNotBlank())
        }
    }

    @Test
    fun `given create event form date screen, when next button is clicked, then navigate to next screen with correct data`() = runTest {
        val calendar = Calendar.getInstance()
        calendar.set(2024, Calendar.JULY, 17)
        val date: Date = calendar.time
        val time = "12:00:00.000Z"
        val esxpectedDateTime = "2024-07-17T${time}"

        viewModel.effect.test {
            viewModel.onNextClick(date, time)
            val effect = awaitItem()
            assertTrue(effect is CreateEventFormDateEffect.NavigateNext)
            val navigateNext = effect as CreateEventFormDateEffect.NavigateNext

            assertEquals(initialForm.name, navigateNext.eventForm.name)
            assertEquals(initialForm.description, navigateNext.eventForm.description)
            assertEquals(initialForm.type, navigateNext.eventForm.type)
            assertEquals(esxpectedDateTime, navigateNext.eventForm.date)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given create event date screen when back clicked then navigate to previous screen`() = runTest {
        viewModel.effect.test {
            viewModel.onBackClick()
            val effect = awaitItem()
            assertEquals(CreateEventFormDateEffect.NavigateBack, effect)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given create event date screen when clicked confirm dialog then return time formated`() {
        val hour = 12
        val minute = 30
        val formatedTime = "${hour}:${minute}:00.000Z"

        val time = viewModel.formattedTime(hour, minute)
        assertEquals(formatedTime, time)
    }
}