package cmm.apps.esmorga.view.createeventtype.model

import cmm.apps.esmorga.domain.event.model.EventType
import cmm.apps.esmorga.view.R

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

object EventTypeHelper {
    fun getUiTextRes(type: EventType): Int = when(type) {
        EventType.PARTY -> R.string.step_2_option_party
        EventType.SPORT -> R.string.step_2_option_sport
        EventType.FOOD -> R.string.step_2_option_food
        EventType.CHARITY -> R.string.step_2_option_charity
        EventType.GAMES -> R.string.step_2_option_games
    }
}