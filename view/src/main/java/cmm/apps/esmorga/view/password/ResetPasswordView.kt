package cmm.apps.esmorga.view.password

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
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
import org.koin.androidx.compose.koinViewModel

@Screen
@Composable
fun ResetPasswordScreen(
    rpvm: ResetPasswordViewModel = koinViewModel(),
    forgotPasswordCode: String,
    onResetPasswordError: (EsmorgaErrorScreenArguments) -> Unit,
    onResetPasswordSuccess: () -> Unit
) {
    val uiState: ResetPasswordUiState by rpvm.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        rpvm.effect.collect { eff ->
            when (eff) {
                is ResetPasswordEffect.ShowFullScreenError -> onResetPasswordError(eff.esmorgaErrorScreenArguments)
                is ResetPasswordEffect.NavigateToLogin -> onResetPasswordSuccess()
            }
        }
    }

    EsmorgaTheme {
        ResetPasswordView(
            uiState = uiState,
            validateField = { type, password, repeatPass -> rpvm.validateField(type, password, repeatPass) },
            onResetPasswordClicked = { password -> rpvm.onResetPasswordClicked(forgotPasswordCode, password) },
            enabledButton = { password, repeatPass -> rpvm.isEnabledButton(password, repeatPass) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordView(
    uiState: ResetPasswordUiState,
    validateField: (ResetPasswordField, String, String?) -> Unit,
    onResetPasswordClicked: (String) -> Unit,
    enabledButton: (String, String) -> Boolean
) {
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    Scaffold(
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
                    .testTag(RESET_PASSWORD_TITLE)
            )
            Spacer(modifier = Modifier.height(16.dp))
            EsmorgaTextField(
                value = password,
                onValueChange = {
                    password = it
                },
                title = R.string.reset_password_new_password_field,
                placeholder = R.string.placeholder_new_password,
                errorText = uiState.passwordError,
                modifier = Modifier
                    .onFocusChanged { focusState ->
                        if (!focusState.isFocused) {
                            validateField(ResetPasswordField.PASS, password, null)
                        }
                    }
                    .testTag(RESET_PASSWORD_NEW_PASSWORD_INPUT),
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            )
            EsmorgaTextField(
                value = repeatPassword,
                onValueChange = {
                    repeatPassword = it
                },
                title = R.string.reset_password_repeat_password_field,
                placeholder = R.string.placeholder_confirm_password,
                errorText = uiState.repeatPasswordError,
                modifier = Modifier
                    .onFocusChanged { focusState ->
                        if (!focusState.isFocused) {
                            validateField(ResetPasswordField.REPEAT_PASS, repeatPassword, password)
                        }
                    }
                    .testTag(RESET_PASSWORD_REPEAT_PASSWORD_INPUT),
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            )
            Spacer(modifier = Modifier.height(32.dp))
            EsmorgaButton(
                text = stringResource(id = R.string.reset_password_button),
                primary = true,
                modifier = Modifier
                    .testTag(RESET_PASSWORD_CHANGE_PASSWORD_BUTTON),
                isEnabled = enabledButton(password, repeatPassword),
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

@Preview(showBackground = true)
@Composable
fun ResetPasswordScreenPreview() {
    ResetPasswordView(
        uiState = ResetPasswordUiState(),
        validateField = { _, _, _ -> },
        onResetPasswordClicked = {},
        enabledButton = { _, _ -> false }
    )
}