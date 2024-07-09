package cmm.apps.esmorga.view.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmm.apps.designsystem.EsmorgaButton
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import cmm.apps.esmorga.view.welcome.model.WelcomeEffect
import cmm.apps.esmorga.view.welcome.model.WelcomeUiState
import org.koin.androidx.compose.koinViewModel

@Composable
fun WelcomeScreen(wvm: WelcomeViewModel = koinViewModel(), onLoginRegisterClicked: () -> Unit, onEnterAsGuestClicked: () -> Unit) {
    val uiState: WelcomeUiState by wvm.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        wvm.effect.collect { eff ->
            when (eff) {
                is WelcomeEffect.NavigateToEventList -> onEnterAsGuestClicked()

            }
        }
    }
    EsmorgaTheme {
        WelcomeView(uiState = uiState, onPrimaryButtonClicked = { wvm.onPrimaryButtonClicked() }, onSecondaryButtonClicked = { wvm.onSecondaryButtonClicked() })
    }
}

@Composable
fun WelcomeView(uiState: WelcomeUiState, onPrimaryButtonClicked: () -> Unit, onSecondaryButtonClicked: () -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {}
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding()
                )
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = uiState.icon), contentDescription = "App logo",
                modifier = Modifier
                    .aspectRatio(1f)
            )
            EsmorgaButton(text = uiState.primaryButtonText) {
                onPrimaryButtonClicked()
            }
            Spacer(modifier = Modifier.height(32.dp))
            EsmorgaButton(text = uiState.secondaryButtonText, primary = false) {
                onSecondaryButtonClicked()
            }
        }
    }
}