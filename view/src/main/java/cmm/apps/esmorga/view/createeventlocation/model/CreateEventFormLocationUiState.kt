package cmm.apps.esmorga.view.createeventlocation.model

import cmm.apps.esmorga.domain.event.model.CreateEventForm

data class CreateEventFormLocationUiState(
    val localizationName: String = "",
    val localizationCoordinates: String = "",
    val eventMaxCapacity: String = "",
    val isButtonEnabled: Boolean = false

)

sealed class CreateEventFormLocationEffect{
    data class NavigateNext(val eventForm: CreateEventForm) : CreateEventFormLocationEffect()
    data object NavigateBack : CreateEventFormLocationEffect()
}