package cmm.apps.esmorga.view.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
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
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmm.apps.designsystem.EsmorgaGuestError
import cmm.apps.designsystem.EsmorgaText
import cmm.apps.designsystem.EsmorgaTextStyle
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.Screen
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArguments
import cmm.apps.esmorga.view.eventlist.MyEventListScreenTestTags.MY_EVENT_LIST_TITLE
import cmm.apps.esmorga.view.extensions.observeLifecycleEvents
import cmm.apps.esmorga.view.profile.model.ProfileEffect
import cmm.apps.esmorga.view.profile.model.ProfileUiState
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import cmm.apps.esmorga.view.theme.Lavender
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
                    //TODO to be done in MOB-27
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
            .background(Lavender)
            .padding(16.dp)
    ) {
        EsmorgaText(
            text = stringResource(R.string.my_profile_title),
            style = EsmorgaTextStyle.HEADING_1,
            modifier = Modifier
                .padding(vertical = 32.dp)
                .testTag(MY_EVENT_LIST_TITLE)
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
    email: String,
    onLogout: () -> Unit,
    onChangePassword: () -> Unit
) {
    var shownDialog by remember { mutableStateOf(false) }

    if (shownDialog) {
        LogoutDialog(onConfirm = onLogout, onDismiss = { shownDialog = false })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        EsmorgaText(text = stringResource(R.string.my_profile_name), style = EsmorgaTextStyle.HEADING_1)
        EsmorgaText(text = name, style = EsmorgaTextStyle.BODY_1)
        Spacer(modifier = Modifier.height(52.dp))

        EsmorgaText(text = stringResource(R.string.my_profile_email), style = EsmorgaTextStyle.HEADING_1)
        EsmorgaText(text = email, style = EsmorgaTextStyle.BODY_1)
        Spacer(modifier = Modifier.height(52.dp))

        EsmorgaText(text = stringResource(R.string.my_profile_options), style = EsmorgaTextStyle.HEADING_1)
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .clickable {
                    onChangePassword()
                }
        ) {
            EsmorgaText(text = stringResource(R.string.my_profile_changue_password), style = EsmorgaTextStyle.HEADING_2)

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = stringResource(R.string.content_description_forward_icon)
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .clickable { shownDialog = true }
        ) {
            EsmorgaText(text = stringResource(R.string.my_profile_logout), style = EsmorgaTextStyle.HEADING_2)

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = stringResource(R.string.content_description_forward_icon)
            )
        }
    }
}

@Composable
fun LogoutDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            EsmorgaText(
                text = stringResource(R.string.my_profile_logout_pop_up_confirm),
                style = EsmorgaTextStyle.BODY_1,
                modifier = Modifier
                    .clickable {
                        onDismiss()
                        onConfirm()
                    }
                    .padding(start = 10.dp)
            )
        },
        dismissButton = {
            EsmorgaText(
                text = stringResource(R.string.my_profile_logout_pop_up_cancel),
                style = EsmorgaTextStyle.BODY_1,
                modifier = Modifier.clickable { onDismiss() }
            )
        },
        title = { EsmorgaText(text = stringResource(R.string.my_profile_logout_pop_up_title), style = EsmorgaTextStyle.TITLE) }
    )
}