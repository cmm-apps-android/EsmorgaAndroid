package cmm.apps.esmorga.view.eventlist.model


data class EventListUiState(
    val loading: Boolean = false,
    val eventList: List<EventUiModel> = emptyList(),
    val error: String? = null
)

data class EventUiModel(
    val imageUrl: String?,
    val cardTitle: String,
    val cardSubtitle1: String,
    val cardSubtitle2: String
)

sealed class EventListEffect {
    data object ShowNoNetworkPrompt : EventListEffect()
}
