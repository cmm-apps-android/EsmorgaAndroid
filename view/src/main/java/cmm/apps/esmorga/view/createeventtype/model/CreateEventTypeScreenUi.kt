package cmm.apps.esmorga.view.createeventtype.model

import cmm.apps.esmorga.view.createeventtype.EventType

data class CreateEventTypeScrrenUi(
    val eventName: String,
    val description: String,
    val selectedEventType: EventType? = null
)

sealed class CreateEventTypeScrrenEffect {
    object NavigateBack : CreateEventTypeScrrenEffect()
    data class NavigateNext(
        val eventName: String,
        val description: String,
        val eventType: String?
    ) : CreateEventTypeScrrenEffect()
}