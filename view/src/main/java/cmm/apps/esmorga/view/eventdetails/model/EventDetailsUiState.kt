package cmm.apps.esmorga.view.eventdetails.model

import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArguments
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArgumentsHelper.getEsmorgaDefaultErrorScreenArguments
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArgumentsHelper.getEsmorgaNoNetworkScreenArguments

sealed class ButtonUiState()
data class Enabled(val text: String) : ButtonUiState()
data class Disabled(val text: String) : ButtonUiState()
data object Loading : ButtonUiState()

data class EventDetailsUiState(
    val id: String = "",
    val title: String = "",
    val date: String = "",
    val description: String = "",
    val image: String? = null,
    val locationName: String = "",
    val showNavigateButton: Boolean = false,
    val primaryButtonState: ButtonUiState = Disabled(""),
    val currentAttendeeCountText: String? = null,
    val joinDeadline: String = "",
    val showViewAttendeesButton: Boolean = false
)

sealed class EventDetailsEffect {
    data object NavigateBack : EventDetailsEffect()
    data object NavigateToLoginScreen : EventDetailsEffect()
    data object ShowJoinEventSuccess : EventDetailsEffect()
    data object ShowLeaveEventSuccess : EventDetailsEffect()
    data object ShowEventFullError : EventDetailsEffect()
    data class NavigateToAttendeesScreen(val event: Event) : EventDetailsEffect()
    data class ShowNoNetworkError(val esmorgaNoNetworkArguments: EsmorgaErrorScreenArguments = getEsmorgaNoNetworkScreenArguments()) : EventDetailsEffect()
    data class NavigateToLocation(val lat: Double, val lng: Double, val locationName: String) : EventDetailsEffect()
    data class ShowFullScreenError(val esmorgaErrorScreenArguments: EsmorgaErrorScreenArguments = getEsmorgaDefaultErrorScreenArguments()) : EventDetailsEffect()
}
