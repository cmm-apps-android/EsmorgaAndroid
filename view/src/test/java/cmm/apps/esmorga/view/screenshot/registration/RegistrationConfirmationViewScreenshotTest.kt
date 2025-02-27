package cmm.apps.esmorga.view.screenshot.registration

import androidx.compose.material3.SnackbarHostState
import cmm.apps.esmorga.view.registration.RegistrationConfirmationView
import cmm.apps.esmorga.view.screenshot.BaseScreenshotTest
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import org.junit.Test

class RegistrationConfirmationViewScreenshotTest : BaseScreenshotTest() {

    @Test
    fun registrationConfirmationView_lightTheme_data() {
        snapshotWithState()
    }

    private fun snapshotWithState() {
        paparazzi.snapshot {
            EsmorgaTheme(darkTheme = false) {
                RegistrationConfirmationView(
                    snackbarHostState = SnackbarHostState(),
                    onBackClicked = {},
                    onNavigateEmailApp = {},
                    onResendEmailClicked = {}
                )
            }
        }
    }
}