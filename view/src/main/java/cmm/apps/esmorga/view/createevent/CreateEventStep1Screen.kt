package cmm.apps.esmorga.view.createevent

import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cmm.apps.designsystem.EsmorgaButton
import cmm.apps.designsystem.EsmorgaDescriptionTextField
import cmm.apps.designsystem.EsmorgaText
import cmm.apps.designsystem.EsmorgaTextField
import cmm.apps.designsystem.EsmorgaTextStyle
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.createevent.model.CreateEventStep1Effect
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventStep1Screen(
    viewModel: CreateEventStep1ViewModel = koinViewModel(),
    onBack: () -> Unit,
    onNext: (String, String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is CreateEventStep1Effect.NavigateBack -> onBack()
                is CreateEventStep1Effect.NavigateToStep2 -> onNext(
                    uiState.eventName.trim(),
                    uiState.description.trim()
                )
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { viewModel.onBackClick() }) {
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
                value = uiState.eventName,
                onValueChange = viewModel::onEventNameChange,
                title = R.string.field_title_event_name,
                placeholder = R.string.placeholder_event_name,
                modifier = Modifier.fillMaxWidth(),
                errorText = uiState.eventNameError
            )

            Spacer(modifier = Modifier.height(16.dp))

            EsmorgaDescriptionTextField(
                value = uiState.description,
                onValueChange = viewModel::onDescriptionChange,
                title = R.string.field_title_event_description,
                errorText = uiState.descriptionError.toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp),
                singleLine = false
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                EsmorgaButton(
                    text = stringResource(id = R.string.step_continue_button),
                    isEnabled = uiState.isFormValid,
                    onClick = { viewModel.onNextClick() }
                )
            }
        }
    }
}