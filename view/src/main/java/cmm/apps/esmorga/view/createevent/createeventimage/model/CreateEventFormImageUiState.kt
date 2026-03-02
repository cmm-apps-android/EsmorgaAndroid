package cmm.apps.esmorga.view.createevent.createeventimage.model

import cmm.apps.esmorga.domain.event.model.CreateEventForm

data class CreateEventFormImageUiState(
    val imageUrl: String = "",
    val showPreview: Boolean = false,
    val isLoading: Boolean = false,
    val imageError: Int? = null,
)

sealed class CreateEventFormImageEffect {
    data class NavigateNext(val eventForm: CreateEventForm) : CreateEventFormImageEffect()
    data object NavigateBack : CreateEventFormImageEffect()
}
