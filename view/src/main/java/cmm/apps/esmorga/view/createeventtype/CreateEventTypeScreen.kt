package cmm.apps.esmorga.view.createeventtype

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmm.apps.designsystem.EsmorgaButton
import cmm.apps.designsystem.EsmorgaRadioButton
import cmm.apps.designsystem.EsmorgaText
import cmm.apps.designsystem.EsmorgaTextStyle
import cmm.apps.esmorga.domain.event.model.EventType
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.createevent.model.CreateEventFormUiModel
import cmm.apps.esmorga.view.createeventtype.model.CreateEventTypeScreenEffect
import cmm.apps.esmorga.view.createeventtype.model.CreateEventTypeScreenUiState
import cmm.apps.esmorga.view.createeventtype.model.EventTypeHelper
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventTypeScreen(
    eventForm: CreateEventFormUiModel,
    createEventviewModel: CreateEventTypeViewModel = koinViewModel(parameters = { parametersOf(eventForm) }),
    onBackClick: () -> Unit,
    onNextClick: (CreateEventFormUiModel) -> Unit
) {
    var selectedEventType by remember { mutableStateOf(eventForm.type ?: EventType.PARTY) }
    val uiState: CreateEventTypeScreenUiState by createEventviewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        createEventviewModel.effect.collect { eff ->
            when (eff) {
                is CreateEventTypeScreenEffect.NavigateBack -> onBackClick()
                is CreateEventTypeScreenEffect.NavigateNext -> onNextClick(eff.eventForm)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = { createEventviewModel.onBackClick() },
                        modifier = Modifier.testTag(CreateEventTypeScreenTestTags.CREATE_EVENT_TYPE_BACK_BUTTON)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
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
                    .padding(bottom = 16.dp)
                    .testTag(CreateEventTypeScreenTestTags.CREATE_EVENT_TYPE_TITLE)
            )

            EsmorgaText(
                text = stringResource(R.string.step_2_screen_titlle),
                style = EsmorgaTextStyle.BODY_1,
                modifier = Modifier.padding(bottom = 15.dp)
            )

            EventType.values().forEach { type ->
                EsmorgaRadioButton(
                    text = EventTypeHelper.getUiTextRes(type),
                    selected = uiState.type == type,
                    onClick = { createEventviewModel.onEventTypeSelected(type) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            EsmorgaButton(
                onClick = { createEventviewModel.onNextClick() },
                text = stringResource(R.string.step_continue_button),
                modifier = Modifier
                    .testTag(CreateEventTypeScreenTestTags.CREATE_EVENT_TYPE_NEXT_BUTTON)
                    .padding(vertical = 32.dp)
            )

        }
    }
}


object CreateEventTypeScreenTestTags {
    const val CREATE_EVENT_TYPE_TITLE = "create_event_type_title"
    const val CREATE_EVENT_TYPE_BACK_BUTTON = "create_event_type_back_button"
    const val CREATE_EVENT_TYPE_NEXT_BUTTON = "create_event_type_next_button"
}