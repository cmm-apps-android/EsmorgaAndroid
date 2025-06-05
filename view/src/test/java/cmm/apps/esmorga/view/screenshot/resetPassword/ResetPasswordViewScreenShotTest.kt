package cmm.apps.esmorga.view.screenshot.resetPassword

import androidx.compose.material3.SnackbarHostState
import cmm.apps.esmorga.view.password.ResetPasswordView
import cmm.apps.esmorga.view.password.model.ResetPasswordUiState
import cmm.apps.esmorga.view.screenshot.BaseScreenshotTest
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import org.junit.Test

class ResetPasswordViewScreenShotTest : BaseScreenshotTest() {

    @Test
    fun resetPasswordView_lightTheme_empty() {
        snapshotWithState(passError = null, repeatPassError = null)
    }

    @Test
    fun resetPasswordView_lightTheme_passwords_errors() {
        snapshotWithState(passError = "Invalid password error", repeatPassError = "Invalid password error")
    }

    private fun snapshotWithState(passError: String?, repeatPassError: String?) {
        paparazzi.snapshot {
            EsmorgaTheme(darkTheme = false) {
                ResetPasswordView(
                    uiState = ResetPasswordUiState(
                        isLoading = false,
                        passwordError = passError,
                        repeatPasswordError = repeatPassError
                    ),
                    snackbarHostState = SnackbarHostState(),
                    onResetPasswordClicked = { },
                    validateField = { _, _, _ -> },
                    onValueChange = { _, _ -> }
                )
            }
        }
    }
}