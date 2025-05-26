package cmm.apps.esmorga.view.activateaccount

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmm.apps.designsystem.EsmorgaButton
import cmm.apps.designsystem.EsmorgaText
import cmm.apps.designsystem.EsmorgaTextStyle
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.Screen
import cmm.apps.esmorga.view.activateaccount.model.ActivateAccountEffect
import cmm.apps.esmorga.view.activateaccount.model.ActivateAccountUiState
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArguments
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Screen
@Composable
fun ActivateAccountScreen(
    verificationCode: String,
    viewModel: ActivateAccountViewModel = koinViewModel(parameters = { parametersOf(verificationCode) }),
    onContinueClick: () -> Unit = {},
    onError: (EsmorgaErrorScreenArguments) -> Unit
) {
    val uiState: ActivateAccountUiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ActivateAccountEffect.ShowFullScreenError -> {
                    onError(effect.esmorgaErrorScreenArguments)
                }
            }
        }
    }
    EsmorgaTheme {
        ActivateAccountView(
            uiState = uiState,
            onContinueClick = onContinueClick
        )
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
                //.fillMaxWidth(),
        ) {

            Image(
                painter = painterResource(id = R.drawable.activate_account_image),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
            )


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                EsmorgaText(
                    text = stringResource(R.string.activate_account_title),
                    style = EsmorgaTextStyle.HEADING_1,
                    Modifier.padding(top = 20.dp, bottom = 12.dp)
                )

                EsmorgaText(
                    text = stringResource(R.string.activate_account_description),
                    style = EsmorgaTextStyle.BODY_1,
                    Modifier.padding(top = 20.dp, bottom = 12.dp)
                )


                EsmorgaButton(
                    text = stringResource(R.string.activate_account_continue),
                    modifier = Modifier.padding(top = 32.dp, bottom = 16.dp),
                    isLoading = uiState.isLoading
                ) {
                    onContinueClick()
                }
            }
        }

    }
}
