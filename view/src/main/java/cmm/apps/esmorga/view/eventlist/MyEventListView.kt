package cmm.apps.esmorga.view.eventlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmm.apps.designsystem.EsmorgaButton
import cmm.apps.designsystem.EsmorgaText
import cmm.apps.designsystem.EsmorgaTextStyle
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.Screen
import cmm.apps.esmorga.view.eventlist.MyEventListScreenTestTags.MY_EVENT_LIST_TITLE
import cmm.apps.esmorga.view.eventlist.model.MyEventListEffect
import cmm.apps.esmorga.view.eventlist.model.MyEventListError
import cmm.apps.esmorga.view.eventlist.model.MyEventListUiState
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Screen
@Composable
fun MyEventListScreen(elvm: MyEventListViewModel = koinViewModel(), onEventClick: (eventId: String) -> Unit, onSignInClick: () -> Unit) {
    val uiState: MyEventListUiState by elvm.uiState.collectAsStateWithLifecycle()

    val message = stringResource(R.string.snackbar_no_internet)
    val snackbarHostState = remember { SnackbarHostState() }
    val localCoroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        elvm.effect.collect { eff ->
            when (eff) {
                is MyEventListEffect.ShowNoNetworkPrompt -> {
                    localCoroutineScope.launch {
                        snackbarHostState.showSnackbar(message = message)
                    }
                }

                is MyEventListEffect.NavigateToEventDetail -> onEventClick(eff.eventId)
                is MyEventListEffect.NavigateToSignIn -> onSignInClick()
            }
        }
    }

    EsmorgaTheme {
        MyEventListView(
            uiState = uiState,
            snackbarHostState = snackbarHostState,
            onSignInClick = { elvm.onSignInClick() },
            onEventClick = { elvm.onEventClick(it) },
            onRetryClick = { elvm.onRetryClick() }
        )
    }
}

@Composable
fun MyEventListView(
    uiState: MyEventListUiState,
    snackbarHostState: SnackbarHostState,
    onSignInClick: () -> Unit,
    onEventClick: (eventId: String) -> Unit,
    onRetryClick: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding(),
            )
        ) {
            EsmorgaText(
                text = stringResource(R.string.screen_my_events_title),
                style = EsmorgaTextStyle.HEADING_1,
                modifier = Modifier
                    .padding(vertical = 32.dp, horizontal = 16.dp)
                    .testTag(MY_EVENT_LIST_TITLE)
            )
            if (uiState.loading) {
                EventListLoading()
            } else {
                when (uiState.error) {
                    MyEventListError.EMPTY_LIST -> MyEventsEmptyView()
                    MyEventListError.NOT_LOGGED_IN -> MyEventGuestError(stringResource(R.string.unauthenticated_error_title), stringResource(R.string.button_login)) { onSignInClick() }
                    MyEventListError.NO_CONNECTION, MyEventListError.UNKNOWN -> MyEventGuestError(stringResource(R.string.default_error_title), stringResource(R.string.button_retry)) { onRetryClick() }
                    null -> EventList(uiState.eventList, onEventClick, modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
        }
    }
}

@Composable
fun MyEventGuestError(errorMessage: String, buttonText: String, onButtonClicked: () -> Unit) {
    val lottieAnimation by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.oops))
    Column(
        modifier = Modifier
            .padding(
                start = 16.dp,
                end = 16.dp
            )
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        LottieAnimation(
            composition = lottieAnimation,
            iterations = Int.MAX_VALUE,
            contentScale = ContentScale.Inside,
            modifier = Modifier.fillMaxHeight(0.3f)
        )
        EsmorgaText(errorMessage, style = EsmorgaTextStyle.HEADING_2)
        Spacer(modifier = Modifier.weight(1f))
        EsmorgaButton(buttonText) {
            onButtonClicked.invoke()
        }
    }
}

@Composable
fun MyEventsEmptyView() {
    val lottieAnimation by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty))
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxSize(),
    ) {
        EsmorgaText(
            text = stringResource(R.string.screen_my_events_empty_text),
            style = EsmorgaTextStyle.HEADING_1,
            modifier = Modifier.padding(16.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        LottieAnimation(
            composition = lottieAnimation,
            iterations = Int.MAX_VALUE,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .align(Alignment.Start)
        )
        Spacer(modifier = Modifier.weight(0.1f))
    }

}

object MyEventListScreenTestTags {
    const val MY_EVENT_LIST_TITLE = "event list screen title"
}