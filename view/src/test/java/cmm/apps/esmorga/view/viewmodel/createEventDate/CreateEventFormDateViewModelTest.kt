package cmm.apps.esmorga.view.viewmodel.createEventDate

import app.cash.turbine.test
import cmm.apps.esmorga.domain.event.model.EventType
import cmm.apps.esmorga.view.createevent.CreateEventFlowSession
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.createevent.createeventdate.CreateEventFormDateViewModel
import cmm.apps.esmorga.view.createevent.createeventdate.model.CreateEventFormDateEffect
import cmm.apps.esmorga.view.dateformatting.DateFormatterImpl
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

class CreateEventFormDateViewModelTest {
    private lateinit var viewModel: CreateEventFormDateViewModel
    private lateinit var flowViewModel: CreateEventFlowSession
    private lateinit var dateFormatter: DateFormatterImpl
    private val previousTimeZone: TimeZone = TimeZone.getDefault()

    @Before
    fun setup() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
        flowViewModel = CreateEventFlowSession().apply {
            updateTitle(name = "Initial Name", description = "Initial Description")
            updateType(EventType.PARTY)
        }
        dateFormatter = DateFormatterImpl()
        viewModel = CreateEventFormDateViewModel(flowViewModel, dateFormatter)
    }

    @After
    fun tearDown() {
        TimeZone.setDefault(previousTimeZone)
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
    fun `given persisted date data when viewModel created then state is restored from shared flow`() {
        flowViewModel.updateDate(
            date = "2024-07-17T12:30:00.000Z",
            joinDeadline = "2024-07-10T10:00:00.000Z"
        )

        val restoredViewModel = CreateEventFormDateViewModel(flowViewModel, dateFormatter)

        val state = restoredViewModel.uiState.value
        assertEquals(dateFormatter.toLocalDateEpochMillis("2024-07-17T12:30:00.000Z"), state.selectedDateMillis)
        assertEquals(dateFormatter.toLocalDateEpochMillis("2024-07-10T10:00:00.000Z"), state.selectedDeadlineDateMillis)
        assertEquals(dateFormatter.extractLocalTime("2024-07-17T12:30:00.000Z"), state.eventTime)
        assertEquals(dateFormatter.extractLocalTime("2024-07-10T10:00:00.000Z"), state.deadlineTime)
        assertTrue(state.isDeadlineToggleOn)
        assertTrue(state.isButtonEnabled)
        assertNull(state.deadlineErrorRes)
    }

    @Test
    fun `given create event form date screen, when event time is selected, then the button is enabled`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onTimeSelected(time(hour = 12, minute = 0))
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
        val time = time(hour = 12, minute = 0)
        val expectedDateTime = dateFormatter.formatIsoDateTime(date, time)

        viewModel.effect.test {
            viewModel.onNextClick(date, time, null, "")
            val effect = awaitItem()
            assertEquals(CreateEventFormDateEffect.NavigateNext, effect)
            assertEquals("Initial Name", flowViewModel.eventForm.value.name)
            assertEquals("Initial Description", flowViewModel.eventForm.value.description)
            assertEquals(EventType.PARTY, flowViewModel.eventForm.value.type)
            assertEquals(expectedDateTime, flowViewModel.eventForm.value.date)
            assertNull(flowViewModel.eventForm.value.joinDeadline)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given create event date screen when back clicked then navigate to previous screen`() = runTest {
        val eventDateMillis = 1790726400000L
        val deadlineDateMillis = 1790121600000L
        val eventTime = time(hour = 12, minute = 0)
        val deadlineTime = time(hour = 10, minute = 0)

        viewModel.onDateSelected(eventDateMillis)
        viewModel.onTimeSelected(eventTime)
        viewModel.onDeadlineToggleChanged(true)
        viewModel.onDeadlineTimeSelected(eventDateMillis, deadlineDateMillis, deadlineTime)

        viewModel.effect.test {
            viewModel.onBackClick()
            val effect = awaitItem()
            assertEquals(CreateEventFormDateEffect.NavigateBack, effect)
            assertEquals(dateFormatter.formatIsoDateTime(Date(eventDateMillis), eventTime), flowViewModel.eventForm.value.date)
            assertEquals(dateFormatter.formatIsoDateTime(Date(deadlineDateMillis), deadlineTime), flowViewModel.eventForm.value.joinDeadline)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given date data persisted on back when viewModel recreated then state is restored`() {
        val eventDateMillis = 1790726400000L
        val deadlineDateMillis = 1790121600000L
        val eventTime = time(hour = 12, minute = 0)
        val deadlineTime = time(hour = 10, minute = 0)

        viewModel.onDateSelected(eventDateMillis)
        viewModel.onTimeSelected(eventTime)
        viewModel.onDeadlineToggleChanged(true)
        viewModel.onDeadlineTimeSelected(eventDateMillis, deadlineDateMillis, deadlineTime)
        viewModel.onBackClick()

        val restoredViewModel = CreateEventFormDateViewModel(flowViewModel, dateFormatter)
        val state = restoredViewModel.uiState.value

        assertEquals(eventDateMillis, state.selectedDateMillis)
        assertEquals(deadlineDateMillis, state.selectedDeadlineDateMillis)
        assertEquals(eventTime, state.eventTime)
        assertEquals(deadlineTime, state.deadlineTime)
        assertTrue(state.isDeadlineToggleOn)
    }

    @Test
    fun `given create event date screen when clicked confirm dialog then return time formatted`() {
        val hour = 12
        val minute = 30
        val formattedTime = time(hour, minute)

        val time = viewModel.formattedTime(hour, minute)
        assertEquals(formattedTime, time)
    }

    @Test
    fun `given event time selected, when deadline toggle is turned on, then button is disabled`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onTimeSelected(time(hour = 12, minute = 0))
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
            viewModel.onTimeSelected(time(hour = 12, minute = 0))
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
            viewModel.onTimeSelected(time(hour = 17, minute = 0))
            awaitItem()
            viewModel.onDeadlineToggleChanged(true)
            awaitItem()
            viewModel.onDeadlineTimeSelected(eventDateMillis, deadlineDateMillis, time(hour = 10, minute = 0))
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
            viewModel.onTimeSelected(time(hour = 12, minute = 0))
            awaitItem()
            viewModel.onDeadlineToggleChanged(true)
            awaitItem()
            viewModel.onDeadlineTimeSelected(eventDateMillis, deadlineDateMillis, time(hour = 10, minute = 0))
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
            viewModel.onTimeSelected(time(hour = 12, minute = 0))
            awaitItem()
            viewModel.onDeadlineToggleChanged(true)
            awaitItem()
            viewModel.onDeadlineTimeSelected(eventDateMillis, lateDateMillis, time(hour = 10, minute = 0))
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
            viewModel.onTimeSelected(time(hour = 17, minute = 0))
            awaitItem()
            viewModel.onDeadlineToggleChanged(true)
            awaitItem()
            viewModel.onDeadlineTimeSelected(sameDateMillis, sameDateMillis, time(hour = 10, minute = 0))
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
            viewModel.onTimeSelected(time(hour = 17, minute = 0))
            awaitItem()
            viewModel.onDeadlineToggleChanged(true)
            awaitItem()
            viewModel.onDeadlineTimeSelected(sameDateMillis, sameDateMillis, time(hour = 17, minute = 0))
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
            viewModel.onTimeSelected(time(hour = 10, minute = 0))
            awaitItem()
            viewModel.onDeadlineToggleChanged(true)
            awaitItem()
            viewModel.onDeadlineTimeSelected(sameDateMillis, sameDateMillis, time(hour = 17, minute = 0))
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
        val eventTime = time(hour = 17, minute = 0)

        val deadlineCal = Calendar.getInstance()
        deadlineCal.set(2024, Calendar.JULY, 10)
        val deadlineDateMillis = deadlineCal.timeInMillis
        val deadlineTime = time(hour = 23, minute = 59)
        val expectedDeadline = dateFormatter.formatIsoDateTime(deadlineCal.time, deadlineTime)

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
            viewModel.onNextClick(eventDate, eventTime, deadlineCal.time, deadlineTime)
            assertEquals(CreateEventFormDateEffect.NavigateNext, awaitItem())
            assertEquals(expectedDeadline, flowViewModel.eventForm.value.joinDeadline)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given deadline toggle off, when next clicked, then form has null joinDeadline`() = runTest {
        val eventCal = Calendar.getInstance()
        eventCal.set(2024, Calendar.JULY, 17)
        val eventDate = eventCal.time
        val eventTime = time(hour = 17, minute = 0)

        viewModel.effect.test {
            viewModel.onNextClick(eventDate, eventTime, null, "")
            assertEquals(CreateEventFormDateEffect.NavigateNext, awaitItem())
            assertNull(flowViewModel.eventForm.value.joinDeadline)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when date is selected then uiState is updated with selectedDateMillis`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            val millis = 1790726400000L
            viewModel.onDateSelected(millis)
            val state = awaitItem()
            assertEquals(millis, state.selectedDateMillis)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun time(hour: Int, minute: Int): String = String.format("%02d:%02d:00.000", hour, minute)
}
