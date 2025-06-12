package cmm.apps.esmorga.view.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmm.apps.designsystem.EsmorgaButton
import cmm.apps.designsystem.EsmorgaGuestError
import cmm.apps.designsystem.EsmorgaRow
import cmm.apps.designsystem.EsmorgaText
import cmm.apps.designsystem.EsmorgaTextStyle
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.Screen
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArguments
import cmm.apps.esmorga.view.extensions.observeLifecycleEvents
import cmm.apps.esmorga.view.profile.HomeScreenTestTags.PROFILE_TITLE
import cmm.apps.esmorga.view.profile.model.ProfileEffect
import cmm.apps.esmorga.view.profile.model.ProfileUiState
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import org.koin.androidx.compose.koinViewModel

@Screen
@Composable
fun ProfileScreen(
    rvm: ProfileViewModel = koinViewModel(),
    navigateLogIn: () -> Unit,
    onNoNetworkError: (EsmorgaErrorScreenArguments) -> Unit

) {
    val context = LocalContext.current
    val uiState: ProfileUiState by rvm.uiState.collectAsStateWithLifecycle()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    rvm.observeLifecycleEvents(lifecycle)

    LaunchedEffect(Unit) {
        rvm.effect.collect { eff ->
            when (eff) {
                ProfileEffect.NavigateToChangePassword -> {
                    //TODO to be done in MOB-176
                }

                ProfileEffect.NavigateToLogOut -> {
                    rvm.clearUserData()
                    navigateLogIn()
                }

                is ProfileEffect.ShowNoNetworkError -> {
                    onNoNetworkError(eff.esmorgaNoNetworkArguments)
                }

                ProfileEffect.NavigateToLogIn -> {
                    navigateLogIn()
                }
            }
        }
    }

    EsmorgaTheme {
        ProfileView(
            uiState = uiState,
            shownLogOutDialog = { rvm.logout() },
            onChangePassword = { rvm.changePassword(context) },
            onNavigateLogin = { rvm.logIn() }
        )
    }
}

@Composable
fun ProfileView(
    uiState: ProfileUiState,
    shownLogOutDialog: () -> Unit,
    onChangePassword: () -> Unit,
    onNavigateLogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        EsmorgaText(
            text = stringResource(R.string.my_profile_title),
            style = EsmorgaTextStyle.HEADING_1,
            modifier = Modifier
                .padding(vertical = 32.dp)
                .testTag(PROFILE_TITLE)
        )
        if (uiState.user == null) {
            EsmorgaGuestError(
                stringResource(R.string.unauthenticated_error_message),
                stringResource(R.string.unauthenticated_error_login_button),
                { onNavigateLogin() },
                R.raw.oops
            )
        } else {
            LoggedProfileView(
                name = uiState.user.name,
                lastName = uiState.user.lastName,
                email = uiState.user.email,
                onLogout = { shownLogOutDialog() },
                onChangePassword = { onChangePassword() },
            )
        }
    }
}

@Composable
private fun LoggedProfileView(
    name: String,
    lastName: String,
    email: String,
    onLogout: () -> Unit,
    onChangePassword: () -> Unit
) {
    var shownDialog by remember { mutableStateOf(false) }

    if (shownDialog) {
        LogoutDialog(
            onConfirm = onLogout,
            onDismiss = { shownDialog = false })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        EsmorgaText(
            text = stringResource(R.string.my_profile_name),
            style = EsmorgaTextStyle.HEADING_1
        )
        EsmorgaText(text = "$name $lastName", style = EsmorgaTextStyle.BODY_1, modifier = Modifier.padding(bottom = 52.dp))

        EsmorgaText(
            text = stringResource(R.string.my_profile_email),
            style = EsmorgaTextStyle.HEADING_1
        )
        EsmorgaText(text = email, style = EsmorgaTextStyle.BODY_1, modifier = Modifier.padding(bottom = 52.dp))

        EsmorgaText(
            text = stringResource(R.string.my_profile_options),
            style = EsmorgaTextStyle.HEADING_1, modifier = Modifier.padding(bottom = 16.dp)
        )

        EsmorgaRow(title = stringResource(R.string.my_profile_logout), modifier = Modifier.clickable { shownDialog = true })
    }
}

@Composable
fun LogoutDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = {
        onDismiss()
    }) {
        Surface(
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(end = 16.dp, start = 16.dp, top = 32.dp, bottom = 16.dp)
                    .fillMaxWidth()
            ) {
                EsmorgaText(
                    text = stringResource(R.string.my_profile_logout_pop_up_title),
                    style = EsmorgaTextStyle.BODY_1,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    EsmorgaButton(
                        text = stringResource(R.string.my_profile_logout_pop_up_cancel),
                        onClick = {
                            onDismiss()
                        },
                        modifier = Modifier
                            .wrapContentSize(unbounded = true),
                        primary = false
                    )
                    Spacer(Modifier.width(12.dp))
                    EsmorgaButton(
                        text = stringResource(R.string.my_profile_logout_pop_up_confirm),
                        modifier = Modifier.wrapContentSize(unbounded = true),
                        onClick = {
                            onDismiss()
                            onConfirm()
                        }
                    )
                }
            }
        }
    }
}

object HomeScreenTestTags {
    const val PROFILE_TITLE = "profile screen title"
}