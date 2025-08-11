package cmm.apps.esmorga.view.createevent

import androidx.lifecycle.ViewModel
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.createevent.model.CreateEventStep1Effect
import cmm.apps.esmorga.view.createevent.model.CreateEventStep1UiState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

class CreateEventStep1ViewModel() : ViewModel() {

    private val _uiState = MutableStateFlow(CreateEventStep1UiState())
    val uiState: StateFlow<CreateEventStep1UiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<CreateEventStep1Effect>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val effect: SharedFlow<CreateEventStep1Effect> = _effect.asSharedFlow()

    fun onEventNameChange(newValue: String) {
        _uiState.update { it.copy(eventName = newValue) }
        validateForm()
    }

    fun onDescriptionChange(newValue: String) {
        _uiState.update { it.copy(description = newValue) }
        validateForm()
    }

    fun onBackClick() {
        _effect.tryEmit(CreateEventStep1Effect.NavigateBack)
    }

    fun onNextClick() {
        validateForm(finalValidation = true)
        if (_uiState.value.isFormValid) {
            _effect.tryEmit(CreateEventStep1Effect.NavigateToStep2)
        }
    }

    private fun validateForm(finalValidation: Boolean = false) {
        val state = _uiState.value

        val nameError = when {
            state.eventName.isBlank() -> R.string.inline_error_empty_field
            state.eventName.length < 3 || state.eventName.length > 100 -> R.string.inline_error_invalid_length_name
            else -> null
        }

        val descriptionError = when {
            state.description.isBlank() -> R.string.inline_error_empty_field
            state.description.length < 4 || state.description.length > 5000 -> R.string.inline_error_invalid_length_description
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
