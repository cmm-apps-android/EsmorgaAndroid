package cmm.apps.esmorga.view.registration

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cmm.apps.designsystem.EsmorgaButton
import cmm.apps.designsystem.EsmorgaText
import cmm.apps.designsystem.EsmorgaTextStyle
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.Screen
import cmm.apps.esmorga.view.navigation.openEmailApp
import cmm.apps.esmorga.view.registration.RegistrationConfirmationScreenTestTags.REGISTRATION_CONFIRMATION_BACK_BUTTON
import cmm.apps.esmorga.view.registration.RegistrationConfirmationScreenTestTags.REGISTRATION_CONFIRMATION_OPEN_BUTTON
import cmm.apps.esmorga.view.registration.RegistrationConfirmationScreenTestTags.REGISTRATION_CONFIRMATION_RESEND_BUTTON
import cmm.apps.esmorga.view.registration.RegistrationConfirmationScreenTestTags.REGISTRATION_CONFIRMATION_SHOW_SNACKBAR
import cmm.apps.esmorga.view.registration.RegistrationConfirmationScreenTestTags.REGISTRATION_CONFIRMATION_TITLE
import cmm.apps.esmorga.view.registration.model.RegistrationConfirmationEffect
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import cmm.apps.designsystem.R as DesignSystem
@Screen
@Composable
fun RegistrationConfirmationScreen(
    rvm: RegistrationConfirmationViewModel = koinViewModel(),
    onBackClicked: () -> Unit,
    email: String
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val localCoroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        rvm.effect.collect { eff ->
            when (eff) {
                is RegistrationConfirmationEffect.NavigateToEmailApp -> openEmailApp(context)
                is RegistrationConfirmationEffect.ShowSnackbarFailure -> localCoroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = eff.message
                    )
                }

                is RegistrationConfirmationEffect.ShowSnackbarSuccess -> localCoroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = eff.message
                    )
                }
            }
        }
    }

    EsmorgaTheme {
        RegistrationConfirmationView(
            snackbarHostState = snackbarHostState,
            onBackClicked = onBackClicked,
            onNavigateEmailApp = { rvm.onNavigateEmailApp() },
            onResendEmailClicked = { rvm.onResendEmailClicked(email) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationConfirmationView(
    snackbarHostState: SnackbarHostState,
    onBackClicked: () -> Unit,
    onNavigateEmailApp: () -> Unit,
    onResendEmailClicked: () -> Unit,
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState, modifier = Modifier.testTag(REGISTRATION_CONFIRMATION_SHOW_SNACKBAR)) },
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = onBackClicked,
                        modifier = Modifier.testTag(REGISTRATION_CONFIRMATION_BACK_BUTTON)
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
                )
                .fillMaxWidth()
                .verticalScroll(state = rememberScrollState())
        ) {
            Image(
                painter = painterResource(DesignSystem.drawable.img_email),
                contentDescription = "image email",
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(1f),
                contentScale = ContentScale.FillWidth
            )
            EsmorgaText(
                text = stringResource(id = R.string.register_confirmation_email_title),
                style = EsmorgaTextStyle.HEADING_1,
                modifier = Modifier
                    .padding(vertical = 20.dp, horizontal = 16.dp)
                    .testTag(REGISTRATION_CONFIRMATION_TITLE)
            )
            EsmorgaText(
                text = stringResource(id = R.string.register_confirmation_email_subtitle),
                style = EsmorgaTextStyle.BODY_1,
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            EsmorgaButton(
                text = stringResource(id = R.string.register_confirmation_email_button_email_app),
                primary = true,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .testTag(REGISTRATION_CONFIRMATION_OPEN_BUTTON)
            ) {
                onNavigateEmailApp()
            }
            Spacer(modifier = Modifier.height(16.dp))
            EsmorgaText(
                text = stringResource(id = R.string.register_confirmation_email_button_resend),
                style = EsmorgaTextStyle.BODY_1_ACCENT,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterHorizontally)
                    .clickable { onResendEmailClicked() }
                    .testTag(REGISTRATION_CONFIRMATION_RESEND_BUTTON)
            )
        }

    }
}

object RegistrationConfirmationScreenTestTags {
    const val REGISTRATION_CONFIRMATION_TITLE = "registration confirmation screen title"
    const val REGISTRATION_CONFIRMATION_OPEN_BUTTON = "registration confirmation screen open button"
    const val REGISTRATION_CONFIRMATION_RESEND_BUTTON =
        "registration confirmation screen resend button"
    const val REGISTRATION_CONFIRMATION_BACK_BUTTON = "registration confirmation screen back button"
    const val REGISTRATION_CONFIRMATION_SHOW_SNACKBAR = "registration confirmation screen show snackBar"
}