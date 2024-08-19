package cmm.apps.esmorga.view.screenshot.registration

import androidx.compose.material3.SnackbarHostState
import cmm.apps.esmorga.view.registration.RegistrationView
import cmm.apps.esmorga.view.registration.model.RegistrationUiState
import cmm.apps.esmorga.view.screenshot.BaseScreenshotTest
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import org.junit.Test


class RegistrationViewScreenshotTest : BaseScreenshotTest() {

    @Test
    fun registrationView_lightTheme_empty() {
        snapshotWithState(nameError = null, lastNameError = null, emailError = null, passwordError = null, repeatPasswordError = null)
    }

    @Test
    fun registrationView_lightTheme_email_error() {
        snapshotWithState(
            nameError = "Invalid name",
            lastNameError = "Invalid last name",
            emailError = "Invalid email error",
            passwordError = "Invalid password",
            repeatPasswordError = "Passwords do not match"
        )
    }

    private fun snapshotWithState(nameError: String?, lastNameError: String?, emailError: String?, passwordError: String?, repeatPasswordError: String?) {
        paparazzi.snapshot {
            EsmorgaTheme(darkTheme = false) {
                RegistrationView(
                    uiState = RegistrationUiState(
                        loading = false,
                        nameError = nameError,
                        lastNameError = lastNameError,
                        emailError = emailError,
                        passwordError = passwordError,
                        repeatPasswordError = repeatPasswordError
                    ),
                    snackbarHostState = SnackbarHostState(),
                    onBackClicked = {},
                    onRegisterClicked = { _, _, _, _, _ -> },
                    validateName = {},
                    validateLastName = {},
                    validateEmail = {},
                    validatePass = {},
                    validateRepeatedPass = { _, _ -> },
                )
            }
        }
    }

}