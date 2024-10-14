package cmm.apps.esmorga.view.eventdetails.model

import android.content.Context
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArguments
import cmm.apps.esmorga.view.eventdetails.model.EventDetailsUiStateHelper.getEsmorgaNoNetworkScreenArguments
import cmm.apps.esmorga.view.login.model.LoginViewHelper.getEsmorgaErrorScreenArguments
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
    val primaryButtonLoading: Boolean = false
)

object EventDetailsUiStateHelper : KoinComponent {
    private val context: Context by inject()
    fun getPrimaryButtonTitle(isAuthenticated: Boolean, userJoined: Boolean): String {
        return if (isAuthenticated) {
            if (userJoined) {
                context.getString(R.string.button_leave_event)
            } else {
                context.getString(R.string.button_join_event)
            }
        } else {
            context.getString(R.string.button_login_to_join)
        }
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
    data object ShowJoinEventSuccessSnackbar : EventDetailsEffect()
    data class ShowNoNetworkScreenError(val esmorgaNoNetworkArguments: EsmorgaErrorScreenArguments = getEsmorgaNoNetworkScreenArguments()) : EventDetailsEffect()
    data class NavigateToLocation(val lat: Double, val lng: Double) : EventDetailsEffect()
    data class ShowFullScreenError(val esmorgaErrorScreenArguments: EsmorgaErrorScreenArguments = getEsmorgaErrorScreenArguments()) : EventDetailsEffect()
}
