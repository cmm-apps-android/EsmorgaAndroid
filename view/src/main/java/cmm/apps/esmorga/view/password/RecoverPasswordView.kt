package cmm.apps.esmorga.view.password

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArguments
import cmm.apps.esmorga.view.eventdetails.EventDetailsScreenTestTags.EVENT_DETAILS_BACK_BUTTON
import cmm.apps.esmorga.view.password.RecoverPasswordScreenTestTags.RECOVER_PASSWORD_EMAIL_INPUT
import cmm.apps.esmorga.view.password.RecoverPasswordScreenTestTags.RECOVER_PASSWORD_SEND_EMAIL_BUTTON
import cmm.apps.esmorga.view.password.RecoverPasswordScreenTestTags.RECOVER_PASSWORD_SHOW_SNACKBAR
import cmm.apps.esmorga.view.password.RecoverPasswordScreenTestTags.RECOVER_PASSWORD_TITLE
import cmm.apps.esmorga.view.password.model.RecoverPasswordEffect
import cmm.apps.esmorga.view.password.model.RecoverPasswordUiState
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Screen
@Composable
fun RecoverPasswordScreen(
    rpvm: RecoverPasswordViewModel = koinViewModel(),
    onBackClicked: () -> Unit,
    onRecoverPasswordError: (EsmorgaErrorScreenArguments) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val localCoroutineScope = rememberCoroutineScope()
    val uiState: RecoverPasswordUiState by rpvm.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        rpvm.effect.collect { eff ->
            when (eff) {
                is RecoverPasswordEffect.ShowSnackbarSuccess -> localCoroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = eff.message
                    )
                }

                is RecoverPasswordEffect.ShowFullScreenError -> onRecoverPasswordError(eff.esmorgaErrorScreenArguments)
            }
        }
    }

    EsmorgaTheme {
        RecoverPasswordView(
            snackbarHostState = snackbarHostState,
            uiState = uiState,
            onBackClicked = onBackClicked,
            onSendEmailClicked = { email -> rpvm.onResendEmailClicked(email) },
            validateEmail = { email -> rpvm.validateEmail(email) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecoverPasswordView(
    snackbarHostState: SnackbarHostState,
    uiState: RecoverPasswordUiState,
    onBackClicked: () -> Unit,
    onSendEmailClicked: (String) -> Unit,
    validateEmail: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    Scaffold(
        modifier = Modifier.imePadding(),
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .testTag(RECOVER_PASSWORD_SHOW_SNACKBAR)
            )
        },
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = onBackClicked,
                        modifier = Modifier.testTag(EVENT_DETAILS_BACK_BUTTON)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.content_description_back_icon)
                        )
                    }
                }
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
                text = stringResource(id = R.string.forgot_password_screen_title),
                style = EsmorgaTextStyle.HEADING_1,
                modifier = Modifier
                    .testTag(RECOVER_PASSWORD_TITLE)
            )
            Spacer(modifier = Modifier.height(16.dp))
            EsmorgaTextField(
                value = email,
                onValueChange = {
                    email = it
                },
                title = R.string.field_title_email,
                placeholder = R.string.placeholder_email,
                errorText = uiState.emailError,
                modifier = Modifier
                    .onFocusChanged { focusState ->
                        if (!focusState.isFocused) {
                            validateEmail(email)
                        }
                    }
                    .testTag(RECOVER_PASSWORD_EMAIL_INPUT),
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            )
            Spacer(modifier = Modifier.height(32.dp))
            EsmorgaButton(
                text = stringResource(id = R.string.forgot_password_button),
                primary = true,
                modifier = Modifier
                    .testTag(RECOVER_PASSWORD_SEND_EMAIL_BUTTON)
            ) {
                onSendEmailClicked(email)
            }
        }
    }
}

object RecoverPasswordScreenTestTags {
    const val RECOVER_PASSWORD_TITLE = "recover password screen title"
    const val RECOVER_PASSWORD_BACK_BUTTON = "recover password screen back button"
    const val RECOVER_PASSWORD_SEND_EMAIL_BUTTON = "recover password screen send email button"
    const val RECOVER_PASSWORD_SHOW_SNACKBAR = "recover password screen show snackBar"
    const val RECOVER_PASSWORD_EMAIL_INPUT = "recover password screen email input"
}