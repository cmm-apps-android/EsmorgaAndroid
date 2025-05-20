package cmm.apps.esmorga.view.screenshot.password

import androidx.compose.material3.SnackbarHostState
import cmm.apps.esmorga.view.password.RecoverPasswordView
import cmm.apps.esmorga.view.password.model.RecoverPasswordUiState
import cmm.apps.esmorga.view.screenshot.BaseScreenshotTest
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import org.junit.Test

class RecoverPasswordViewScreenshotTest : BaseScreenshotTest() {

    @Test
    fun recoverPasswordView_lightTheme_empty() {
        snapshotWithState(emailError = null)
    }

    @Test
    fun recoverPasswordView_lightTheme_email_error() {
        snapshotWithState(emailError = "Invalid email error")
    }

    private fun snapshotWithState(emailError: String?) {
        paparazzi.snapshot {
            EsmorgaTheme(darkTheme = false) {
                RecoverPasswordView(
                    snackbarHostState = SnackbarHostState(),
                    uiState = RecoverPasswordUiState(
                        emailError = emailError
                    ),
                    onBackClicked = { },
                    onSendEmailClicked = { },
                    validateEmail = { }
                )
            }
        }
    }
}