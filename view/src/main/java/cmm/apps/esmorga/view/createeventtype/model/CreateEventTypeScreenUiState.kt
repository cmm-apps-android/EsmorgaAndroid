package cmm.apps.esmorga.view.createeventtype.model

import cmm.apps.esmorga.view.createeventtype.EventType

data class CreateEventTypeScreenUiState(
    val selectedEventType: EventType? = null
)

sealed class CreateEventTypeScreenEffect {
    object NavigateBack : CreateEventTypeScreenEffect()
    data class NavigateNext(
        val eventName: String,
        val description: String,
        val eventType: String
    ) : CreateEventTypeScreenEffect()
}