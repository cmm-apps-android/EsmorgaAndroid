package cmm.apps.esmorga.view.eventdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmm.apps.designsystem.EsmorgaButton
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.eventdetails.model.EventDetailsEffect
import cmm.apps.esmorga.view.eventdetails.model.EventDetailsUiState
import cmm.apps.esmorga.view.navigation.openNavigationApp
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import cmm.apps.esmorga.view.theme.Sepia
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.koin.androidx.compose.koinViewModel


@Composable
fun EventDetailsScreen(eventId: String, onBackPressed: () -> Unit, edvm: EventDetailsViewModel = koinViewModel()) {
    val uiState: EventDetailsUiState by edvm.uiState.collectAsStateWithLifecycle()
    edvm.loadEventDetails(eventId)
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
                    IconButton(onClick = {
                        onBackPressed()
                    }) {
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
            modifier = Modifier.padding(
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding()
            )
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(uiState.image)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.img_event_list_empty),
                error = painterResource(R.drawable.img_event_list_empty),
                contentDescription = stringResource(id = R.string.event_image_content_description).format(uiState.title),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16 / 9f)
            )
            Text(
                text = uiState.title,
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier
                    .padding(top = 32.dp, start = 16.dp, bottom = 16.dp, end = 16.dp)
            )
            Text(
                text = uiState.subtitle,
                color = Sepia,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Text(
                text = stringResource(id = R.string.event_details_description),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 16.dp, top = 32.dp, end = 16.dp)
            )
            Text(
                text = uiState.description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )
            Text(
                text = stringResource(id = R.string.event_details_location),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )
            Text(
                text = uiState.locationName,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            if (uiState.navigateButton) {
                EsmorgaButton(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 32.dp),
                    text = stringResource(id = R.string.navigate),
                    primary = false
                ) {
                    onNavigateClicked()
                }
            }

        }
    }
}