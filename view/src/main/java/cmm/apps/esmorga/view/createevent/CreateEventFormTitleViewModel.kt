package cmm.apps.esmorga.view.createevent

import androidx.lifecycle.ViewModel
import cmm.apps.esmorga.domain.event.model.CreateEventForm
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.createevent.model.CreateEventFormEffect
import cmm.apps.esmorga.view.createevent.model.CreateEventFormUiState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CreateEventFormTitleViewModel() : ViewModel() {
    companion object {
        private const val EVENT_NAME_MIN_LENGTH = 3
        private const val EVENT_NAME_MAX_LENGTH = 100
        private const val DESCRIPTION_MIN_LENGTH = 4
        private const val DESCRIPTION_MAX_LENGTH = 5000
    }

    var eventName = ""
    var eventDescription = ""

    private val _uiState = MutableStateFlow(CreateEventFormUiState(eventName = eventName,eventDescription = eventDescription))
    val uiState: StateFlow<CreateEventFormUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<CreateEventFormEffect>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val effect: SharedFlow<CreateEventFormEffect> = _effect.asSharedFlow()

    fun onEventNameChange(newValue: String) {
        _uiState.update { it.copy(eventName = newValue) }
        eventName = newValue
        validateEventName()
    }

    fun onDescriptionChange(newValue: String) {
        _uiState.update { it.copy(eventDescription = newValue) }
        eventDescription = newValue
        validateEventDescription()

    }


    fun onBackClick() {
        _effect.tryEmit(CreateEventFormEffect.NavigateBack)
    }

    fun onNextClick() {
        if (_uiState.value.isFormValid) {
            _effect.tryEmit(CreateEventFormEffect.NavigateNext(eventForm = CreateEventForm(name = eventName, description = eventDescription)))
        }
    }

    private fun validateEventName(){
        val state = _uiState.value
        val name = state.eventName

        val nameError = when {
            name.isBlank() -> R.string.inline_error_empty_field
            name.length < EVENT_NAME_MIN_LENGTH || name.length > EVENT_NAME_MAX_LENGTH -> R.string.inline_error_invalid_length_name
            else -> null
        }

        val isValid = nameError == null && eventDescription.isNotBlank()
        _uiState.update {
            it.copy(
                eventNameError = nameError,
                isFormValid = isValid
            )
        }
    }

    private fun validateEventDescription(){
        val state = _uiState.value
        val description = state.eventDescription

        val descriptionError = when {
            description.isBlank() -> R.string.inline_error_empty_field
            description.length < DESCRIPTION_MIN_LENGTH || description.length > DESCRIPTION_MAX_LENGTH -> R.string.inline_error_invalid_length_description
            else -> null
        }

        val isValid = descriptionError == null && eventName.isNotBlank()
        _uiState.update {
            it.copy(
                descriptionError = descriptionError,
                isFormValid = isValid
            )
        }
    }
}