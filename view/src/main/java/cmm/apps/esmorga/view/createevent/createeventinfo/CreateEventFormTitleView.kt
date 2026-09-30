package cmm.apps.esmorga.view.createevent.createeventinfo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmm.apps.designsystem.EsmorgaButton
import cmm.apps.designsystem.EsmorgaText
import cmm.apps.designsystem.EsmorgaTextField
import cmm.apps.designsystem.EsmorgaTextStyle
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.Screen
import cmm.apps.esmorga.view.createevent.createeventinfo.model.CreateEventFormEffect
import cmm.apps.esmorga.view.createevent.createeventinfo.model.CreateEventFormUiState
import cmm.apps.esmorga.view.theme.EsmorgaTheme

@Screen
@Composable
fun CreateEventFormScreen(
    viewModel: CreateEventFormTitleViewModel,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    val uiState: CreateEventFormUiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is CreateEventFormEffect.NavigateBack -> onBack()
                is CreateEventFormEffect.NavigateNext -> onNext()
            }
        }
    }

    EsmorgaTheme {
        CreateEventFormTitleScreenContent(
            uiState = uiState,
            onEventNameChange = viewModel::onEventNameChange,
            onDescriptionChange = viewModel::onDescriptionChange,
            onBackClick = viewModel::onBackClick,
            onNextClick = viewModel::onNextClick
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventFormTitleScreenContent(
    uiState: CreateEventFormUiState,
    onEventNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackClick, modifier = Modifier.testTag(CreateEventFormTitleScreenTestTags.CREATE_EVENT_FORM_BACK_BUTTON)) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.content_description_back_icon))
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
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .testTag(CreateEventFormTitleScreenTestTags.CREATE_EVENT_FORM_TITLE)
            )

            EsmorgaTextField(
                value = uiState.eventName,
                onValueChange = onEventNameChange,
                title = R.string.field_title_event_name,
                placeholder = R.string.placeholder_event_name,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(CreateEventFormTitleScreenTestTags.CREATE_EVENT_FORM_NAME),
                errorText = uiState.eventNameError?.let { stringResource(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            EsmorgaTextField(
                value = uiState.eventDescription.orEmpty(),
                onValueChange = onDescriptionChange,
                title = R.string.field_title_event_description,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp)
                    .testTag(CreateEventFormTitleScreenTestTags.CREATE_EVENT_FORM_DESCRIPTION),
                singleLine = false,
                maxChars = 5000,
                placeholder = R.string.placeholder_event_description,
                errorText = uiState.descriptionError?.let { stringResource(it) }
            )

            EsmorgaButton(
                text = stringResource(id = R.string.step_continue_button),
                isEnabled = uiState.isFormValid,
                onClick = onNextClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .testTag(CreateEventFormTitleScreenTestTags.CREATE_EVENT_FORM_NEXT_BUTTON),
            )
        }
    }
}

object CreateEventFormTitleScreenTestTags {
    const val CREATE_EVENT_FORM_TITLE = "create_event_form_title"
    const val CREATE_EVENT_FORM_BACK_BUTTON = "create_event_form_back_button"
    const val CREATE_EVENT_FORM_NEXT_BUTTON = "create_event_form_next_button"
    const val CREATE_EVENT_FORM_DESCRIPTION = "create_event_form_description"
    const val CREATE_EVENT_FORM_NAME = "create_event_form_name"

}
