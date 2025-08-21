package cmm.apps.esmorga.view.createeventtype.model

import cmm.apps.esmorga.domain.event.model.EventType


data class CreateEventTypeScreenUiState(
    val selectedEventType: EventType = EventType.PARTY
)

sealed class CreateEventTypeScreenEffect {
    object NavigateBack : CreateEventTypeScreenEffect()
    data class NavigateNext(
        val eventName: String,
        val description: String,
        val eventType: String
    ) : CreateEventTypeScreenEffect()
}