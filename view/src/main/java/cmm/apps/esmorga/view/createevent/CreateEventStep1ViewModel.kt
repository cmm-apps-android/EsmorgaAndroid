package cmm.apps.esmorga.view.createevent

import androidx.lifecycle.ViewModel
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
        val result = validateCreateEventForm(
            name = _uiState.value.eventName,
            description = _uiState.value.description,
            finalValidation = true
        )

        _uiState.update {
            it.copy(
                eventNameError = result.nameErrorRes,
                descriptionError = result.descriptionErrorRes,
                isFormValid = result.isFormValid
            )
        }

        if (result.isFormValid) {
            _effect.tryEmit(CreateEventStep1Effect.NavigateToStep2)
        }
    }

    private fun validateForm() {
        val result = validateCreateEventForm(
            name = _uiState.value.eventName,
            description = _uiState.value.description,
            finalValidation = false
        )

        _uiState.update {
            it.copy(
                eventNameError = result.nameErrorRes,
                descriptionError = result.descriptionErrorRes,
                isFormValid = result.isFormValid
            )
        }
    }
}
