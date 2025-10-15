package cmm.apps.esmorga.view.screenshot.eventdetails

import androidx.compose.material3.SnackbarHostState
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
        snapshotWithState(buttonTitle = "Leave Event")
    }

    @Test
    fun eventDetailsView_lightTheme_data_user_event_not_joined() {
        snapshotWithState(buttonTitle = "Join Event")
    }

    @Test
    fun eventDetailsView_lightTheme_data_primary_button_loading_state() {
        snapshotWithState(buttonLoading = true)
    }
    @Test
    fun eventDetailsView_lightTheme_event_full_user_not_joined() {
        snapshotWithState(
            currentAttendeeCount = 10,
            maxCapacity = 10,
            buttonTitle = "Full",
            isJoinButtonEnabled = false
        )
    }

    @Test
    fun eventDetailsView_lightTheme_event_full_user_joined() {
        snapshotWithState(
            currentAttendeeCount = 10,
            maxCapacity = 10,
            buttonTitle = "Leave Event",
            isJoinButtonEnabled = true
        )
    }

    private fun snapshotWithState(
        lat: Double? = 0.0,
        lng: Double? = 2.88,
        buttonTitle: String = "Sign in to sign up",
        buttonLoading: Boolean = false,
        currentAttendeeCount: Int? = null,
        maxCapacity: Int? = null,
        isJoinButtonEnabled: Boolean = true
    ) {
        paparazzi.snapshot {
            EsmorgaTheme(darkTheme = false) {
                EventDetailsView(
                    uiState = EventDetailsUiState(
                        id = "1",
                        title = "Mobgen fest",
                        subtitle = "Fri, Sep 26, 2025, 16:44",
                        description = "El mejor evento del año",
                        image = "test.png",
                        locationName = "Mi casa",
                        locationLat = lat,
                        locationLng = lng,
                        primaryButtonTitle = buttonTitle,
                        primaryButtonLoading = buttonLoading,
                        currentAttendeeCount = currentAttendeeCount?: 0,
                        maxCapacity = maxCapacity,
                        isJoinButtonEnabled = isJoinButtonEnabled
                    ),
                    snackbarHostState = SnackbarHostState(),
                    onNavigateClicked = {},
                    onBackPressed = {},
                    onPrimaryButtonClicked = {}
                )
            }
        }
    }

}