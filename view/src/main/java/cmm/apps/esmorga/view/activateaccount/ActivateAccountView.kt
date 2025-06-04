package cmm.apps.esmorga.view.activateaccount

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmm.apps.designsystem.EsmorgaButton
import cmm.apps.designsystem.EsmorgaText
import cmm.apps.designsystem.EsmorgaTextStyle
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.Screen
import cmm.apps.esmorga.view.activateaccount.RegistrationConfirmationScreenTestTags.ACTIVATE_ACCOUNT_BUTTON
import cmm.apps.esmorga.view.activateaccount.RegistrationConfirmationScreenTestTags.ACTIVATE_ACCOUNT_IMAGE
import cmm.apps.esmorga.view.activateaccount.RegistrationConfirmationScreenTestTags.ACTIVATE_ACCOUNT_SUBTITLE
import cmm.apps.esmorga.view.activateaccount.RegistrationConfirmationScreenTestTags.ACTIVATE_ACCOUNT_TITLE
import cmm.apps.esmorga.view.activateaccount.model.ActivateAccountEffect
import cmm.apps.esmorga.view.activateaccount.model.ActivateAccountUiState
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArguments
import cmm.apps.esmorga.view.extensions.observeLifecycleEvents
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Screen
@Composable
fun ActivateAccountScreen(
    verificationCode: String,
    viewModel: ActivateAccountViewModel = koinViewModel(parameters = { parametersOf(verificationCode) }),
    onContinueClick: () -> Unit = {},
    onError: (EsmorgaErrorScreenArguments) -> Unit,
    onLastTryError: (EsmorgaErrorScreenArguments, Boolean) -> Unit
) {
    val uiState: ActivateAccountUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    viewModel.observeLifecycleEvents(lifecycle)
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ActivateAccountEffect.ShowFullScreenError -> {
                    onError(effect.esmorgaErrorScreenArguments)
                }

                is ActivateAccountEffect.ShowLastTryFullScreenError -> {
                    onLastTryError(effect.esmorgaErrorScreenArguments, effect.redirectToWelcome)
                }

                is ActivateAccountEffect.NavigateToWelcomeScreen -> {
                    onContinueClick()
                }
            }
        }
    }
    EsmorgaTheme {
        ActivateAccountView(uiState = uiState,
            onContinueClick = {
            viewModel.onContinueClicked()
        })
    }
}

@Composable
fun ActivateAccountView(
    uiState: ActivateAccountUiState,
    onContinueClick: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxWidth(),
        ) {

            Image(
                painter = painterResource(id = R.drawable.activate_account_image), contentDescription = null, contentScale = ContentScale.FillWidth, modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .testTag(ACTIVATE_ACCOUNT_IMAGE)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                EsmorgaText(
                    text = stringResource(R.string.activate_account_title), style = EsmorgaTextStyle.HEADING_1, Modifier
                        .padding(top = 20.dp, bottom = 12.dp)
                        .testTag(ACTIVATE_ACCOUNT_TITLE)
                )

                EsmorgaText(
                    text = stringResource(R.string.activate_account_description), style = EsmorgaTextStyle.BODY_1, Modifier
                        .padding(top = 20.dp, bottom = 12.dp)
                        .testTag(ACTIVATE_ACCOUNT_SUBTITLE)
                )

                EsmorgaButton(
                    text = stringResource(R.string.activate_account_continue), modifier = Modifier
                        .padding(top = 32.dp, bottom = 16.dp)
                        .testTag(ACTIVATE_ACCOUNT_BUTTON), isLoading = uiState.isLoading
                ) {
                    onContinueClick()
                }
            }
        }

    }
}

object RegistrationConfirmationScreenTestTags {
    const val ACTIVATE_ACCOUNT_TITLE = "activate account screen title"
    const val ACTIVATE_ACCOUNT_BUTTON = "activate account screen button"
    const val ACTIVATE_ACCOUNT_IMAGE = "activate account screen image"
    const val ACTIVATE_ACCOUNT_SUBTITLE = "activate account screen subtitle"
}