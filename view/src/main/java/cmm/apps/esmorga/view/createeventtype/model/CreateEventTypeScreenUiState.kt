package cmm.apps.esmorga.view.createeventtype.model

import android.content.Context
import cmm.apps.esmorga.domain.event.model.EventType
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.createevent.model.CreateEventFormUiModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

data class CreateEventTypeScreenUiState(
    val type: EventType? = null,
)

sealed class CreateEventTypeScreenEffect {
    object NavigateBack : CreateEventTypeScreenEffect()
    data class NavigateNext(val eventForm: CreateEventFormUiModel) : CreateEventTypeScreenEffect()
}

object EventTypeHelper : KoinComponent {
    private val context: Context by inject()

    fun getUiTextRes(type: EventType): String = when (type) {
        EventType.PARTY -> context.getString(R.string.step_2_option_party)
        EventType.SPORT -> context.getString(R.string.step_2_option_sport)
        EventType.FOOD -> context.getString(R.string.step_2_option_food)
        EventType.CHARITY -> context.getString(R.string.step_2_option_charity)
        EventType.GAMES -> context.getString(R.string.step_2_option_games)
    }
}