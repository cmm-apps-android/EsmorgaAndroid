package cmm.apps.esmorga.view.eventdetails.model

import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArguments
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArgumentsHelper.getEsmorgaDefaultErrorScreenArguments
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArgumentsHelper.getEsmorgaNoNetworkScreenArguments

data class EventDetailsUiState(
    val id: String = "",
    val title: String = "",
    val date: String = "",
    val description: String = "",
    val image: String? = null,
    val locationName: String = "",
    val showNavigateButton: Boolean = false,
    val primaryButtonTitle: String = "",
    val isPrimaryButtonLoading: Boolean = false,
    val isPrimaryButtonEnabled: Boolean = true,
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
