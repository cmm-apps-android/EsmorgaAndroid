package cmm.apps.esmorga.view.profileV2

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cmm.apps.designsystem.EsmorgaGuestError
import cmm.apps.designsystem.EsmorgaRow
import cmm.apps.designsystem.EsmorgaText
import cmm.apps.designsystem.EsmorgaTextStyle
import cmm.apps.esmorga.domain.user.model.RoleType
import cmm.apps.esmorga.domain.user.model.User
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.Screen
import cmm.apps.esmorga.view.profileV2.model.ProfileUiStateV2
import cmm.apps.esmorga.view.theme.EsmorgaTheme

@Screen
@Composable
fun ProfileScreenV2() {

    EsmorgaTheme {
        ProfileViewV2(uiState = ProfileUiStateV2())
    }
}

@Composable
fun ProfileViewV2(
    uiState: ProfileUiStateV2,
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
                    .padding(vertical = 16.dp, horizontal = 16.dp)
            )

            if (uiState.user == null){
                EsmorgaGuestError(
                    errorMessage = stringResource(R.string.unauthenticated_error_message),
                    buttonText = stringResource(R.string.unauthenticated_error_login_button),
                    onButtonClicked = {},
                    animation = R.raw.oops)
            } else {
                LoggedProfileViewV2(name = "Fulano", lastName = "López", email = "fulanolopez@gmail.com", logOutClick = {}, changePasswordClick = {})
            }
        }
    }
}

@Composable
fun LoggedProfileViewV2(
    name: String,
    lastName: String,
    email: String,
    logOutClick: () -> Unit,
    changePasswordClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp, horizontal = 16.dp)
    ) {
        EsmorgaText(
            text = stringResource(R.string.my_profile_name),
            style = EsmorgaTextStyle.HEADING_1,
        )
        EsmorgaText(
            text = "$name $lastName ",
            style = EsmorgaTextStyle.BODY_1,
            modifier = Modifier.padding(top = 10.dp)
        )

        EsmorgaText(
            text = stringResource(R.string.my_profile_email),
            style = EsmorgaTextStyle.HEADING_1,
            modifier = Modifier.padding(top = 52.dp)
        )
        EsmorgaText(
            email,
            style = EsmorgaTextStyle.BODY_1,
            modifier = Modifier.padding(top = 10.dp)
        )

        EsmorgaText(
            text = stringResource(R.string.my_profile_options),
            style = EsmorgaTextStyle.HEADING_1,
            modifier = Modifier.padding(top = 52.dp, bottom = 16.dp)
        )

        EsmorgaRow(title = stringResource(R.string.my_profile_change_password), onClick = changePasswordClick)
        EsmorgaRow(title = stringResource(R.string.my_profile_logout), onClick = logOutClick)
    }
}

@Preview
@Composable
fun ProfileViewV2NotLoguedPreview() {
    EsmorgaTheme { ProfileScreenV2() }
}

@Preview
@Composable
fun ProfileViewV2LoguedPreview() {
    EsmorgaTheme {
        ProfileViewV2(uiState = ProfileUiStateV2(user = User("Fulano", "López", "fulanolopez@gmail.com", role = RoleType.ADMIN)))
    }
}