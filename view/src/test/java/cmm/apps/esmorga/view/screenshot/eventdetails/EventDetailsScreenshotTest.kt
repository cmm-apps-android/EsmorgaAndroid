package cmm.apps.esmorga.view.screenshot.eventdetails

import cmm.apps.esmorga.view.eventdetails.EventDetailsView
import cmm.apps.esmorga.view.eventdetails.model.EventDetailsUiState
import cmm.apps.esmorga.view.screenshot.BaseScreenshotTest
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import org.junit.Test

class EventDetailsScreenshotTest : BaseScreenshotTest() {

    @Test
    fun eventDetailsView_lightTheme_no_location() {
        snapshotWithState(lat = null, lng = null)
    }

    @Test
    fun eventDetailsView_lightTheme_data() {
        snapshotWithState()
    }

    @Test
    fun eventDetailsView_lightTheme_data_user_event_joined() {
        snapshotWithState(userJoined = true, isAuthenticate = true)
    }

    @Test
    fun eventDetailsView_lightTheme_data_user_event_not_joined() {
        snapshotWithState(userJoined = false, isAuthenticate = true)
    }

    private fun snapshotWithState(lat: Double? = 0.0, lng: Double? = 2.88, userJoined: Boolean = false, isAuthenticate: Boolean = false) {
        paparazzi.snapshot {
            EsmorgaTheme(darkTheme = false) {
                EventDetailsView(
                    uiState = EventDetailsUiState(
                        id = "1",
                        title = "Mobgen fest",
                        subtitle = "35 de Mayo a las 27:00",
                        description = "El mejor evento del año",
                        image = "test.png",
                        locationName = "Mi casa",
                        locationLat = lat,
                        locationLng = lng,
                        userJoined = userJoined,
                        isAuthenticated = isAuthenticate
                    ),
                    onNavigateClicked = {},
                    onBackPressed = {},
                    onPrimaryButtonClicked = {}
                )
            }
        }
    }

}