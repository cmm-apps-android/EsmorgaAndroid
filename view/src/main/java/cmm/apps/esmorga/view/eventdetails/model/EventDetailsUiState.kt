package cmm.apps.esmorga.view.eventdetails.model

import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.eventdetails.model.EventDetailsUiStateHelper.getPrimaryButtonTitle

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
    val userJoined: Boolean = false,
    val isAuthenticated: Boolean = false,
    val primaryButtonTitle: Int = getPrimaryButtonTitle(userJoined, isAuthenticated)
)

object EventDetailsUiStateHelper {
    fun getPrimaryButtonTitle(userJoined: Boolean, isAuthenticated: Boolean): Int {
        return if (isAuthenticated) {
            if (userJoined) {
                R.string.button_leave_event
            } else {
                R.string.button_join_event
            }
        } else {
            R.string.button_login_to_join
        }
    }
}

sealed class EventDetailsEffect {
    data object NavigateBack : EventDetailsEffect()

    data class NavigateToLocation(val lat: Double, val lng: Double) : EventDetailsEffect()
    data object NavigateToLoginScreen : EventDetailsEffect()

}
