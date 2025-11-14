package cmm.apps.esmorga.view.myeventlist.model

import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.view.explore.model.ListCardUiModel

data class MyEventListUiState(
    val loading: Boolean = false,
    val eventList: List<ListCardUiModel> = emptyList(),
    val error: MyEventListError? = null,
    val isAdmin: Boolean = false,
)

enum class MyEventListError {
    NOT_LOGGED_IN,
    EMPTY_LIST,
    UNKNOWN
}

sealed class MyEventListEffect {
    data object ShowNoNetworkPrompt : MyEventListEffect()
    data class NavigateToEventDetail(val event: Event) : MyEventListEffect()
    data object NavigateToSignIn : MyEventListEffect()
}