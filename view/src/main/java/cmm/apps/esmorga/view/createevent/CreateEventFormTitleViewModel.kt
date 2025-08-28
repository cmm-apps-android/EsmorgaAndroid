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

    fun onEventNameChange(name: String) {
        eventName = name
        val error = if (name.length < 3) R.string.inline_error_invalid_length_name else null
        _uiState.value = _uiState.value.copy(
            eventName = eventName,
            eventDescription = eventDescription,
            eventNameError = error,
            isFormValid = error == null && _uiState.value.descriptionError == null
        )
    }

    fun onDescriptionChange(description: String) {
        eventDescription = description
        val error = if (description.isBlank()) R.string.inline_error_invalid_length_description else null
        _uiState.value = _uiState.value.copy(
            eventName = eventName,
            eventDescription = eventDescription,
            descriptionError = error,
            isFormValid = error == null && _uiState.value.eventNameError == null
        )
    }


    fun onBackClick() {
        _effect.tryEmit(CreateEventFormEffect.NavigateBack)
    }

    fun onNextClick() {
        validateForm(finalValidation = true)
        if (_uiState.value.isFormValid) {
            _effect.tryEmit(CreateEventFormEffect.NavigateNext(eventForm = CreateEventForm(name = eventName, description = eventDescription)))
        }
    }

    private fun validateForm(finalValidation: Boolean = false) {
        val state = _uiState.value
        val name = state.eventName
        val description = state.eventDescription

        val nameError = when {
            name.isBlank() -> R.string.inline_error_empty_field
            name.length < EVENT_NAME_MIN_LENGTH || name.length > EVENT_NAME_MAX_LENGTH -> R.string.inline_error_invalid_length_name
            else -> null
        }

        val descriptionError = when {
            description.isBlank() -> R.string.inline_error_empty_field
            description.length < DESCRIPTION_MIN_LENGTH || description.length > DESCRIPTION_MAX_LENGTH -> R.string.inline_error_invalid_length_description
            else -> null
        }

        val isValid = nameError == null && descriptionError == null

        _uiState.update {
            it.copy(
                eventNameError = nameError,
                descriptionError = descriptionError,
                isFormValid = isValid
            )
        }
    }
}