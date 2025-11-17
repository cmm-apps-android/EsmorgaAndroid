package cmm.apps.esmorga.view.eventattendees

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmm.apps.designsystem.EsmorgaCheckbox
import cmm.apps.designsystem.EsmorgaLinearLoader
import cmm.apps.designsystem.EsmorgaText
import cmm.apps.designsystem.EsmorgaTextStyle
import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.Screen
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArguments
import cmm.apps.esmorga.view.eventattendees.model.AttendeeUiModel
import cmm.apps.esmorga.view.eventattendees.model.EventAttendeesEffect
import cmm.apps.esmorga.view.eventattendees.model.EventAttendeesUiState
import cmm.apps.esmorga.view.extensions.observeLifecycleEvents
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Screen
@Composable
fun EventAttendeesScreen(
    event: Event,
    eavm: EventAttendeesViewModel = koinViewModel(parameters = { parametersOf(event) }),
    onBackPressed: () -> Unit,
    onError: (EsmorgaErrorScreenArguments) -> Unit
) {
    val uiState: EventAttendeesUiState by eavm.uiState.collectAsStateWithLifecycle()
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    eavm.observeLifecycleEvents(lifecycle)
    LaunchedEffect(Unit) {
        eavm.effect.collect { eff ->
            when (eff) {
                is EventAttendeesEffect.NavigateBack -> {
                    onBackPressed()
                }

                is EventAttendeesEffect.ShowFullScreenError -> {
                    onError(eff.esmorgaErrorScreenArguments)
                }

                is EventAttendeesEffect.ShowNoNetworkError -> {
                    onBackPressed()
                    onError(eff.esmorgaNoNetworkArguments)
                }
            }
        }
    }

    EsmorgaTheme {
        EventAttendeesView(
            uiState = uiState,
            onAttendeeChecked = { pos, checked -> eavm.onAttendeeChecked(pos, checked) },
            onBackPressed = {
                eavm.onBackPressed()
            }
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventAttendeesView(
    uiState: EventAttendeesUiState,
    onAttendeeChecked: (Int, Boolean) -> Unit,
    onBackPressed: () -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(
            title = {},
            navigationIcon = {
                IconButton(
                    onClick = { onBackPressed() }, modifier = Modifier.testTag(EventAttendessScreenTestTags.EVENT_ATTENDEES_BACK_BUTTON)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.content_description_back_icon)
                    )
                }
            },
        )
    }) { innerPadding ->
        Column(
            modifier = Modifier.padding(top = innerPadding.calculateTopPadding())
        ) {
            EsmorgaText(
                text = stringResource(id = R.string.title_event_attendees),
                style = EsmorgaTextStyle.TITLE, modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 32.dp)
                    .testTag(EventAttendessScreenTestTags.EVENT_ATTENDEES_TITLE)
            )

            if (uiState.loading) {
                AttendeeListLoading()
            } else {
                AttendeeList(attendees = uiState.attendeeList, shouldShowChecked = uiState.showChecked, onAttendeeChecked = onAttendeeChecked)
            }

        }
    }
}

@Composable
fun AttendeeListLoading() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        EsmorgaText(text = stringResource(id = R.string.body_loader), style = EsmorgaTextStyle.HEADING_1, modifier = Modifier.padding(bottom = 16.dp))
        EsmorgaLinearLoader(modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun AttendeeList(
    attendees: List<AttendeeUiModel>,
    shouldShowChecked: Boolean,
    onAttendeeChecked: (Int, Boolean) -> Unit,
    modifier: Modifier = Modifier, nestedScrollConnection: NestedScrollConnection? = null
) {

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.padding(bottom = 32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            EsmorgaText(
                text = stringResource(id = R.string.title_name),
                style = EsmorgaTextStyle.HEADING_1,
                modifier = Modifier.padding(start = 24.dp)
            )

            if (shouldShowChecked) {
                Spacer(modifier = Modifier.weight(1f))
                EsmorgaText(
                    text = stringResource(id = R.string.title_payment_status),
                    style = EsmorgaTextStyle.HEADING_1,
                    modifier = Modifier.padding(end = 24.dp)
                )
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.secondary, thickness = 1.dp)

        LazyColumn(modifier = nestedScrollConnection?.let { Modifier.nestedScroll(it) } ?: run { Modifier }) {
            items(attendees.size) { pos ->
                val attendee = attendees[pos]
                var checked by remember { mutableStateOf(attendee.checked) }

                Row(
                    modifier = Modifier.padding(start = 32.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    EsmorgaText(
                        text = "${pos + 1}. ${attendee.name}",
                        style = EsmorgaTextStyle.BODY_1,
                        modifier = Modifier.padding(vertical = 16.dp),
                    )

                    if (shouldShowChecked) {
                        Spacer(modifier = Modifier.weight(1f))
                        EsmorgaCheckbox(
                            checked = checked,
                            onCheckedChanged = {
                                checked = it
                                onAttendeeChecked(pos, checked)
                            }
                        )
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.secondary, thickness = 1.dp)
            }
        }
    }
}

object EventAttendessScreenTestTags {
    const val EVENT_ATTENDEES_TITLE = "event attendees title"
    const val EVENT_ATTENDEES_BACK_BUTTON = "event attendees back button"
}