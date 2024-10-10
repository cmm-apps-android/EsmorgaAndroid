package cmm.apps.esmorga.view.eventlist.model

data class MyEventListUiState(
    val loading: Boolean = false,
    val eventList: List<EventListUiModel> = emptyList(),
    val error: MyEventListError? = null
)

enum class MyEventListError {
    NOT_LOGGED_IN,
    NO_CONNECTION,
    EMPTY_LIST,
    UNKNOWN
}

sealed class MyEventListEffect {
    data object ShowNoNetworkPrompt : MyEventListEffect()
    data class NavigateToEventDetail(val eventId: String) : MyEventListEffect()
    data object NavigateToSignIn : MyEventListEffect()
}