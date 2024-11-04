package cmm.apps.esmorga.view.eventlist.model

import cmm.apps.esmorga.domain.event.model.Event

data class MyEventListUiState(
    val loading: Boolean = false,
    val eventList: List<EventListUiModel> = emptyList(),
    val error: MyEventListError? = null
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