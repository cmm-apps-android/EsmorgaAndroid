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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import cmm.apps.esmorga.view.eventdetails.EventDetailsScreenTestTags.EVENT_DETAILS_BACK_BUTTON
import cmm.apps.esmorga.view.eventdetails.model.EventDetailsEffect
import cmm.apps.esmorga.view.eventdetails.model.EventDetailsUiState
import cmm.apps.esmorga.view.navigation.openNavigationApp
import cmm.apps.esmorga.view.registration.RegistrationScreenTestTags.REGISTRATION_BACK_BUTTON
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf


@Screen
@Composable
fun EventDetailsScreen(eventId: String, onBackPressed: () -> Unit, edvm: EventDetailsViewModel = koinViewModel(parameters = { parametersOf(eventId) })) {
    val uiState: EventDetailsUiState by edvm.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        edvm.effect.collect { eff ->
            when (eff) {
                is EventDetailsEffect.NavigateToLocation -> {
                    openNavigationApp(context, eff.lat, eff.lng)
                }

                is EventDetailsEffect.NavigateBack -> {
                    onBackPressed()
                }
            }
        }
    }
    EsmorgaTheme {
        EventDetailsView(
            uiState = uiState,
            onNavigateClicked = {
                edvm.onNavigateClick()
            },
            onBackPressed = {
                edvm.onBackPressed()
            }
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailsView(uiState: EventDetailsUiState, onNavigateClicked: () -> Unit, onBackPressed: () -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { onBackPressed() },
                        modifier = Modifier.testTag(EVENT_DETAILS_BACK_BUTTON)) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to event list"
                        )
                    }
                },
            )
        }
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
                    .crossfade(true)
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
                    primary = false
                ) {
                    onNavigateClicked()
                }
            }

        }
    }
}

object EventDetailsScreenTestTags {
    const val EVENT_DETAILS_EVENT_NAME = "event details event name"
    const val EVENT_DETAILS_BACK_BUTTON = "event details back button"
}