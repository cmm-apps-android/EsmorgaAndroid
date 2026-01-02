package cmm.apps.esmorga.view.eventdetails

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmm.apps.designsystem.ButtonUiState
import cmm.apps.designsystem.Disabled
import cmm.apps.designsystem.Enabled
import cmm.apps.designsystem.EsmorgaButton
import cmm.apps.designsystem.EsmorgaText
import cmm.apps.designsystem.EsmorgaTextStyle
import cmm.apps.designsystem.Loading
import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.Screen
import cmm.apps.esmorga.view.details.DetailsDescriptionSection
import cmm.apps.esmorga.view.details.DetailsHeaderSection
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArguments
import cmm.apps.esmorga.view.eventdetails.EventDetailsScreenTestTags.EVENT_DETAILS_BACK_BUTTON
import cmm.apps.esmorga.view.eventdetails.EventDetailsScreenTestTags.EVENT_DETAILS_EVENT_NAME
import cmm.apps.esmorga.view.eventdetails.model.EventDetailsEffect
import cmm.apps.esmorga.view.eventdetails.model.EventDetailsUiState
import cmm.apps.esmorga.view.extensions.observeLifecycleEvents
import cmm.apps.esmorga.view.navigation.openNavigationApp
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import cmm.apps.designsystem.R as DesignSystem

@Screen
@Composable
fun EventDetailsScreen(
    event: Event,
    edvm: EventDetailsViewModel = koinViewModel(parameters = { parametersOf(event) }),
    onBackPressed: () -> Unit,
    onLoginClicked: () -> Unit,
    onViewAttendeesClicked: (event: Event) -> Unit,
    onJoinEventError: (EsmorgaErrorScreenArguments) -> Unit,
    onNoNetworkError: (EsmorgaErrorScreenArguments) -> Unit
) {
    val uiState: EventDetailsUiState by edvm.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val joinEventSuccessMessage = stringResource(R.string.snackbar_event_joined)
    val leaveEventSuccessMessage = stringResource(R.string.snackbar_event_left)
    val eventFullErrorMessage = stringResource(R.string.snackbar_event_full)
    val snackbarHostState = remember { SnackbarHostState() }
    val localCoroutineScope = rememberCoroutineScope()
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    edvm.observeLifecycleEvents(lifecycle)
    LaunchedEffect(Unit) {
        edvm.effect.collect { eff ->
            when (eff) {
                is EventDetailsEffect.NavigateToLocation -> {
                    openNavigationApp(context, eff.lat, eff.lng, eff.locationName)
                }

                is EventDetailsEffect.NavigateBack -> {
                    onBackPressed()
                }

                is EventDetailsEffect.NavigateToLoginScreen -> {
                    onLoginClicked()
                }

                is EventDetailsEffect.NavigateToAttendeesScreen -> {
                    onViewAttendeesClicked(event)
                }

                is EventDetailsEffect.ShowJoinEventSuccess -> {
                    localCoroutineScope.launch {
                        snackbarHostState.showSnackbar(message = joinEventSuccessMessage)
                    }
                }

                EventDetailsEffect.ShowEventFullError -> {
                    localCoroutineScope.launch {
                        snackbarHostState.showSnackbar(message = eventFullErrorMessage)
                    }
                }

                is EventDetailsEffect.ShowFullScreenError -> {
                    onJoinEventError(eff.esmorgaErrorScreenArguments)
                }

                is EventDetailsEffect.ShowNoNetworkError -> {
                    onNoNetworkError(eff.esmorgaNoNetworkArguments)
                }

                is EventDetailsEffect.ShowLeaveEventSuccess -> {
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
            onNavigateClicked = { edvm.onNavigateClick() },
            onPrimaryButtonClicked = { edvm.onPrimaryButtonClicked() },
            onViewAttendeesClicked = { edvm.onViewAttendeesClicked() },
            onBackPressed = { edvm.onBackPressed() })
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailsView(
    uiState: EventDetailsUiState,
    snackbarHostState: SnackbarHostState,
    onNavigateClicked: () -> Unit,
    onPrimaryButtonClicked: () -> Unit,
    onViewAttendeesClicked: () -> Unit,
    onBackPressed: () -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(
            title = {},
            navigationIcon = {
                IconButton(
                    onClick = { onBackPressed() }, modifier = Modifier.testTag(EVENT_DETAILS_BACK_BUTTON)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.content_description_back_icon)
                    )
                }
            },
        )
    }, snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(
                    top = innerPadding.calculateTopPadding()
                )
                .verticalScroll(state = rememberScrollState())
        ) {
            DetailsHeaderSection(image = uiState.image, title = uiState.title, date = uiState.date, titleTestTag = EVENT_DETAILS_EVENT_NAME)

            EventDetailsAttendeesSection(
                attendeeCountText = uiState.currentAttendeeCountText,
                showViewAttendeesButton = uiState.showViewAttendeesButton,
                joinDeadline = uiState.joinDeadline,
                onViewAttendeesClicked = onViewAttendeesClicked
            )

            DetailsDescriptionSection(description = uiState.description, locationName = uiState.locationName)

            EventDetailsButtonSection(
                showNavigateButton = uiState.showNavigateButton,
                primaryButtonState = uiState.primaryButtonState,
                onNavigateClicked = onNavigateClicked,
                onPrimaryButtonClicked = onPrimaryButtonClicked
            )
        }
    }
}


@Composable
fun EventDetailsAttendeesSection(
    attendeeCountText: String?,
    showViewAttendeesButton: Boolean,
    joinDeadline: String,
    onViewAttendeesClicked: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
    ) {
        attendeeCountText?.let { max ->
            Icon(painter = painterResource(DesignSystem.drawable.group), contentDescription = null)
            Spacer(modifier = Modifier.width(5.dp))
            EsmorgaText(
                text = attendeeCountText,
                style = EsmorgaTextStyle.CAPTION,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        if (showViewAttendeesButton) {
            EsmorgaText(
                text = stringResource(R.string.button_view_attendees),
                style = EsmorgaTextStyle.CAPTION_UNDERSCORE,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clickable { onViewAttendeesClicked() }
                    .testTag(EventDetailsScreenTestTags.EVENT_DETAILS_ATTENDEES_BUTTON)
            )
        }
    }

    EsmorgaText(
        text = stringResource(id = R.string.screen_event_details_join_deadline, joinDeadline),
        style = EsmorgaTextStyle.CAPTION,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 4.dp)
    )
}

@Composable
fun EventDetailsButtonSection(
    showNavigateButton: Boolean,
    primaryButtonState: ButtonUiState,
    onNavigateClicked: () -> Unit,
    onPrimaryButtonClicked: () -> Unit
) {
    if (showNavigateButton) {
        val navigateText = stringResource(id = R.string.button_navigate)
        EsmorgaButton(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 32.dp),
            state = if (primaryButtonState is Loading) Disabled(navigateText) else Enabled(navigateText),
            primary = false
        ) {
            onNavigateClicked()
        }
    }

    EsmorgaButton(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .testTag(EventDetailsScreenTestTags.EVENT_DETAILS_PRIMARY_BUTTON),
        state = primaryButtonState,
        primary = true
    ) {
        onPrimaryButtonClicked()
    }
}

object EventDetailsScreenTestTags {
    const val EVENT_DETAILS_EVENT_NAME = "event details event name"
    const val EVENT_DETAILS_BACK_BUTTON = "event details back button"
    const val EVENT_DETAILS_PRIMARY_BUTTON = "event details primary button"
    const val EVENT_DETAILS_ATTENDEES_BUTTON = "event details attendee button"
}