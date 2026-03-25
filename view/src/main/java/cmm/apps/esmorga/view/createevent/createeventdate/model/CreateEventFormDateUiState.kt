package cmm.apps.esmorga.view.createevent.createeventdate.model

import cmm.apps.esmorga.domain.event.model.CreateEventForm

data class CreateEventFormDateUiState(
    val isButtonEnabled: Boolean = false,
    val isDeadlineToggleOn: Boolean = false,
    val deadlineErrorRes: Int? = null
)

sealed class CreateEventFormDateEffect {
    data class NavigateNext(val eventForm: CreateEventForm) : CreateEventFormDateEffect()
    data object NavigateBack : CreateEventFormDateEffect()
}
