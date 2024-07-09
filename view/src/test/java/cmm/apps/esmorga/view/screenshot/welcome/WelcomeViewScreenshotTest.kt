package cmm.apps.esmorga.view.screenshot.welcome

import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.screenshot.BaseScreenshotTest
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import cmm.apps.esmorga.view.welcome.WelcomeView
import cmm.apps.esmorga.view.welcome.model.WelcomeUiState
import org.junit.Test

class WelcomeViewScreenshotTest : BaseScreenshotTest() {

    @Test
    fun welcomeView_lightTheme_data() {
        val uiState = WelcomeUiState(
            primaryButtonText = "Primary",
            secondaryButtonText = "Secondary",
            icon = R.drawable.ic_app
        )

        snapshotWithState(uiState)
    }

    private fun snapshotWithState(uiState: WelcomeUiState) {
        paparazzi.snapshot {
            EsmorgaTheme(darkTheme = false) {
                WelcomeView(uiState = uiState, onPrimaryButtonClicked = { }) {

                }
            }
        }
    }
}