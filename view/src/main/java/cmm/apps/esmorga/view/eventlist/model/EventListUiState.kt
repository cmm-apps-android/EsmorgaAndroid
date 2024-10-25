package cmm.apps.esmorga.view.eventlist.model

import cmm.apps.esmorga.domain.event.model.Event
import kotlinx.serialization.Serializable

data class EventListUiState(
    val loading: Boolean = false,
    val eventList: List<EventListUiModel> = emptyList(),
    val error: String? = null
)

@Serializable
data class EventListUiModel(
    val id: String,
    val imageUrl: String?,
    val cardTitle: String,
    val cardSubtitle1: String,
    val cardSubtitle2: String
)

sealed class EventListEffect {
    data object ShowNoNetworkPrompt : EventListEffect()
    data class NavigateToEventDetail(val event: Event) : EventListEffect()
}
