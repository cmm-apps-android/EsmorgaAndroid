package cmm.apps.esmorga.view.screenshot.changepassword

import androidx.compose.material3.SnackbarHostState
import cmm.apps.esmorga.view.changepassword.ChangePasswordView
import cmm.apps.esmorga.view.changepassword.model.ChangePasswordUiState
import cmm.apps.esmorga.view.screenshot.BaseScreenshotTest
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import org.junit.Test

class ChangePasswordScreenShotTest : BaseScreenshotTest() {

    @Test
    fun changePasswordView_lightTheme_empty() {
        snapshotWithState(currentPassError = null, newPassError = null, repeatPassError = null)
    }

    @Test
    fun changePasswordView_lightTheme_passwords_errors() {
        snapshotWithState(currentPassError = "Invalid password error", newPassError = "Invalid password error", repeatPassError = "Invalid password error")
    }

    private fun snapshotWithState(currentPassError: String?, newPassError: String?, repeatPassError: String?) {
        paparazzi.snapshot {
            EsmorgaTheme(darkTheme = false) {
                ChangePasswordView(
                    uiState = ChangePasswordUiState(
                        isLoading = false,
                        currentPasswordError = currentPassError,
                        newPasswordError = newPassError,
                        repeatPasswordError = repeatPassError
                    ),
                    onBackPressed = {},
                    onChangePasswordClicked = { _, _ -> },
                    snackbarHostState = SnackbarHostState(),
                    validateField = { _, _, _, _, _ -> }
                )
            }
        }
    }
}