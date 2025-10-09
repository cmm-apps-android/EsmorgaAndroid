package cmm.apps.esmorga.view.createeventdate

import androidx.lifecycle.ViewModel
import cmm.apps.esmorga.domain.event.model.CreateEventForm
import cmm.apps.esmorga.view.createeventdate.model.CreateEventFormDateEffect
import cmm.apps.esmorga.view.createeventdate.model.CreateEventFormDateUiState
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


    fun onBackClick() {
        _effect.tryEmit(CreateEventFormDateEffect.NavigateBack)
    }

    fun onTimeSelected(selectedTime: String) {
        if (selectedTime.isNotEmpty()) {
            _uiState.value = _uiState.value.copy(
                isButtonEnabled = true
            )
        }
    }

    fun formattedTime(hour: Int, minute: Int): String {
        return esmorgaDateTimeFormatter.formatTimeWithMillisUtcSuffix(hour, minute)
    }

    private fun formattedDateTime(date: Date, time: String) {
        val dateTime = esmorgaDateTimeFormatter.formatIsoDateTime(date, time)
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