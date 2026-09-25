package cmm.apps.esmorga.view.createevent.createeventinfo

import androidx.lifecycle.ViewModel
import cmm.apps.esmorga.domain.event.model.CreateEventForm
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.createevent.createeventinfo.model.CreateEventFormEffect
import cmm.apps.esmorga.view.createevent.createeventinfo.model.CreateEventFormUiState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CreateEventFormTitleViewModel : ViewModel() {
    companion object {
        private const val EVENT_NAME_MIN_LENGTH = 3
        private const val EVENT_NAME_MAX_LENGTH = 100
        private const val DESCRIPTION_MIN_LENGTH = 20
        private const val DESCRIPTION_MAX_LENGTH = 5000
    }

    var eventName = ""
    var eventDescription: String? = null

    private val _uiState = MutableStateFlow(CreateEventFormUiState(eventName = eventName, eventDescription = eventDescription))
    val uiState: StateFlow<CreateEventFormUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<CreateEventFormEffect>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val effect: SharedFlow<CreateEventFormEffect> = _effect.asSharedFlow()

    fun onEventNameChange(newValue: String) {
        eventName = newValue
        validateEventName()
    }

    fun onDescriptionChange(newValue: String) {
        eventDescription = newValue.ifEmpty { null }
        validateEventDescription()
    }

    fun onBackClick() {
        _effect.tryEmit(CreateEventFormEffect.NavigateBack)
    }

    fun onNextClick() {
        if (_uiState.value.isFormValid) {
            _effect.tryEmit(
                CreateEventFormEffect.NavigateNext(
                    eventForm = CreateEventForm(
                        name = eventName,
                        description = eventDescription?.takeIf { it.isNotBlank() }
                    )
                )
            )
        }
    }

    private fun validateEventName() {
        val name = eventName

        val nameError = when {
            name.isBlank() -> R.string.inline_error_empty_field
            name.length !in EVENT_NAME_MIN_LENGTH..EVENT_NAME_MAX_LENGTH -> R.string.inline_error_invalid_length_name
            else -> null
        }

        val isValid = nameError == null && _uiState.value.descriptionError == null
        _uiState.update {
            it.copy(
                eventName = name,
                eventNameError = nameError,
                isFormValid = isValid
            )
        }
    }

    private fun validateEventDescription() {
        val description = eventDescription

        val descriptionError = when {
            description.isNullOrBlank() -> null
            description.length !in DESCRIPTION_MIN_LENGTH..DESCRIPTION_MAX_LENGTH -> R.string.inline_error_invalid_length_description
            else -> null
        }

        val name = eventName
        val isNameValid = name.isNotBlank() && name.length >= EVENT_NAME_MIN_LENGTH && name.length <= EVENT_NAME_MAX_LENGTH
        val isValid = descriptionError == null && isNameValid
        _uiState.update {
            it.copy(
                eventDescription = description,
                descriptionError = descriptionError,
                isFormValid = isValid
            )
        }
    }
}
