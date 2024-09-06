package cmm.apps.esmorga.view.screenshot.error

import cmm.apps.designsystem.EsmorgaFullScreenError
import cmm.apps.esmorga.view.eventdetails.EventDetailsView
import cmm.apps.esmorga.view.eventdetails.model.EventDetailsUiState
import cmm.apps.esmorga.view.screenshot.BaseScreenshotTest
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import org.junit.Test

class FullErrorScreenshotTest : BaseScreenshotTest() {

    @Test
    fun fullScreenErrorView_lightTheme_data() {
        snapshotWithState()
    }

    private fun snapshotWithState(lat: Double? = 0.0, lng: Double? = 2.88) {
        paparazzi.snapshot {
            EsmorgaTheme(darkTheme = false) {
                EsmorgaFullScreenError(
                    title = "Something has failed, please try again later.",
                    buttonText = "Retry",
                    buttonAction = {}
                )
            }
        }
    }

}