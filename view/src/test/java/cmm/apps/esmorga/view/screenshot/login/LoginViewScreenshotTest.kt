package cmm.apps.esmorga.view.screenshot.login

import androidx.compose.material3.SnackbarHostState
import cmm.apps.esmorga.view.login.LoginView
import cmm.apps.esmorga.view.login.model.LoginUiState
import cmm.apps.esmorga.view.screenshot.BaseScreenshotTest
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import org.junit.Test

class LoginViewScreenshotTest : BaseScreenshotTest() {

    @Test
    fun loginView_lightTheme_empty() {
        snapshotWithState(emailError = null, passwordError = null)
    }

    @Test
    fun loginView_lightTheme_email_error() {
        snapshotWithState(emailError = "Invalid email error", passwordError = null)
    }

    @Test
    fun loginView_lightTheme_password_error() {
        snapshotWithState(emailError = null, passwordError = "Invalid password error")
    }

    private fun snapshotWithState(emailError: String?, passwordError: String?) {
        paparazzi.snapshot {
            EsmorgaTheme(darkTheme = false) {
                LoginView(
                    uiState = LoginUiState(
                        loading = false,
                        emailError = emailError,
                        passwordError = passwordError
                    ),
                    snackbarHostState = SnackbarHostState(),
                    onLoginClicked = { _, _ -> },
                    onRegisterClicked = { },
                    validateEmail = { },
                    validatePass = { }
                )
            }
        }
    }
}