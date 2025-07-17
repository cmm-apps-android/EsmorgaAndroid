package cmm.apps.esmorga.view.eventlist.model

import cmm.apps.esmorga.domain.event.model.Event
import org.koin.core.component.KoinComponent

data class MyEventListUiState(
    val loading: Boolean = false,
    val eventList: List<EventListUiModel> = emptyList(),
    val error: MyEventListError? = null,
    val isAdmin: Boolean = false,
)

enum class MyEventListError {
    NOT_LOGGED_IN,
    EMPTY_LIST,
    UNKNOWN
}

object MyEventListUiStateHelper : KoinComponent {
    fun isAdmin(role: String?): Boolean {
        return role?.uppercase() == "ADMIN"
    }
}

sealed class MyEventListEffect {
    data object ShowNoNetworkPrompt : MyEventListEffect()
    data class NavigateToEventDetail(val event: Event) : MyEventListEffect()
    data object NavigateToSignIn : MyEventListEffect()
}