package cmm.apps.esmorga.view.eventdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmm.apps.designsystem.EsmorgaButton
import cmm.apps.designsystem.EsmorgaText
import cmm.apps.designsystem.EsmorgaTextStyle
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.Screen
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArguments
import cmm.apps.esmorga.view.eventdetails.EventDetailsScreenTestTags.EVENT_DETAILS_BACK_BUTTON
import cmm.apps.esmorga.view.eventdetails.model.EventDetailsEffect
import cmm.apps.esmorga.view.eventdetails.model.EventDetailsUiState
import cmm.apps.esmorga.view.eventlist.model.EventListUiModel
import cmm.apps.esmorga.view.navigation.openNavigationApp
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf


@Screen
@Composable
fun EventDetailsScreen(
    event: EventListUiModel,
    edvm: EventDetailsViewModel = koinViewModel(parameters = { parametersOf(event) }),
    onBackPressed: () -> Unit,
    onLoginClicked: () -> Unit,
    onJoinEventError: (EsmorgaErrorScreenArguments) -> Unit,
    onNoNetworkError: (EsmorgaErrorScreenArguments) -> Unit
) {
    val uiState: EventDetailsUiState by edvm.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val joinEventSuccessMessage = stringResource(R.string.snackbar_event_joined)
    val leaveEventSuccessMessage = stringResource(R.string.snackbar_event_left)
    val snackbarHostState = remember { SnackbarHostState() }

    val localCoroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        edvm.effect.collect { eff ->
            when (eff) {
                is EventDetailsEffect.NavigateToLocation -> {
                    openNavigationApp(context, eff.lat, eff.lng)
                }

                is EventDetailsEffect.NavigateBack -> {
                    onBackPressed()
                }

                is EventDetailsEffect.NavigateToLoginScreen -> {
                    onLoginClicked()
                }

                EventDetailsEffect.ShowJoinEventSuccess -> {
                    localCoroutineScope.launch {
                        snackbarHostState.showSnackbar(message = joinEventSuccessMessage)
                    }
                }

                is EventDetailsEffect.ShowFullScreenError -> {
                    onJoinEventError(eff.esmorgaErrorScreenArguments)
                }

                is EventDetailsEffect.ShowNoNetworkError -> {
                    onNoNetworkError(eff.esmorgaNoNetworkArguments)
                }

                EventDetailsEffect.ShowLeaveEventSuccess -> {
                    localCoroutineScope.launch {
                        snackbarHostState.showSnackbar(leaveEventSuccessMessage)
                    }
                }
            }
        }
    }
    EsmorgaTheme {
        EventDetailsView(
            uiState = uiState,
            snackbarHostState = snackbarHostState,
            onNavigateClicked = {
                edvm.onNavigateClick()
            },
            onBackPressed = {
                edvm.onBackPressed()
            },
            onPrimaryButtonClicked = {
                edvm.onPrimaryButtonClicked()
            }
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailsView(
    uiState: EventDetailsUiState,
    snackbarHostState: SnackbarHostState,
    onNavigateClicked: () -> Unit,
    onBackPressed: () -> Unit,
    onPrimaryButtonClicked: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = { onBackPressed() },
                        modifier = Modifier.testTag(EVENT_DETAILS_BACK_BUTTON)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.content_description_back_icon)
                        )
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding()
                )
                .verticalScroll(state = rememberScrollState())
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(uiState.image)
                    //.crossfade(true) //Open bug in Coil https://github.com/coil-kt/coil/issues/1688 leads to image not being properly scaled if crossfade is used
                    .build(),
                placeholder = painterResource(R.drawable.img_event_list_empty),
                error = painterResource(R.drawable.img_event_list_empty),
                contentDescription = stringResource(id = R.string.content_description_event_image).format(uiState.title),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16 / 9f)
            )
            EsmorgaText(
                text = uiState.title,
                style = EsmorgaTextStyle.TITLE,
                modifier = Modifier
                    .padding(top = 32.dp, start = 16.dp, bottom = 16.dp, end = 16.dp)
                    .testTag(EventDetailsScreenTestTags.EVENT_DETAILS_EVENT_NAME)
            )
            EsmorgaText(text = uiState.subtitle, style = EsmorgaTextStyle.BODY_1_ACCENT, modifier = Modifier.padding(horizontal = 16.dp))
            EsmorgaText(
                text = stringResource(id = R.string.screen_event_details_description),
                style = EsmorgaTextStyle.HEADING_1,
                modifier = Modifier.padding(start = 16.dp, top = 32.dp, end = 16.dp)
            )
            EsmorgaText(
                text = uiState.description, style = EsmorgaTextStyle.BODY_1, modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )
            EsmorgaText(
                text = stringResource(id = R.string.screen_event_details_location),
                style = EsmorgaTextStyle.HEADING_1,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )
            EsmorgaText(
                text = uiState.locationName, style = EsmorgaTextStyle.BODY_1, modifier = Modifier.padding(horizontal = 16.dp)
            )
            if (uiState.navigateButton) {
                EsmorgaButton(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 32.dp),
                    text = stringResource(id = R.string.button_navigate),
                    primary = false,
                    isEnabled = !uiState.primaryButtonLoading
                ) {
                    onNavigateClicked()
                }
            }

            EsmorgaButton(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = if (!uiState.navigateButton) 32.dp else 0.dp)
                    .testTag(EventDetailsScreenTestTags.EVENT_DETAIL_PRIMARY_BUTTON),
                text = uiState.primaryButtonTitle,
                primary = true,
                isLoading = uiState.primaryButtonLoading
            ) {
                onPrimaryButtonClicked()
            }

        }
    }
}

object EventDetailsScreenTestTags {
    const val EVENT_DETAILS_EVENT_NAME = "event details event name"
    const val EVENT_DETAILS_BACK_BUTTON = "event details back button"
    const val EVENT_DETAIL_PRIMARY_BUTTON = "event_detail_primary_button"
}