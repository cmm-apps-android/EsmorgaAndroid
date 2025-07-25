package cmm.apps.esmorga.view.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmm.apps.designsystem.EsmorgaDialog
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
            onChangePassword = {
                //TODO To be done in MOB-176
            },
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
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            EsmorgaText(
                text = stringResource(R.string.my_profile_title),
                style = EsmorgaTextStyle.HEADING_1,
                modifier = Modifier
                    .padding(vertical = 32.dp, horizontal = 16.dp)
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
        EsmorgaDialog(
            title = stringResource(R.string.my_profile_logout_pop_up_title),
            dismissButtonText = stringResource(R.string.my_profile_logout_pop_up_cancel),
            confirmButtonText = stringResource(R.string.my_profile_logout_pop_up_confirm),
            onConfirm = {
                shownDialog = false
                onLogout()
            },
            onDismiss = { shownDialog = false })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
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

        EsmorgaRow(title = stringResource(R.string.my_profile_changue_password), onClick = onChangePassword )
        EsmorgaRow(title = stringResource(R.string.my_profile_logout), onClick =  { shownDialog = true })
    }
}

object HomeScreenTestTags {
    const val PROFILE_TITLE = "profile screen title"
}