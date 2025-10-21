package cmm.apps.esmorga.view.eventdetails.model

import android.content.Context
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.dateformatting.EsmorgaDateTimeFormatter
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
    val joinDeadline: Long? = null,
    val isJoinDeadlinePassed: Boolean = false
)

object EventDetailsUiStateHelper : KoinComponent {
    val context: Context by inject()
    private val dateFormatter: EsmorgaDateTimeFormatter by inject()
    fun getPrimaryButtonTitle(
        isAuthenticated: Boolean,
        userJoined: Boolean,
        eventFull: Boolean,
        isDeadlinePassed: Boolean
    ): String {
        return when {
            !isAuthenticated -> context.getString(R.string.button_login_to_join)
            userJoined -> context.getString(R.string.button_leave_event)
            isDeadlinePassed -> context.getString(R.string.button_join_event_closed)
            !userJoined && eventFull -> context.getString(R.string.button_join_event_disabled)
            else -> context.getString(R.string.button_join_event)
        }
    }

    fun getButtonEnableStatus(
        eventFull: Boolean,
        userJoined: Boolean,
        isDeadlinePassed: Boolean,
        isAuthenticated: Boolean
    ): Boolean {
        if (!isAuthenticated) return true
        return userJoined || (!eventFull && !isDeadlinePassed)
    }

    fun getEsmorgaNoNetworkScreenArguments() = EsmorgaErrorScreenArguments(
        animation = R.raw.no_connection_anim,
        title = context.getString(R.string.screen_no_connection_title),
        subtitle = context.getString(R.string.screen_no_connection_body),
        buttonText = context.getString(R.string.button_ok)
    )

    fun hasJoinDeadlinePassed(joinDeadline: Long?): Boolean {
        return joinDeadline?.let { System.currentTimeMillis() > it } ?: false
    }

    fun formatJoinDeadline(joinDeadline: Long?): String {
        return joinDeadline?.let { dateFormatter.formatEventDate(it) } ?: ""
    }
}

sealed class EventDetailsEffect {
    data object NavigateBack : EventDetailsEffect()
    data object NavigateToLoginScreen : EventDetailsEffect()
    data object ShowJoinEventSuccess : EventDetailsEffect()
    data object ShowLeaveEventSuccess : EventDetailsEffect()
    data object ShowFullEventError : EventDetailsEffect()
    data class ShowNoNetworkError(val esmorgaNoNetworkArguments: EsmorgaErrorScreenArguments = getEsmorgaNoNetworkScreenArguments()) : EventDetailsEffect()
    data class NavigateToLocation(val lat: Double, val lng: Double, val locationName: String) : EventDetailsEffect()
    data class ShowFullScreenError(val esmorgaErrorScreenArguments: EsmorgaErrorScreenArguments = getEsmorgaDefaultErrorScreenArguments()) : EventDetailsEffect()
}
