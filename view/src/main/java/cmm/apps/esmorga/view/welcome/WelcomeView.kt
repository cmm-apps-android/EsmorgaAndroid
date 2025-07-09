package cmm.apps.esmorga.view.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmm.apps.designsystem.EsmorgaButton
import cmm.apps.esmorga.view.Screen
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import cmm.apps.esmorga.view.welcome.WelcomeScreenTestTags.WELCOME_PRIMARY_BUTTON
import cmm.apps.esmorga.view.welcome.WelcomeScreenTestTags.WELCOME_SECONDARY_BUTTON
import cmm.apps.esmorga.view.welcome.model.WelcomeEffect
import cmm.apps.esmorga.view.welcome.model.WelcomeUiState
import com.google.firebase.BuildConfig
import org.koin.androidx.compose.koinViewModel

@Screen
@Composable
fun WelcomeScreen(wvm: WelcomeViewModel = koinViewModel(), onLoginRegisterClicked: () -> Unit, onEnterAsGuestClicked: () -> Unit) {
    val uiState: WelcomeUiState by wvm.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        wvm.effect.collect { eff ->
            when (eff) {
                is WelcomeEffect.NavigateToEventList -> onEnterAsGuestClicked()
                is WelcomeEffect.NavigateToLogin -> onLoginRegisterClicked()
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
        Box(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding()
                )
                .fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = uiState.icon),
                    contentDescription = "App logo",
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(32.dp))
                )
                Spacer(modifier = Modifier.height(32.dp))
                EsmorgaButton(
                    text = uiState.primaryButtonText,
                    modifier = Modifier.testTag(WELCOME_PRIMARY_BUTTON)
                ) {
                    onPrimaryButtonClicked()
                }
                Spacer(modifier = Modifier.height(32.dp))
                EsmorgaButton(
                    text = uiState.secondaryButtonText,
                    primary = false,
                    modifier = Modifier.testTag(WELCOME_SECONDARY_BUTTON)
                ) {
                    onSecondaryButtonClicked()
                }
            }

            uiState.deviceId?.let {
                Text(
                    text = it,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .testTag("welcome_screen_device_id")
                        .padding(bottom = 16.dp)
                )
            }
        }
    }
}

object WelcomeScreenTestTags {
    const val WELCOME_PRIMARY_BUTTON = "welcome screen primary button"
    const val WELCOME_SECONDARY_BUTTON = "welcome screen secondary button"
}