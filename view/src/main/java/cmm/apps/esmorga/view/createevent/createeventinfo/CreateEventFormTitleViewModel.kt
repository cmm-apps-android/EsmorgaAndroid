package cmm.apps.esmorga.view.createevent.createeventinfo

import androidx.lifecycle.ViewModel
import cmm.apps.esmorga.view.createevent.CreateEventFlowViewModel
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

class CreateEventFormTitleViewModel(
    private val createEventFlowViewModel: CreateEventFlowViewModel
) : ViewModel() {
    companion object {
        private const val EVENT_NAME_MIN_LENGTH = 3
        private const val EVENT_NAME_MAX_LENGTH = 100
        private const val DESCRIPTION_MIN_LENGTH = 20
        private const val DESCRIPTION_MAX_LENGTH = 5000
    }

    private val _uiState = MutableStateFlow(CreateEventFormUiState(eventName = "", eventDescription = null))
    val uiState: StateFlow<CreateEventFormUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<CreateEventFormEffect>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val effect: SharedFlow<CreateEventFormEffect> = _effect.asSharedFlow()


    fun onEventNameChange(newValue: String) {
        updateFormState(eventName = newValue)
    }

    fun onDescriptionChange(newValue: String) {
        updateFormState(eventDescription = newValue.ifEmpty { null })
    }

    fun onBackClick() {
        _effect.tryEmit(CreateEventFormEffect.NavigateBack)
    }

    fun onNextClick() {
        val state = _uiState.value
        if (state.isFormValid) {
            createEventFlowViewModel.updateTitle(
                name = state.eventName,
                description = state.eventDescription
            )
            _effect.tryEmit(
                CreateEventFormEffect.NavigateNext
            )
        }
    }

    private fun updateFormState(
        eventName: String = _uiState.value.eventName,
        eventDescription: String? = _uiState.value.eventDescription
    ) {
        val eventNameError = validateEventName(eventName)
        val descriptionError = validateEventDescription(eventDescription)

        _uiState.update {
            it.copy(
                eventName = eventName,
                eventDescription = eventDescription,
                eventNameError = eventNameError,
                descriptionError = descriptionError,
                isFormValid = eventNameError == null && descriptionError == null
            )
        }
    }

    private fun validateEventName(name: String): Int? = when {
        name.isBlank() -> R.string.inline_error_empty_field
        name.length !in EVENT_NAME_MIN_LENGTH..EVENT_NAME_MAX_LENGTH -> R.string.inline_error_invalid_length_name
        else -> null
    }

    private fun validateEventDescription(description: String?): Int? = when {
        description.isNullOrBlank() -> null
        description.length !in DESCRIPTION_MIN_LENGTH..DESCRIPTION_MAX_LENGTH -> R.string.inline_error_invalid_length_description
        else -> null
    }
}
