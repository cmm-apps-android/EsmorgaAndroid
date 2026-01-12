package cmm.apps.esmorga.view.createeventlocation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cmm.apps.designsystem.EsmorgaButton
import cmm.apps.designsystem.EsmorgaText
import cmm.apps.designsystem.EsmorgaTextField
import cmm.apps.designsystem.EsmorgaTextStyle
import cmm.apps.esmorga.domain.event.model.CreateEventForm
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.Screen
import cmm.apps.esmorga.view.createeventlocation.model.CreateEventFormLocationEffect
import cmm.apps.esmorga.view.createeventlocation.model.CreateEventFormLocationUiState
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Screen
@Composable
fun CreateEventFormLocationScreen(
    eventForm: CreateEventForm,
    viewModel: CreateEventFormLocationViewModel = koinViewModel(parameters = { parametersOf(eventForm) }),
    onBackPressed: () -> Unit,
    onNextClick: (CreateEventForm) -> Unit
) {
    val uiState: CreateEventFormLocationUiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { eff ->
            when (eff) {
                is CreateEventFormLocationEffect.NavigateNext -> onNextClick(eff.eventForm)
                is CreateEventFormLocationEffect.NavigateBack -> onBackPressed()
            }
        }
    }

    EsmorgaTheme {
        CreateEventFormLocationView(
            uiState = uiState,
            onBackPressed = { viewModel.onBackClick() },
            onLocationChange = viewModel::onLocationChanged,
            onCoordinatesChange = viewModel::onCoordinatesChanged,
            onMaxCapacityChange = viewModel::onMaxCapacityChanged,
            onNextClick = viewModel::onNextClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventFormLocationView(
    uiState: CreateEventFormLocationUiState,
    onBackPressed: () -> Unit,
    onLocationChange: (String) -> Unit,
    onCoordinatesChange: (String) -> Unit,
    onMaxCapacityChange: (String) -> Unit,
    onNextClick: () -> Unit
) {
    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = onBackPressed,
                        modifier = Modifier.testTag(CreateEventLocationScreenTestTags.BACK_BUTTON)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.content_description_back_icon)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            EsmorgaText(
                text = stringResource(R.string.screen_create_event_title),
                style = EsmorgaTextStyle.HEADING_1,
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .testTag(CreateEventLocationScreenTestTags.TITLE)
            )

            EsmorgaTextField(
                value = uiState.localizationName,
                onValueChange = onLocationChange,
                title = R.string.field_title_event_location,
                placeholder = R.string.placeholder_event_location,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(CreateEventLocationScreenTestTags.LOCATION_FIELD)
            )

            Spacer(modifier = Modifier.height(16.dp))

            EsmorgaTextField(
                value = uiState.localizationCoordinates,
                onValueChange = onCoordinatesChange,
                title = R.string.field_title_event_coordinates,
                placeholder = R.string.placeholder_event_coordinates,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(CreateEventLocationScreenTestTags.COORDINATES_FIELD)
            )

            Spacer(modifier = Modifier.height(16.dp))

            EsmorgaTextField(
                value = uiState.eventMaxCapacity,
                onValueChange = onMaxCapacityChange,
                title = R.string.field_title_event_max_capacity,
                placeholder = R.string.placeholder_event_max_capacity,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(CreateEventLocationScreenTestTags.MAX_CAPACITY_FIELD),
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
            )

            EsmorgaButton(
                text = stringResource(R.string.step_continue_button),
                isEnabled = uiState.isButtonEnabled,
                onClick = onNextClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .testTag(CreateEventLocationScreenTestTags.NEXT_BUTTON)
            )
        }
    }
}

object CreateEventLocationScreenTestTags {
    const val TITLE = "create_event_location_title"
    const val BACK_BUTTON = "create_event_location_back_button"
    const val NEXT_BUTTON = "create_event_location_next_button"
    const val LOCATION_FIELD = "create_event_location_field"
    const val COORDINATES_FIELD = "create_event_coordinates_field"
    const val MAX_CAPACITY_FIELD = "create_event_max_capacity_field"
}
