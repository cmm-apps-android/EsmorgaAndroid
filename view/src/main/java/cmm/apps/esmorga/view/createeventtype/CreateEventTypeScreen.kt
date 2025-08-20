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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cmm.apps.designsystem.EsmorgaButton
import cmm.apps.designsystem.EsmorgaRadioButton
import cmm.apps.designsystem.EsmorgaText
import cmm.apps.designsystem.EsmorgaTextStyle
import cmm.apps.esmorga.view.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventTypeScreen(
    viewModel: CreateEventTypeViewModel,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
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
                modifier = Modifier.padding(bottom = 12.dp)
            )
            EsmorgaText(text = stringResource(R.string.step_2_screen_titlle), style = EsmorgaTextStyle.BODY_1, modifier = Modifier .padding(bottom = 15.dp))

            EventType.values().forEach { type ->
                EsmorgaRadioButton(
                    text = stringResource(type.uiTextRes),
                    selected = uiState.selectedEventType == type,
                    onClick = { viewModel.onEventTypeSelected(type) },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            EsmorgaButton(
                onClick = onNextClick,
                text = stringResource(R.string.step_continue_button),
                modifier = Modifier.padding(top = 54.dp)
            )
        }
    }
}
