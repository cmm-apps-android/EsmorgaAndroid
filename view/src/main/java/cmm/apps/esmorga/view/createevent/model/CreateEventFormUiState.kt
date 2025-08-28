package cmm.apps.esmorga.view.createevent.model

import cmm.apps.esmorga.domain.event.model.CreateEventForm

data class CreateEventFormUiState(
    val eventName: String,
    val eventDescription: String,
    val eventNameError: Int? = null,
    val descriptionError: Int? = null,
    val isFormValid: Boolean = false
)

sealed class CreateEventFormEffect {
    data class NavigateNext(val eventForm: CreateEventForm) : CreateEventFormEffect()
    data object NavigateBack : CreateEventFormEffect()
}