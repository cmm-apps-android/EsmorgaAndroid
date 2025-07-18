package cmm.apps.esmorga.view.eventlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmm.apps.designsystem.EsmorgaGuestError
import cmm.apps.designsystem.EsmorgaText
import cmm.apps.designsystem.EsmorgaTextStyle
import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.Screen
import cmm.apps.esmorga.view.eventlist.MyEventListScreenTestTags.MY_EVENT_LIST_TITLE
import cmm.apps.esmorga.view.eventlist.model.EventListUiModel
import cmm.apps.esmorga.view.eventlist.model.MyEventListEffect
import cmm.apps.esmorga.view.eventlist.model.MyEventListError
import cmm.apps.esmorga.view.eventlist.model.MyEventListUiState
import cmm.apps.esmorga.view.extensions.observeLifecycleEvents
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Screen
@Composable
fun MyEventListScreen(elvm: MyEventListViewModel = koinViewModel(), onEventClick: (event: Event) -> Unit, onSignInClick: () -> Unit) {
    val uiState: MyEventListUiState by elvm.uiState.collectAsStateWithLifecycle()

    val message = stringResource(R.string.snackbar_no_internet)
    val snackbarHostState = remember { SnackbarHostState() }
    val localCoroutineScope = rememberCoroutineScope()
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    elvm.observeLifecycleEvents(lifecycle)
    LaunchedEffect(Unit) {
        elvm.checkIfUserIsAdmin()
    }
    LaunchedEffect(Unit) {
        elvm.effect.collect { eff ->
            when (eff) {
                is MyEventListEffect.ShowNoNetworkPrompt -> {
                    localCoroutineScope.launch {
                        snackbarHostState.showSnackbar(message = message)
                    }
                }

                is MyEventListEffect.NavigateToEventDetail -> onEventClick(eff.event)
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
            onRetryClick = { elvm.loadMyEvents() }
        )
    }
}

@Composable
fun MyEventListView(
    uiState: MyEventListUiState,
    snackbarHostState: SnackbarHostState,
    onSignInClick: () -> Unit,
    onEventClick: (event: EventListUiModel) -> Unit,
    onRetryClick: () -> Unit,
    onAddEventClick: () -> Unit = {}
) {
    val listState = rememberLazyListState()
    var fabVisible by remember { mutableStateOf(true) }
    var lastScrollOffset by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        fabVisible = true
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
        }.collect { (index, offset) ->
            val currentScroll = index * 10000 + offset
            val goingDown = currentScroll > lastScrollOffset
            lastScrollOffset = currentScroll

            if (goingDown) {
                fabVisible = false
            }
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.isScrollInProgress
        }.collect { isScrolling ->
            if (!isScrolling) {
                fabVisible = true
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            if (uiState.isAdmin && fabVisible) {
                FloatingActionButton(
                    onClick = onAddEventClick,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Añadir evento"
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(
                top = innerPadding.calculateTopPadding()
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
                    MyEventListError.NOT_LOGGED_IN -> EsmorgaGuestError(stringResource(R.string.unauthenticated_error_title), stringResource(R.string.button_login), { onSignInClick() }, R.raw.oops)
                    MyEventListError.UNKNOWN -> EsmorgaGuestError(stringResource(R.string.default_error_title), stringResource(R.string.button_retry), { onRetryClick() }, R.raw.oops)
                    null -> EventList(
                        events = uiState.eventList,
                        onEventClick = onEventClick,
                        modifier = Modifier
                            .padding(horizontal = 16.dp),
                        listState = listState
                    )
                }
            }
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