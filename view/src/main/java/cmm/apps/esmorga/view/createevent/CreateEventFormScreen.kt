package cmm.apps.esmorga.view.createevent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cmm.apps.designsystem.EsmorgaButton
import cmm.apps.designsystem.EsmorgaText
import cmm.apps.designsystem.EsmorgaTextField
import cmm.apps.designsystem.EsmorgaTextStyle
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.createevent.model.CreateEventFormEffect
import org.koin.androidx.compose.koinViewModel


@Composable
fun CreateEventFormScreen(
    viewModel: CreateEventFormViewModel = koinViewModel(),
    onBack: () -> Unit,
    onNext: (String, String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is CreateEventFormEffect.NavigateBack -> onBack()
                is CreateEventFormEffect.NavigateEventType -> onNext(
                    uiState.eventName.trim(),
                    uiState.description.trim()
                )
            }
        }
    }

    CreateEventFormScreenContent(
        eventName = uiState.eventName,
        onEventNameChange = viewModel::onEventNameChange,
        eventNameError = uiState.eventNameError,
        description = uiState.description,
        onDescriptionChange = viewModel::onDescriptionChange,
        descriptionError = uiState.descriptionError,
        isFormValid = uiState.isFormValid,
        onBackClick = { viewModel.onBackClick() },
        onNextClick = { viewModel.onNextClick() }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventFormScreenContent(
    eventName: String,
    onEventNameChange: (String) -> Unit,
    eventNameError: Int?,
    description: String,
    onDescriptionChange: (String) -> Unit,
    descriptionError: Int?,
    isFormValid: Boolean,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
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
                modifier = Modifier.padding(bottom = 12.dp)
            )

            EsmorgaTextField(
                value = eventName,
                onValueChange = onEventNameChange,
                title = R.string.field_title_event_name,
                placeholder = R.string.placeholder_event_name,
                modifier = Modifier.fillMaxWidth(),
                maxChars = 100,
                errorText = eventNameError?.let { stringResource(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            EsmorgaTextField(
                value = description,
                onValueChange = onDescriptionChange,
                title = R.string.field_title_event_description,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp),
                singleLine = false,
                maxChars = 5000,
                placeholder = R.string.placeholder_event_name,
                errorText = descriptionError?.let { stringResource(it) }
            )

                EsmorgaButton(
                    text = stringResource(id = R.string.step_continue_button),
                    isEnabled = isFormValid,
                    onClick = onNextClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                )
            }
        }

}