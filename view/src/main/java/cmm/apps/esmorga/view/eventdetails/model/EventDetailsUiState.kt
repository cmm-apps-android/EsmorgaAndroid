package cmm.apps.esmorga.view.eventdetails.model

import android.content.Context
import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArguments
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArgumentsHelper.getEsmorgaDefaultErrorScreenArguments
import cmm.apps.esmorga.view.eventdetails.model.EventDetailsUiStateHelper.getEsmorgaNoNetworkScreenArguments
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

data class EventDetailsUiState(
    val id: String = "",
    val title: String = "",
    val subtitle: String = "",
    val description: String = "",
    val image: String? = null,
    val locationName: String = "",
    val locationLat: Double? = null,
    val locationLng: Double? = null,
    val navigateButton: Boolean = locationLat != null && locationLng != null,
    val primaryButtonTitle: String = "",
    val primaryButtonLoading: Boolean = false,
    val currentAttendeeCount: Int = 0,
    val maxCapacity: Int? = null,
    val isJoinButtonEnabled: Boolean = true,
    val isEventFull: Boolean = false,
    val showViewAttendeesButton: Boolean = false
)

object EventDetailsUiStateHelper : KoinComponent {
    val context: Context by inject()
    fun getPrimaryButtonTitle(
        isAuthenticated: Boolean,
        userJoined: Boolean,
        eventFull: Boolean
    ): String {
        return when {
            !isAuthenticated -> context.getString(R.string.button_login_to_join)
            userJoined -> context.getString(R.string.button_leave_event)
            !userJoined && eventFull -> context.getString(R.string.button_join_event_disabled)
            else -> context.getString(R.string.button_join_event)
        }
    }

    fun getButtonEnableStatus(
        eventFull: Boolean,
        userJoined: Boolean
    ): Boolean{
        return userJoined || !eventFull
    }

    fun getEsmorgaNoNetworkScreenArguments() = EsmorgaErrorScreenArguments(
        animation = R.raw.no_connection_anim,
        title = context.getString(R.string.screen_no_connection_title),
        subtitle = context.getString(R.string.screen_no_connection_body),
        buttonText = context.getString(R.string.button_ok)
    )
}

sealed class EventDetailsEffect {
    data object NavigateBack : EventDetailsEffect()
    data object NavigateToLoginScreen : EventDetailsEffect()
    data object ShowJoinEventSuccess : EventDetailsEffect()
    data object ShowLeaveEventSuccess : EventDetailsEffect()
    data class NavigateToAttendeesScreen(val event: Event) : EventDetailsEffect()
    data class ShowNoNetworkError(val esmorgaNoNetworkArguments: EsmorgaErrorScreenArguments = getEsmorgaNoNetworkScreenArguments()) : EventDetailsEffect()
    data class NavigateToLocation(val lat: Double, val lng: Double, val locationName: String) : EventDetailsEffect()
    data class ShowFullScreenError(val esmorgaErrorScreenArguments: EsmorgaErrorScreenArguments = getEsmorgaDefaultErrorScreenArguments()) : EventDetailsEffect()
}
