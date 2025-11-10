package cmm.apps.esmorga.view.changepassword

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import cmm.apps.esmorga.view.changepassword.ChangePasswordScreen.CHANGE_PASSWORD_BUTTON
import cmm.apps.esmorga.view.changepassword.ChangePasswordScreen.CHANGE_PASSWORD_CURRENT_PASS_INPUT
import cmm.apps.esmorga.view.changepassword.ChangePasswordScreen.CHANGE_PASSWORD_NEW_PASS_INPUT
import cmm.apps.esmorga.view.changepassword.ChangePasswordScreen.CHANGE_PASSWORD_REPEAT_PASS_INPUT
import cmm.apps.esmorga.view.changepassword.ChangePasswordScreen.CHANGE_PASSWORD_SCREEN_TITLE
import cmm.apps.esmorga.view.changepassword.model.ChangePasswordEffect
import cmm.apps.esmorga.view.changepassword.model.ChangePasswordUiState
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArguments
import cmm.apps.esmorga.view.eventdetails.EventDetailsScreenTestTags.EVENT_DETAILS_BACK_BUTTON
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Screen
@Composable
fun ChangePasswordScreen(
    cpvm: ChangePasswordViewModel = koinViewModel(),
    onChangePasswordSuccess: (String) -> Unit,
    onChangePasswordError: (EsmorgaErrorScreenArguments) -> Unit,
    onBackPressed: () -> Unit
) {

    val uiState: ChangePasswordUiState by cpvm.uiState.collectAsStateWithLifecycle()
    val localCoroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        cpvm.effect.collect { eff ->
            when (eff) {
                is ChangePasswordEffect.NavigateToLogin -> onChangePasswordSuccess(eff.snackbarMessage)
                is ChangePasswordEffect.ShowFullScreenError -> onChangePasswordError(eff.esmorgaErrorScreenArguments)
                is ChangePasswordEffect.ShowNoConnectionSnackbar -> localCoroutineScope.launch { snackbarHostState.showSnackbar(eff.message) }
            }
        }
    }
    EsmorgaTheme {
        ChangePasswordView(
            uiState = uiState,
            snackbarHostState = snackbarHostState,
            onChangePasswordClicked = { currentPassword, newPassword ->
                cpvm.onChangePasswordClicked(currentPassword, newPassword)
            },
            onBackPressed = onBackPressed,
            validateField = { type, password, newPass, repeatNewPass, hasFocused -> cpvm.validateField(type, password, newPass, repeatNewPass, hasFocused) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordView(
    uiState: ChangePasswordUiState,
    snackbarHostState: SnackbarHostState,
    onChangePasswordClicked: (String, String) -> Unit,
    onBackPressed: () -> Unit,
    validateField: (ChangePasswordViewModel.ChangePasswordField, String, String?, String?, Boolean) -> Unit
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var repeatNewPassword by remember { mutableStateOf("") }
    var currentPasswordFieldAlreadyFocussed by remember { mutableStateOf(false) }
    var newPasswordFieldAlreadyFocussed by remember { mutableStateOf(false) }
    var repeatNewasswordFieldAlreadyFocussed by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .imePadding()
            .fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onBackPressed()
                        },
                        modifier = Modifier.testTag(EVENT_DETAILS_BACK_BUTTON)
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.content_description_back_icon))
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .verticalScroll(rememberScrollState())
        )
        {
            EsmorgaText(
                text = stringResource(R.string.reset_password_screen_title),
                style = EsmorgaTextStyle.HEADING_1,
                modifier = Modifier
                    .padding(vertical = 32.dp, horizontal = 16.dp)
                    .testTag(CHANGE_PASSWORD_SCREEN_TITLE)
            )

            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                EsmorgaTextField(
                    value = currentPassword,
                    onValueChange = {
                        currentPassword = it
                        validateField(ChangePasswordViewModel.ChangePasswordField.PASS, currentPassword, null, null, currentPasswordFieldAlreadyFocussed)
                    },
                    title = R.string.field_title_password,
                    placeholder = R.string.placeholder_password,
                    errorText = uiState.currentPasswordError,
                    modifier = Modifier
                        .onFocusChanged { focusState ->
                            if (focusState.isFocused) currentPasswordFieldAlreadyFocussed = true

                            if (!focusState.isFocused) {
                                validateField(ChangePasswordViewModel.ChangePasswordField.PASS, currentPassword, null, null, currentPasswordFieldAlreadyFocussed)
                            }
                        }
                        .testTag(CHANGE_PASSWORD_CURRENT_PASS_INPUT),
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Password
                )

                EsmorgaTextField(
                    value = newPassword,
                    onValueChange = {
                        newPassword = it
                        validateField(ChangePasswordViewModel.ChangePasswordField.NEW_PASS, currentPassword, newPassword, null, newPasswordFieldAlreadyFocussed)
                    },
                    title = R.string.reset_password_new_password_field,
                    placeholder = R.string.placeholder_new_password,
                    errorText = uiState.newPasswordError,
                    modifier = Modifier
                        .onFocusChanged { focusState ->
                            if (focusState.isFocused) newPasswordFieldAlreadyFocussed = true

                            if (!focusState.isFocused) {
                                validateField(ChangePasswordViewModel.ChangePasswordField.NEW_PASS, currentPassword, newPassword, null, newPasswordFieldAlreadyFocussed)
                            }
                        }
                        .testTag(CHANGE_PASSWORD_NEW_PASS_INPUT),
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Password
                )

                EsmorgaTextField(
                    value = repeatNewPassword,
                    onValueChange = {
                        repeatNewPassword = it
                        validateField(
                            ChangePasswordViewModel.ChangePasswordField.REPEAT_NEW_PASS,
                            currentPassword,
                            newPassword,
                            repeatNewPassword,
                            repeatNewasswordFieldAlreadyFocussed
                        )
                    },
                    title = R.string.reset_password_repeat_password_field,
                    placeholder = R.string.placeholder_confirm_password,
                    errorText = uiState.repeatPasswordError,
                    modifier = Modifier
                        .onFocusChanged { focusState ->
                            if (focusState.isFocused) repeatNewasswordFieldAlreadyFocussed = true

                            if (!focusState.isFocused) {
                                validateField(
                                    ChangePasswordViewModel.ChangePasswordField.REPEAT_NEW_PASS,
                                    currentPassword,
                                    newPassword,
                                    repeatNewPassword,
                                    repeatNewasswordFieldAlreadyFocussed
                                )
                            }
                        }
                        .testTag(CHANGE_PASSWORD_REPEAT_PASS_INPUT),
                    imeAction = ImeAction.Done,
                    onDonePressed = { onChangePasswordClicked(currentPassword, newPassword) },
                    keyboardType = KeyboardType.Password
                )

                EsmorgaButton(
                    text = stringResource(id = R.string.reset_password_button),
                    primary = true,
                    isLoading = uiState.isLoading,
                    isEnabled = uiState.enableButton(currentPassword, newPassword, repeatNewPassword),
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .testTag(CHANGE_PASSWORD_BUTTON)
                ) {
                    onChangePasswordClicked(currentPassword, newPassword)
                }
            }
        }
    }
}

object ChangePasswordScreen {
    const val CHANGE_PASSWORD_SCREEN_TITLE = "change password screen title"
    const val CHANGE_PASSWORD_CURRENT_PASS_INPUT = "change password currente pass input"
    const val CHANGE_PASSWORD_NEW_PASS_INPUT = "change password new pass input"
    const val CHANGE_PASSWORD_REPEAT_PASS_INPUT = "change password repeat pass input"
    const val CHANGE_PASSWORD_BUTTON = "change password button"
}

