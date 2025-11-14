package cmm.apps.esmorga.view.explore.model

import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.domain.poll.model.Poll

data class ExploreUiState(
    val loading: Boolean = false,
    val eventList: List<ListCardUiModel> = emptyList(),
    val pollList: List<ListCardUiModel> = emptyList(),
    val error: String? = null
)

data class ListCardUiModel(
    val id: String,
    val imageUrl: String?,
    val cardTitle: String,
    val cardSubtitle1: String,
    val cardSubtitle2: String? = null
)

sealed class ExploreEffect {
    data object ShowNoNetworkPrompt : ExploreEffect()
    data class NavigateToEventDetail(val event: Event) : ExploreEffect()
    data class NavigateToPollDetail(val poll: Poll) : ExploreEffect()
}
