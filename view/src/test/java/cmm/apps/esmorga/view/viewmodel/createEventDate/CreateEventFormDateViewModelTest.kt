package cmm.apps.esmorga.view.viewmodel.createEventDate

import app.cash.turbine.test
import cmm.apps.esmorga.domain.event.model.CreateEventForm
import cmm.apps.esmorga.domain.event.model.EventType
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.createevent.createeventdate.CreateEventFormDateViewModel
import cmm.apps.esmorga.view.createevent.createeventdate.model.CreateEventFormDateEffect
import cmm.apps.esmorga.view.dateformatting.DateFormatterImpl
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
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
        viewModel = CreateEventFormDateViewModel(initialForm, DateFormatterImpl())
    }

    @Test
    fun `given create event form date screen, when screen started, then the button is disabled`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()

            assertFalse(state.isButtonEnabled)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given create event form date screen, when event time is selected, then the button is enabled`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onTimeSelected("12:00:00.000Z")
            val updatedState = awaitItem()

            assertTrue(updatedState.isButtonEnabled)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given create event form date screen, when next button is clicked, then navigate to next screen with correct date and no deadline`() = runTest {
        val cal = Calendar.getInstance()
        cal.set(2024, Calendar.JULY, 17)
        val date: Date = cal.time
        val time = "12:00:00.000Z"
        val expectedDateTime = "2024-07-17T${time}"

        viewModel.effect.test {
            viewModel.onNextClick(date, time, null, "")
            val effect = awaitItem()
            assertTrue(effect is CreateEventFormDateEffect.NavigateNext)
            val navigateNext = effect as CreateEventFormDateEffect.NavigateNext

            assertEquals(initialForm.name, navigateNext.eventForm.name)
            assertEquals(initialForm.description, navigateNext.eventForm.description)
            assertEquals(initialForm.type, navigateNext.eventForm.type)
            assertEquals(expectedDateTime, navigateNext.eventForm.date)
            assertNull(navigateNext.eventForm.joinDeadline)

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
    fun `given create event date screen when clicked confirm dialog then return time formatted`() {
        val hour = 12
        val minute = 30
        val formattedTime = "${hour}:${minute}:00.000Z"

        val time = viewModel.formattedTime(hour, minute)
        assertEquals(formattedTime, time)
    }

    @Test
    fun `given event time selected, when deadline toggle is turned on, then button is disabled`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onTimeSelected("12:00:00.000Z")
            awaitItem() // button enabled
            viewModel.onDeadlineToggleChanged(true)
            val state = awaitItem()

            assertFalse(state.isButtonEnabled)
            assertTrue(state.isDeadlineToggleOn)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given deadline toggle on, when toggle is turned off, then button is enabled if event time was selected`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onTimeSelected("12:00:00.000Z")
            awaitItem()
            viewModel.onDeadlineToggleChanged(true)
            awaitItem()
            viewModel.onDeadlineToggleChanged(false)
            val state = awaitItem()

            assertTrue(state.isButtonEnabled)
            assertFalse(state.isDeadlineToggleOn)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given deadline toggle on and event time selected, when valid deadline time selected, then button is enabled and no error`() = runTest {
        val cal = Calendar.getInstance()
        cal.set(2024, Calendar.JULY, 17)
        val eventDateMillis = cal.timeInMillis

        val deadlineCal = Calendar.getInstance()
        deadlineCal.set(2024, Calendar.JULY, 10)
        val deadlineDateMillis = deadlineCal.timeInMillis

        viewModel.uiState.test {
            awaitItem()
            viewModel.onTimeSelected("17:00:00.000Z")
            awaitItem()
            viewModel.onDeadlineToggleChanged(true)
            awaitItem()
            viewModel.onDeadlineTimeSelected(eventDateMillis, deadlineDateMillis, "10:00:00.000Z")
            val state = awaitItem()

            assertTrue(state.isButtonEnabled)
            assertNull(state.deadlineErrorRes)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given deadline toggle on, when deadline is after event date, then button is disabled and error is shown`() = runTest {
        val cal = Calendar.getInstance()
        cal.set(2024, Calendar.JULY, 10)
        val eventDateMillis = cal.timeInMillis

        val deadlineCal = Calendar.getInstance()
        deadlineCal.set(2024, Calendar.JULY, 17)
        val deadlineDateMillis = deadlineCal.timeInMillis

        viewModel.uiState.test {
            awaitItem()
            viewModel.onTimeSelected("12:00:00.000Z")
            awaitItem()
            viewModel.onDeadlineToggleChanged(true)
            awaitItem()
            viewModel.onDeadlineTimeSelected(eventDateMillis, deadlineDateMillis, "10:00:00.000Z")
            val state = awaitItem()

            assertFalse(state.isButtonEnabled)
            assertEquals(R.string.inline_error_event_date_deadline_exceeded, state.deadlineErrorRes)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given deadline toggle on with error, when deadline date changes to valid, then error is cleared`() = runTest {
        val cal = Calendar.getInstance()
        cal.set(2024, Calendar.JULY, 10)
        val eventDateMillis = cal.timeInMillis

        val lateDateCal = Calendar.getInstance()
        lateDateCal.set(2024, Calendar.JULY, 17)
        val lateDateMillis = lateDateCal.timeInMillis

        val earlyDateCal = Calendar.getInstance()
        earlyDateCal.set(2024, Calendar.JULY, 5)
        val earlyDateMillis = earlyDateCal.timeInMillis

        viewModel.uiState.test {
            awaitItem()
            viewModel.onTimeSelected("12:00:00.000Z")
            awaitItem()
            viewModel.onDeadlineToggleChanged(true)
            awaitItem()
            viewModel.onDeadlineTimeSelected(eventDateMillis, lateDateMillis, "10:00:00.000Z")
            awaitItem() // error state
            viewModel.onDeadlineDateChanged(eventDateMillis, earlyDateMillis)
            val state = awaitItem()

            assertTrue(state.isButtonEnabled)
            assertNull(state.deadlineErrorRes)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given deadline same day as event, when deadline time is before event time, then button is enabled`() = runTest {
        val cal = Calendar.getInstance()
        cal.set(2024, Calendar.JULY, 17)
        val sameDateMillis = cal.timeInMillis

        viewModel.uiState.test {
            awaitItem()
            viewModel.onTimeSelected("17:00:00.000Z")
            awaitItem()
            viewModel.onDeadlineToggleChanged(true)
            awaitItem()
            viewModel.onDeadlineTimeSelected(sameDateMillis, sameDateMillis, "10:00:00.000Z")
            val state = awaitItem()

            assertTrue(state.isButtonEnabled)
            assertNull(state.deadlineErrorRes)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given deadline same day as event, when deadline time equals event time, then button is enabled`() = runTest {
        val cal = Calendar.getInstance()
        cal.set(2024, Calendar.JULY, 17)
        val sameDateMillis = cal.timeInMillis

        viewModel.uiState.test {
            awaitItem()
            viewModel.onTimeSelected("17:00:00.000Z")
            awaitItem()
            viewModel.onDeadlineToggleChanged(true)
            awaitItem()
            viewModel.onDeadlineTimeSelected(sameDateMillis, sameDateMillis, "17:00:00.000Z")
            val state = awaitItem()

            assertTrue(state.isButtonEnabled)
            assertNull(state.deadlineErrorRes)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given deadline same day as event, when deadline time is after event time, then button is disabled and error is shown`() = runTest {
        val cal = Calendar.getInstance()
        cal.set(2024, Calendar.JULY, 17)
        val sameDateMillis = cal.timeInMillis

        viewModel.uiState.test {
            awaitItem()
            viewModel.onTimeSelected("10:00:00.000Z")
            awaitItem()
            viewModel.onDeadlineToggleChanged(true)
            awaitItem()
            viewModel.onDeadlineTimeSelected(sameDateMillis, sameDateMillis, "17:00:00.000Z")
            val state = awaitItem()

            assertFalse(state.isButtonEnabled)
            assertEquals(R.string.inline_error_event_date_deadline_exceeded, state.deadlineErrorRes)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given deadline toggle on with valid deadline, when next clicked, then form has joinDeadline set`() = runTest {
        val eventCal = Calendar.getInstance()
        eventCal.set(2024, Calendar.JULY, 17)
        val eventDate = eventCal.time
        val eventDateMillis = eventCal.timeInMillis
        val eventTime = "17:00:00.000Z"

        val deadlineCal = Calendar.getInstance()
        deadlineCal.set(2024, Calendar.JULY, 10)
        val deadlineDateMillis = deadlineCal.timeInMillis
        val deadlineTime = "23:59:00.000Z"
        val expectedDeadline = "2024-07-10T${deadlineTime}"

        viewModel.uiState.test {
            awaitItem()
            viewModel.onTimeSelected(eventTime)
            awaitItem()
            viewModel.onDeadlineToggleChanged(true)
            awaitItem()
            viewModel.onDeadlineTimeSelected(eventDateMillis, deadlineDateMillis, deadlineTime)
            awaitItem()
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.effect.test {
            viewModel.onNextClick(eventDate, eventTime, deadlineDateMillis, deadlineTime)
            val effect = awaitItem() as CreateEventFormDateEffect.NavigateNext

            assertEquals(expectedDeadline, effect.eventForm.joinDeadline)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given deadline toggle off, when next clicked, then form has null joinDeadline`() = runTest {
        val eventCal = Calendar.getInstance()
        eventCal.set(2024, Calendar.JULY, 17)
        val eventDate = eventCal.time
        val eventTime = "17:00:00.000Z"

        viewModel.effect.test {
            viewModel.onNextClick(eventDate, eventTime, null, "")
            val effect = awaitItem() as CreateEventFormDateEffect.NavigateNext

            assertNull(effect.eventForm.joinDeadline)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
