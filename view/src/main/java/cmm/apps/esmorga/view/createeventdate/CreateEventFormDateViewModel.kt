package cmm.apps.esmorga.view.createeventdate

import androidx.lifecycle.ViewModel
import cmm.apps.esmorga.domain.event.model.CreateEventForm
import cmm.apps.esmorga.view.createeventdate.model.CreateEventFormDateEffect
import cmm.apps.esmorga.view.createeventdate.model.CreateEventFormDateUiState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

class CreateEventFormDateViewModel(
    private val eventForm: CreateEventForm
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateEventFormDateUiState())
    val uiState: StateFlow<CreateEventFormDateUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<CreateEventFormDateEffect>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val effect: SharedFlow<CreateEventFormDateEffect> = _effect.asSharedFlow()

    fun onBackClick() {
        _effect.tryEmit(CreateEventFormDateEffect.NavigateBack)
    }

    fun isTimeSelected(selectedTime: String) {
        if (selectedTime.isNotEmpty()) {
            _uiState.value = _uiState.value.copy(
                isButtonEnabled = true
            )
        }
    }

    fun formattedTime(hour: Int, minute: Int): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS'Z'")
        val time = LocalTime.of(hour, minute).format(formatter)
        return time
    }

    private fun formattedDateTime(date: Date, time: String) {
        val instant: Instant = date.toInstant()
        val systemZone: ZoneId = ZoneId.systemDefault()
        val localDateTimeFromDate: LocalDateTime = instant.atZone(systemZone).toLocalDateTime()

        val datePartFormatter = DateTimeFormatter.ISO_LOCAL_DATE
        val datePart = localDateTimeFromDate.format(datePartFormatter)

        val dateTime = "${datePart}T${time}"

        _uiState.value = _uiState.value.copy(
            dateTime = dateTime
        )
    }

    fun onNextClick(date: Date, time: String) {
        formattedDateTime(date, time)
        val updatedForm = eventForm.copy(date = _uiState.value.dateTime)
        _effect.tryEmit(CreateEventFormDateEffect.NavigateNext(updatedForm))
    }
}