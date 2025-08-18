package cmm.apps.esmorga.view.createevent

import androidx.lifecycle.ViewModel
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.createevent.model.CreateEventFormEffect
import cmm.apps.esmorga.view.createevent.model.CreateEventFormUiState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

class CreateEventFormViewModel() : ViewModel() {
    companion object {
        private const val EVENT_NAME_MIN_LENGTH = 3
        private const val EVENT_NAME_MAX_LENGTH = 100
        private const val DESCRIPTION_MIN_LENGTH = 4
        private const val DESCRIPTION_MAX_LENGTH = 5000
    }


    private val _uiState = MutableStateFlow(CreateEventFormUiState())
    val uiState: StateFlow<CreateEventFormUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<CreateEventFormEffect>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val effect: SharedFlow<CreateEventFormEffect> = _effect.asSharedFlow()

    fun onEventNameChange(newValue: String) {
        _uiState.update { it.copy(eventName = newValue) }
        validateForm()
    }

    fun onDescriptionChange(newValue: String) {
        _uiState.update { it.copy(description = newValue) }
        validateForm()
    }

    fun onBackClick() {
        _effect.tryEmit(CreateEventFormEffect.NavigateBack)
    }

    fun onNextClick() {
        validateForm(finalValidation = true)
        if (_uiState.value.isFormValid) {
            _effect.tryEmit(CreateEventFormEffect.NavigateEventType)
        }
    }

    private fun validateForm(finalValidation: Boolean = false) {
        val state = _uiState.value

        val nameError = when {
            state.eventName.isBlank() -> R.string.inline_error_empty_field
            state.eventName.length < EVENT_NAME_MIN_LENGTH || state.eventName.length > EVENT_NAME_MAX_LENGTH -> R.string.inline_error_invalid_length_name
            else -> null
        }

        val descriptionError = when {
            state.description.isBlank() -> R.string.inline_error_empty_field
            state.description.length < DESCRIPTION_MIN_LENGTH || state.description.length > DESCRIPTION_MAX_LENGTH -> R.string.inline_error_invalid_length_description
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
