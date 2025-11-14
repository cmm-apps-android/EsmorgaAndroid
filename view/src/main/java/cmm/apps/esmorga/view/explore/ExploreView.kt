package cmm.apps.esmorga.view.explore

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmm.apps.designsystem.EsmorgaButton
import cmm.apps.designsystem.EsmorgaLinearLoader
import cmm.apps.designsystem.EsmorgaText
import cmm.apps.designsystem.EsmorgaTextStyle
import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.Screen
import cmm.apps.esmorga.view.explore.ExploreScreenTestTags.EXPLORE_LIST_CARD_NAME
import cmm.apps.esmorga.view.explore.ExploreScreenTestTags.EXPLORE_TITLE
import cmm.apps.esmorga.view.explore.model.ExploreUiState
import cmm.apps.esmorga.view.explore.model.ExploreEffect
import cmm.apps.esmorga.view.explore.model.ListCardUiModel
import cmm.apps.esmorga.view.extensions.observeLifecycleEvents
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import cmm.apps.designsystem.R as DesignSystem

@Screen
@Composable
fun ExploreScreen(vm: ExploreViewModel = koinViewModel(), onEventClick: (event: Event) -> Unit) {
    val uiState: ExploreUiState by vm.uiState.collectAsStateWithLifecycle()

    val noInternetMessage = stringResource(R.string.snackbar_no_internet)
    val snackbarHostState = remember { SnackbarHostState() }
    val localCoroutineScope = rememberCoroutineScope()
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    vm.observeLifecycleEvents(lifecycle)
    LaunchedEffect(Unit) {
        vm.effect.collect { eff ->
            when (eff) {
                is ExploreEffect.ShowNoNetworkPrompt -> {
                    localCoroutineScope.launch {
                        snackbarHostState.showSnackbar(message = noInternetMessage)
                    }
                }

                is ExploreEffect.NavigateToEventDetail -> onEventClick(eff.event)

                is ExploreEffect.NavigateToPollDetail -> {
                    localCoroutineScope.launch {
                        snackbarHostState.showSnackbar(message = "Navigate to Poll")
                    }
                }
            }
        }
    }

    EsmorgaTheme {
        EventListView(
            uiState = uiState,
            snackbarHostState = snackbarHostState,
            onRetryClick = { vm.loadEventsAndPolls() },
            onEventClick = { vm.onEventClick(it) },
            onPollClick = { vm.onPollClick(it) }
        )
    }
}

@Composable
fun EventListView(
    uiState: ExploreUiState,
    snackbarHostState: SnackbarHostState,
    onRetryClick: () -> Unit,
    onEventClick: (event: ListCardUiModel) -> Unit,
    onPollClick: (event: ListCardUiModel) -> Unit
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(
                top = innerPadding.calculateTopPadding(),
            )
        ) {
            EsmorgaText(
                text = stringResource(R.string.title_explore),
                style = EsmorgaTextStyle.HEADING_1,
                modifier = Modifier
                    .padding(vertical = 32.dp, horizontal = 16.dp)
                    .testTag(EXPLORE_TITLE)
            )
            if (uiState.loading) {
                EventListLoading()
            } else {
                if (uiState.error.isNullOrBlank().not()) {
                    EventListError(onRetryClick)
                } else {
                    EventList(
                        showHeaders = true,
                        events = uiState.eventList,
                        polls = uiState.pollList,
                        onEventClick = onEventClick,
                        onPollClick = onPollClick,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun EventListLoading() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        EsmorgaText(text = stringResource(id = R.string.body_loader), style = EsmorgaTextStyle.HEADING_1, modifier = Modifier.padding(vertical = 16.dp))
        EsmorgaLinearLoader(modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun EventListEmpty() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Image(
            painter = painterResource(id = DesignSystem.drawable.img_event_list_empty),
            contentDescription = stringResource(id = R.string.text_empty_state),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
        )

        EsmorgaText(
            text = stringResource(R.string.text_empty_state),
            style = EsmorgaTextStyle.HEADING_2,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 32.dp, horizontal = 16.dp)
        )
    }
}

@Composable
fun EventListError(onRetryClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerLow)
                    .size(48.dp)
            ) {
                Image(
                    painter = painterResource(id = DesignSystem.drawable.ic_error),
                    contentDescription = stringResource(R.string.default_error_title),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                EsmorgaText(text = stringResource(R.string.default_error_title), style = EsmorgaTextStyle.HEADING_2, modifier = Modifier.padding(vertical = 4.dp))
                EsmorgaText(text = stringResource(R.string.default_error_body), style = EsmorgaTextStyle.BODY_1)
            }
        }

        Box(modifier = Modifier.height(32.dp))

        EsmorgaButton(text = stringResource(R.string.button_retry)) {
            onRetryClick()
        }
    }
}

