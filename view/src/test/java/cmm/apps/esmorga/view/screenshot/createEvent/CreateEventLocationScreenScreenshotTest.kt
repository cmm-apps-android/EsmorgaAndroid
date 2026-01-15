package cmm.apps.esmorga.view.screenshot.createEvent

import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.createeventlocation.CreateEventFormLocationView
import cmm.apps.esmorga.view.createeventlocation.model.CreateEventFormLocationUiState
import cmm.apps.esmorga.view.screenshot.BaseScreenshotTest
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import org.junit.Test

class CreateEventFormLocationScreenScreenshotTest : BaseScreenshotTest() {

    @Test
    fun createEventLocationScreenDefault() {
        snapshotWithState(
            CreateEventFormLocationUiState(
                localizationName = "",
                localizationCoordinates = "",
                eventMaxCapacity = "",
                isButtonEnabled = false,
                locationError = null,
            )
        )
    }

    @Test
    fun createEventLocationScreenWithLocationError() {
        snapshotWithState(
            CreateEventFormLocationUiState(
                localizationName = "",
                localizationCoordinates = "",
                eventMaxCapacity = "",
                isButtonEnabled = false,
                locationError = R.string.inline_error_location_required,
            )
        )
    }

    @Test
    fun createEventLocationScreenValidLocation() {
        snapshotWithState(
            CreateEventFormLocationUiState(
                localizationName = "Madrid",
                localizationCoordinates = "",
                eventMaxCapacity = "",
                isButtonEnabled = true,
                locationError = null,
            )
        )
    }

    @Test
    fun createEventLocationScreenFullData() {
        snapshotWithState(
            CreateEventFormLocationUiState(
                localizationName = "Madrid",
                localizationCoordinates = "40.4168, -3.7038",
                eventMaxCapacity = "100",
                isButtonEnabled = true,
                locationError = null,
            )
        )
    }

    private fun snapshotWithState(uiState: CreateEventFormLocationUiState) {
        paparazzi.snapshot {
            EsmorgaTheme(darkTheme = false) {
                CreateEventFormLocationView(
                    uiState = uiState,
                    onBackPressed = {},
                    onLocationChange = {},
                    onCoordinatesChange = {},
                    onMaxCapacityChange = {},
                    onNextClick = {}
                )
            }
        }
    }
}
