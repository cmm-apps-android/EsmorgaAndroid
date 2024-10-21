package cmm.apps.esmorga.view.eventlist.model

import cmm.apps.esmorga.domain.event.model.EventType
import kotlinx.serialization.Serializable

data class EventListUiState(
    val loading: Boolean = false,
    val eventList: List<EventListUiModel> = emptyList(),
    val error: String? = null
)

@Serializable
data class EventListUiModel(
    val id: String,
    val name: String,
    val date: String,
    val dateFormatted: String,
    val description: String,
    val type: EventType,
    val imageUrl: String? = null,
    val location: EventUILocation,
    val tags: List<String> = listOf(),
    val userJoined: Boolean
)

@Serializable
data class EventUILocation(
    val name: String,
    val lat: Double? = null,
    val long: Double? = null
)

sealed class EventListEffect {
    data object ShowNoNetworkPrompt : EventListEffect()
    data class NavigateToEventDetail(val event: EventListUiModel) : EventListEffect()
}