@Composable
fun EventList(
    showHeaders: Boolean,
    events: List<ListCardUiModel>,
    polls: List<ListCardUiModel>,
    onEventClick: (event: ListCardUiModel) -> Unit,
    onPollClick: (card: ListCardUiModel) -> Unit,
    modifier: Modifier = Modifier,
    nestedScrollConnection: NestedScrollConnection? = null
) {
    LazyColumn(modifier = nestedScrollConnection?.let { Modifier.nestedScroll(it) } ?: run { Modifier }) {
        if (polls.isNotEmpty()) {
            if (showHeaders) {
                item {
                    ListHeader(text = stringResource(R.string.title_polls))
                }
            }

            items(
                items = polls,
                key = { poll -> "poll-${poll.id}" }
            ) { poll ->
                ListCard(
                    item = poll,
                    onCardClick = onPollClick,
                    modifier = modifier.animateItem()
                )
            }
        }

        if (showHeaders) {
            item {
                ListHeader(text = stringResource(R.string.title_events))
            }
        }
        if (events.isNotEmpty()) {
            items(
                items = events,
                key = { event -> "event-${event.id}" }
            ) { event ->
                ListCard(
                    item = event,
                    onCardClick = onEventClick,
                    modifier = modifier.animateItem()
                )
            }
        } else {
            item {
                EventListEmpty()
            }
        }
    }
}

@Composable
fun ListHeader(text: String) {
    EsmorgaText(
        text = text,
        style = EsmorgaTextStyle.HEADING_1,
        modifier = Modifier
            .padding(horizontal = 32.dp)
    )
    HorizontalDivider(color = MaterialTheme.colorScheme.primary, thickness = 1.dp, modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp))
}


@Composable
fun ListCard(item: ListCardUiModel, onCardClick: (card: ListCardUiModel) -> Unit, modifier: Modifier) {
    Column(
        modifier = modifier
            .padding(bottom = 32.dp)
            .clickable {
                onCardClick(item)
            }) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(item.imageUrl)
                //.crossfade(true) //Open bug in Coil https://github.com/coil-kt/coil/issues/1688 leads to image not being properly scaled if crossfade is used
                .build(),
            placeholder = painterResource(DesignSystem.drawable.img_event_list_empty),
            error = painterResource(DesignSystem.drawable.img_event_list_empty),
            contentDescription = stringResource(id = R.string.content_description_event_image).format(item.cardTitle),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
        )

        EsmorgaText(
            text = item.cardTitle,
            style = EsmorgaTextStyle.HEADING_2,
            modifier = Modifier
                .padding(vertical = 4.dp)
                .testTag("$EXPLORE_LIST_CARD_NAME - ${item.cardTitle}")
        )

        EsmorgaText(text = item.cardSubtitle1, style = EsmorgaTextStyle.BODY_1_ACCENT, modifier = Modifier.padding(vertical = 4.dp))

        item.cardSubtitle2?.let {
            EsmorgaText(text = item.cardSubtitle2, style = EsmorgaTextStyle.BODY_1_ACCENT, modifier = Modifier.padding(vertical = 4.dp))
        }
    }
}

object ExploreScreenTestTags {
    const val EXPLORE_TITLE = "explore screen title"
    const val EXPLORE_LIST_CARD_NAME = "explore card name"
}