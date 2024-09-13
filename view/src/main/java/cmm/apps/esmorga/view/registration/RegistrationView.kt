package cmm.apps.esmorga.view.registration

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
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
import cmm.apps.esmorga.view.registration.RegistrationScreenTestTags.REGISTRATION_BACK_BUTTON
import cmm.apps.esmorga.view.registration.RegistrationScreenTestTags.REGISTRATION_LOGIN_BUTTON
import cmm.apps.esmorga.view.registration.RegistrationScreenTestTags.REGISTRATION_TITLE
import cmm.apps.esmorga.view.registration.model.RegistrationEffect
import cmm.apps.esmorga.view.registration.model.RegistrationUiState
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@Screen
@Composable
fun RegistrationScreen(
    rvm: RegistrationViewModel = koinViewModel(),
    onRegistrationSuccess: () -> Unit,
    onRegistrationError: (EsmorgaErrorScreenArguments) -> Unit,
    onBackClicked: () -> Unit
) {
    val uiState: RegistrationUiState by rvm.uiState.collectAsStateWithLifecycle()
    val message = stringResource(R.string.snackbar_no_internet)
    val snackbarHostState = remember { SnackbarHostState() }
    val localCoroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        rvm.effect.collect { eff ->
            when (eff) {
                is RegistrationEffect.ShowNoNetworkSnackbar -> localCoroutineScope.launch { snackbarHostState.showSnackbar(message = message) }
                is RegistrationEffect.ShowFullScreenError -> onRegistrationError(eff.esmorgaErrorScreenArguments)
                is RegistrationEffect.NavigateToEventList -> onRegistrationSuccess()
            }
        }
    }

    EsmorgaTheme {
        RegistrationView(
            uiState = uiState,
            snackbarHostState = snackbarHostState,
            onBackClicked = onBackClicked,
            onRegisterClicked = { name, lastName, email, password, repeatedPassword -> rvm.onRegisterClicked(name, lastName, email, password, repeatedPassword) },
            validateField = { field, value, comparisonValue -> rvm.validateField(field, value, comparisonValue) },
            onFieldChanged = { field -> rvm.onFieldChanged(field) }
        )
    }
}

@Composable
fun RegistrationView(
    uiState: RegistrationUiState,
    snackbarHostState: SnackbarHostState,
    onBackClicked: () -> Unit,
    onRegisterClicked: (String, String, String, String, String) -> Unit,
    validateField: (RegistrationField, String, String?) -> Unit,
    onFieldChanged: (RegistrationField) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatedPassword by remember { mutableStateOf("") }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 16.dp,
                        bottom = 16.dp,
                        start = 16.dp,
                        end = 8.dp
                    )
                    .height(48.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = stringResource(R.string.content_description_back_icon),
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .clickable { onBackClicked() }
                        .testTag(REGISTRATION_BACK_BUTTON)
                )
            }
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
                text = stringResource(id = R.string.screen_registration_title),
                style = EsmorgaTextStyle.HEADING_1,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .testTag(REGISTRATION_TITLE)
            )
            EsmorgaTextField(
                value = name,
                isEnabled = !uiState.loading,
                onValueChange = {
                    name = it
                    onFieldChanged(RegistrationField.NAME)
                },
                title = R.string.field_title_name,
                placeholder = R.string.placeholder_name,
                errorText = uiState.nameError,
                modifier = Modifier.onFocusChanged { focusState ->
                    if (!focusState.isFocused) {
                        validateField(RegistrationField.NAME, name, null)
                    }
                },
                imeAction = ImeAction.Next
            )
            EsmorgaTextField(
                value = lastName,
                isEnabled = !uiState.loading,
                onValueChange = {
                    lastName = it
                    onFieldChanged(RegistrationField.LAST_NAME)
                },
                title = R.string.field_title_last_name,
                placeholder = R.string.placeholder_last_name,
                errorText = uiState.lastNameError,
                modifier = Modifier.onFocusChanged { focusState ->
                    if (!focusState.isFocused) {
                        validateField(RegistrationField.LAST_NAME, lastName, null)
                    }
                },
                imeAction = ImeAction.Next
            )
            EsmorgaTextField(
                value = email,
                isEnabled = !uiState.loading,
                onValueChange = {
                    email = it
                    onFieldChanged(RegistrationField.EMAIL)
                },
                title = R.string.field_title_email,
                placeholder = R.string.placeholder_email,
                errorText = uiState.emailError,
                modifier = Modifier.onFocusChanged { focusState ->
                    if (!focusState.isFocused) {
                        validateField(RegistrationField.EMAIL, email, null)
                    }
                },
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            )
            EsmorgaTextField(
                value = password,
                isEnabled = !uiState.loading,
                onValueChange = {
                    password = it
                    onFieldChanged(RegistrationField.PASS)
                },
                title = R.string.field_title_password,
                placeholder = R.string.placeholder_password,
                errorText = uiState.passError,
                modifier = Modifier.onFocusChanged { focusState ->
                    if (!focusState.isFocused) {
                        validateField(RegistrationField.PASS, password, null)
                    }
                },
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Password
            )
            EsmorgaTextField(
                value = repeatedPassword,
                isEnabled = !uiState.loading,
                onValueChange = {
                    repeatedPassword = it
                    onFieldChanged(RegistrationField.REPEAT_PASS)
                },
                title = R.string.field_title_repeat_password,
                placeholder = R.string.placeholder_confirm_password,
                errorText = uiState.repeatPassError,
                modifier = Modifier.onFocusChanged { focusState ->
                    if (!focusState.isFocused) {
                        validateField(RegistrationField.REPEAT_PASS, password, repeatedPassword)
                    }
                },
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password,
                onDonePressed = {
                    onRegisterClicked(name, lastName, email, password, repeatedPassword)
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            EsmorgaButton(
                text = stringResource(id = R.string.button_register),
                isLoading = uiState.loading,
                primary = true,
                modifier = Modifier.testTag(REGISTRATION_LOGIN_BUTTON)
            ) {
                onRegisterClicked(name, lastName, email, password, repeatedPassword)
            }
        }

    }
}

object RegistrationScreenTestTags {
    const val REGISTRATION_TITLE = "registration screen title"
    const val REGISTRATION_LOGIN_BUTTON = "registration screen button"
    const val REGISTRATION_BACK_BUTTON = "registration screen back button"
}