package cmm.apps.esmorga.view.createevent.createeventdate

import androidx.lifecycle.ViewModel
import cmm.apps.esmorga.domain.event.model.CreateEventForm
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
    private val eventForm: CreateEventForm,
    private val esmorgaDateTimeFormatter: EsmorgaDateTimeFormatter
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateEventFormDateUiState())
    val uiState: StateFlow<CreateEventFormDateUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<CreateEventFormDateEffect>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val effect: SharedFlow<CreateEventFormDateEffect> = _effect.asSharedFlow()

    private var eventTime: String = ""
    private var deadlineTime: String = ""

    fun onBackClick() {
        _effect.tryEmit(CreateEventFormDateEffect.NavigateBack)
    }

    fun onTimeSelected(selectedTime: String) {
        eventTime = selectedTime
        val deadlineValid = computeDeadlineValid(_uiState.value.isDeadlineToggleOn, _uiState.value.deadlineErrorRes)
        _uiState.value = _uiState.value.copy(isButtonEnabled = eventTime.isNotEmpty() && deadlineValid)
    }

    fun onDeadlineToggleChanged(isEnabled: Boolean) {
        val deadlineValid = computeDeadlineValid(isEnabled, _uiState.value.deadlineErrorRes)
        _uiState.value = _uiState.value.copy(
            isDeadlineToggleOn = isEnabled,
            deadlineErrorRes = if (isEnabled) _uiState.value.deadlineErrorRes else null,
            isButtonEnabled = eventTime.isNotEmpty() && deadlineValid
        )
    }

    fun onDeadlineTimeSelected(eventDateMillis: Long?, deadlineDateMillis: Long?, time: String) {
        deadlineTime = time
        val errorRes = computeDeadlineError(eventDateMillis, deadlineDateMillis, time)
        val deadlineValid = computeDeadlineValid(isToggleOn = true, deadlineErrorRes = errorRes)
        _uiState.value = _uiState.value.copy(
            deadlineErrorRes = errorRes,
            isButtonEnabled = eventTime.isNotEmpty() && deadlineValid
        )
    }

    fun onDeadlineDateChanged(eventDateMillis: Long?, deadlineDateMillis: Long?) {
        if (deadlineTime.isEmpty() || !_uiState.value.isDeadlineToggleOn) return
        val errorRes = computeDeadlineError(eventDateMillis, deadlineDateMillis, deadlineTime)
        val deadlineValid = computeDeadlineValid(isToggleOn = true, deadlineErrorRes = errorRes)
        _uiState.value = _uiState.value.copy(
            deadlineErrorRes = errorRes,
            isButtonEnabled = eventTime.isNotEmpty() && deadlineValid
        )
    }

    private fun computeDeadlineError(eventDateMillis: Long?, deadlineDateMillis: Long?, time: String): Int? {
        if (time.isEmpty() || eventTime.isEmpty() || eventDateMillis == null || deadlineDateMillis == null) return null
        val deadlineDateTime = esmorgaDateTimeFormatter.formatIsoDateTime(Date(deadlineDateMillis), time)
        val eventDateTime = esmorgaDateTimeFormatter.formatIsoDateTime(Date(eventDateMillis), eventTime)
        return if (deadlineDateTime > eventDateTime) R.string.inline_error_event_date_deadline_exceeded else null
    }

    private fun computeDeadlineValid(isToggleOn: Boolean, deadlineErrorRes: Int?): Boolean {
        return if (isToggleOn) deadlineTime.isNotEmpty() && deadlineErrorRes == null else true
    }

    fun formattedTime(hour: Int, minute: Int): String {
        return esmorgaDateTimeFormatter.formatTimeWithMillisUtcSuffix(hour, minute)
    }

    fun onNextClick(date: Date, time: String, deadlineDateMillis: Long?, deadlineTimeArg: String) {
        val dateTime = esmorgaDateTimeFormatter.formatIsoDateTime(date, time)
        val joinDeadline = if (_uiState.value.isDeadlineToggleOn && deadlineTimeArg.isNotEmpty() && deadlineDateMillis != null) {
            esmorgaDateTimeFormatter.formatIsoDateTime(Date(deadlineDateMillis), deadlineTimeArg)
        } else {
            null
        }
        val updatedForm = eventForm.copy(date = dateTime, joinDeadline = joinDeadline)
        _effect.tryEmit(CreateEventFormDateEffect.NavigateNext(updatedForm))
    }
}
