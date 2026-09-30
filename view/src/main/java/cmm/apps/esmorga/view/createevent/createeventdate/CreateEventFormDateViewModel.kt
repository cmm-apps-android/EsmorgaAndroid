package cmm.apps.esmorga.view.createevent.createeventdate

import androidx.lifecycle.ViewModel
import cmm.apps.esmorga.view.createevent.CreateEventFlowViewModel
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.createevent.createeventdate.model.CreateEventFormDateEffect
import cmm.apps.esmorga.view.createevent.createeventdate.model.CreateEventFormDateUiState
import cmm.apps.esmorga.view.dateformatting.EsmorgaDateTimeFormatter
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Date

class CreateEventFormDateViewModel(
    private val createEventFlowViewModel: CreateEventFlowViewModel,
    private val esmorgaDateTimeFormatter: EsmorgaDateTimeFormatter
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateEventFormDateUiState())
    val uiState: StateFlow<CreateEventFormDateUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<CreateEventFormDateEffect>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val effect: SharedFlow<CreateEventFormDateEffect> = _effect.asSharedFlow()

    init {
        restoreFromFlow()
    }

    private fun restoreFromFlow() {
        val form = createEventFlowViewModel.eventForm.value
        updateUiState(
            _uiState.value.copy(
                selectedDateMillis = form.date?.let(esmorgaDateTimeFormatter::toLocalDateEpochMillis),
                selectedDeadlineDateMillis = form.joinDeadline?.let(esmorgaDateTimeFormatter::toLocalDateEpochMillis),
                eventTime = form.date?.let(esmorgaDateTimeFormatter::extractLocalTime).orEmpty(),
                deadlineTime = form.joinDeadline?.let(esmorgaDateTimeFormatter::extractLocalTime).orEmpty(),
                isDeadlineToggleOn = form.joinDeadline != null,
                deadlineErrorRes = null
            )
        )
    }

    fun onBackClick() {
        _effect.tryEmit(CreateEventFormDateEffect.NavigateBack)
    }

    fun onDateSelected(dateMillis: Long?) {
        val currentState = _uiState.value
        val newDeadlineError = computeDeadlineError(
            eventDateMillis = dateMillis,
            deadlineDateMillis = currentState.selectedDeadlineDateMillis,
            time = currentState.deadlineTime
        )
        updateUiState(
            currentState.copy(
                selectedDateMillis = dateMillis,
                deadlineErrorRes = newDeadlineError
            )
        )
    }

    fun onTimeSelected(selectedTime: String) {
        val currentState = _uiState.value
        val newDeadlineError = computeDeadlineError(
            eventDateMillis = currentState.selectedDateMillis,
            deadlineDateMillis = currentState.selectedDeadlineDateMillis,
            time = currentState.deadlineTime,
            eventTime = selectedTime
        )
        updateUiState(
            currentState.copy(
                eventTime = selectedTime,
                deadlineErrorRes = newDeadlineError
            )
        )
    }

    fun onDeadlineToggleChanged(isEnabled: Boolean) {
        val currentState = _uiState.value
        val newDeadlineError = if (isEnabled) {
            computeDeadlineError(
                eventDateMillis = currentState.selectedDateMillis,
                deadlineDateMillis = currentState.selectedDeadlineDateMillis,
                time = currentState.deadlineTime
            )
        } else {
            null
        }
        updateUiState(
            currentState.copy(
                isDeadlineToggleOn = isEnabled,
                deadlineErrorRes = newDeadlineError
            )
        )
    }

    fun onDeadlineTimeSelected(eventDateMillis: Long?, deadlineDateMillis: Long?, time: String) {
        updateUiState(
            _uiState.value.copy(
                selectedDateMillis = eventDateMillis,
                selectedDeadlineDateMillis = deadlineDateMillis,
                deadlineTime = time,
                deadlineErrorRes = computeDeadlineError(eventDateMillis, deadlineDateMillis, time)
            )
        )
    }

    fun onDeadlineDateChanged(eventDateMillis: Long?, deadlineDateMillis: Long?) {
        val state = _uiState.value
        val newDeadlineError = computeDeadlineError(eventDateMillis, deadlineDateMillis, state.deadlineTime)
        updateUiState(
            state.copy(
                selectedDateMillis = eventDateMillis,
                selectedDeadlineDateMillis = deadlineDateMillis,
                deadlineErrorRes = if (state.isDeadlineToggleOn && state.deadlineTime.isNotEmpty()) newDeadlineError else state.deadlineErrorRes
            )
        )
    }

    private fun computeDeadlineError(
        eventDateMillis: Long?,
        deadlineDateMillis: Long?,
        time: String,
        eventTime: String = _uiState.value.eventTime
    ): Int? {
        if (time.isEmpty() || eventTime.isEmpty() || eventDateMillis == null || deadlineDateMillis == null) return null
        val deadlineDateTime = esmorgaDateTimeFormatter.formatIsoDateTime(Date(deadlineDateMillis), time)
        val eventDateTime = esmorgaDateTimeFormatter.formatIsoDateTime(Date(eventDateMillis), eventTime)
        return if (deadlineDateTime > eventDateTime) R.string.inline_error_event_date_deadline_exceeded else null
    }

    private fun updateUiState(state: CreateEventFormDateUiState) {
        _uiState.value = state.copy(
            isButtonEnabled = state.eventTime.isNotEmpty() &&
                if (state.isDeadlineToggleOn) state.deadlineTime.isNotEmpty() && state.deadlineErrorRes == null else true
        )
    }

    fun formattedTime(hour: Int, minute: Int): String {
        return esmorgaDateTimeFormatter.formatTimeWithMillisUtcSuffix(hour, minute)
    }

    fun onNextClick(date: Date, time: String, deadLineDate: Date?, deadlineTimeArg: String) {
        val dateTime = esmorgaDateTimeFormatter.formatIsoDateTime(date, time)
        val joinDeadline = if (_uiState.value.isDeadlineToggleOn && deadlineTimeArg.isNotEmpty() && deadLineDate != null) {
            esmorgaDateTimeFormatter.formatIsoDateTime(deadLineDate, deadlineTimeArg)
        } else {
            null
        }
        createEventFlowViewModel.updateDate(date = dateTime, joinDeadline = joinDeadline)
        _effect.tryEmit(CreateEventFormDateEffect.NavigateNext)
    }
}
