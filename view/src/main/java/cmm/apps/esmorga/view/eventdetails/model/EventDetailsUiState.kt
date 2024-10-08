package cmm.apps.esmorga.view.eventdetails.model

import cmm.apps.esmorga.view.R

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
    val primaryButtonTitle: Int = R.string.button_login_to_join
)

object EventDetailsUiStateHelper {
    fun getPrimaryButtonTitle(isAuthenticated: Boolean, userJoined: Boolean): Int {
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
