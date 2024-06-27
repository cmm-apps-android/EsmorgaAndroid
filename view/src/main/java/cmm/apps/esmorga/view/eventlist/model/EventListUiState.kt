package cmm.apps.esmorga.view.eventlist.model

import java.io.Serializable


data class EventListUiState(
    val loading: Boolean = false,
    val eventList: List<EventListUiModel> = emptyList(),
    val error: String? = null
)

data class EventListUiModel(
    val id: String,
    val imageUrl: String?,
    val cardTitle: String,
    val cardSubtitle1: String,
    val cardSubtitle2: String
) : Serializable

sealed class EventListEffect {
    data object ShowNoNetworkPrompt : EventListEffect()
    data class NavigateToEventDetail(val eventId: String) : EventListEffect()
}
