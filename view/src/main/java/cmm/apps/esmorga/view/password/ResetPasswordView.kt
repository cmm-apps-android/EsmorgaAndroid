package cmm.apps.esmorga.view.password

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmm.apps.designsystem.EsmorgaButton
import cmm.apps.designsystem.EsmorgaText
import cmm.apps.designsystem.EsmorgaTextField
import cmm.apps.designsystem.EsmorgaTextStyle
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.Screen
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArguments
import cmm.apps.esmorga.view.password.ResetPasswordScreenTestTags.RESET_PASSWORD_CHANGE_PASSWORD_BUTTON
import cmm.apps.esmorga.view.password.ResetPasswordScreenTestTags.RESET_PASSWORD_NEW_PASSWORD_INPUT
import cmm.apps.esmorga.view.password.ResetPasswordScreenTestTags.RESET_PASSWORD_REPEAT_PASSWORD_INPUT
import cmm.apps.esmorga.view.password.ResetPasswordScreenTestTags.RESET_PASSWORD_TITLE
import cmm.apps.esmorga.view.password.ResetPasswordViewModel.ResetPasswordField
import cmm.apps.esmorga.view.password.model.ResetPasswordEffect
import cmm.apps.esmorga.view.password.model.ResetPasswordUiState
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Screen
@Composable
fun ResetPasswordScreen(
    rpvm: ResetPasswordViewModel = koinViewModel(),
    forgotPasswordCode: String,
    onResetPasswordError: (EsmorgaErrorScreenArguments) -> Unit,
    onResetPasswordSuccess: (String) -> Unit
) {
    val uiState: ResetPasswordUiState by rpvm.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val localCoroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        rpvm.effect.collect { eff ->
            when (eff) {
                is ResetPasswordEffect.ShowFullScreenError -> onResetPasswordError(eff.esmorgaErrorScreenArguments)
                is ResetPasswordEffect.NavigateToLogin -> onResetPasswordSuccess(eff.snackbarMessage)
                is ResetPasswordEffect.ShowNoConnectionSnackbar -> localCoroutineScope.launch { snackbarHostState.showSnackbar(eff.message) }
            }
        }
    }

    EsmorgaTheme {
        ResetPasswordView(
            uiState = uiState,
            snackbarHostState = snackbarHostState,
            validateField = { type, password, repeatPass, isTouched -> rpvm.validateField(type, password, repeatPass, isTouched) },
            onResetPasswordClicked = { password -> rpvm.onResetPasswordClicked(forgotPasswordCode, password) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordView(
    uiState: ResetPasswordUiState,
    snackbarHostState: SnackbarHostState,
    validateField: (ResetPasswordField, String, String?, Boolean) -> Unit,
    onResetPasswordClicked: (String) -> Unit
) {
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    var passwordTouched by remember { mutableStateOf(false) }
    var repeatPasswordTouched by remember { mutableStateOf(false) }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {}
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding(),
                    start = 16.dp,
                    end = 16.dp
                )
                .fillMaxWidth()
                .verticalScroll(state = rememberScrollState())
        ) {
            EsmorgaText(
                text = stringResource(id = R.string.reset_password_screen_title),
                style = EsmorgaTextStyle.HEADING_1,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .testTag(RESET_PASSWORD_TITLE)
            )
            EsmorgaTextField(
                value = password,
                onValueChange = {
                    passwordTouched = true
                    password = it
                    validateField(ResetPasswordField.PASS, password, null, passwordTouched)
                },
                title = R.string.reset_password_new_password_field,
                placeholder = R.string.placeholder_new_password,
                errorText = uiState.passwordError,
                modifier = Modifier
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) passwordTouched = true

                        if (!focusState.isFocused && passwordTouched) {
                            validateField(ResetPasswordField.PASS, password, null, passwordTouched)
                        }
                    }
                    .testTag(RESET_PASSWORD_NEW_PASSWORD_INPUT),
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Password
            )
            EsmorgaTextField(
                value = repeatPassword,
                onValueChange = {
                    repeatPasswordTouched = true
                    repeatPassword = it
                    validateField(ResetPasswordField.REPEAT_PASS, repeatPassword, password, repeatPasswordTouched)
                },
                title = R.string.reset_password_repeat_password_field,
                placeholder = R.string.placeholder_confirm_password,
                errorText = uiState.repeatPasswordError,
                modifier = Modifier
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) repeatPasswordTouched = true
                        if (!focusState.isFocused && repeatPasswordTouched) {
                            validateField(ResetPasswordField.REPEAT_PASS, repeatPassword, password, repeatPasswordTouched)
                        }
                    }
                    .testTag(RESET_PASSWORD_REPEAT_PASSWORD_INPUT),
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            )
            EsmorgaButton(
                text = stringResource(id = R.string.reset_password_button),
                primary = true,
                modifier = Modifier
                    .padding(top = 32.dp)
                    .testTag(RESET_PASSWORD_CHANGE_PASSWORD_BUTTON),
                isEnabled = uiState.enableButton(password, repeatPassword),
                isLoading = uiState.isLoading
            ) {
                onResetPasswordClicked(password)
            }
        }
    }
}

object ResetPasswordScreenTestTags {
    const val RESET_PASSWORD_TITLE = "reset password screen title"
    const val RESET_PASSWORD_CHANGE_PASSWORD_BUTTON = "reset password screen change password button"
    const val RESET_PASSWORD_NEW_PASSWORD_INPUT = "recover password screen new password input"
    const val RESET_PASSWORD_REPEAT_PASSWORD_INPUT = "recover password screen repeat password input"
}