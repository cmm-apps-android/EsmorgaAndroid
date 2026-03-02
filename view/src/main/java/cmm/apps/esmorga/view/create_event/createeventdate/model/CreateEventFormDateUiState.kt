package cmm.apps.esmorga.view.create_event.createeventdate.model

import cmm.apps.esmorga.domain.event.model.CreateEventForm

data class CreateEventFormDateUiState(
    val dateTime: String = "",
    val isButtonEnabled: Boolean = false
)

sealed class CreateEventFormDateEffect {
    data class NavigateNext(val eventForm: CreateEventForm) : CreateEventFormDateEffect()
    data object NavigateBack : CreateEventFormDateEffect()

}