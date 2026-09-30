package cmm.apps.esmorga.view.createevent.createeventdate

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmm.apps.designsystem.DeadlineSelectableDates
import cmm.apps.designsystem.EsmorgaButton
import cmm.apps.designsystem.EsmorgaDatePicker
import cmm.apps.designsystem.EsmorgaRow
import cmm.apps.designsystem.EsmorgaText
import cmm.apps.designsystem.EsmorgaTextStyle
import cmm.apps.designsystem.EsmorgaTimePickerDialog
import cmm.apps.designsystem.PossibleSelectableDates
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.Screen
import cmm.apps.esmorga.view.createevent.createeventdate.CreateEventDateScreenTestTags.CREATE_EVENT_DATE_BACK_BUTTON
import cmm.apps.esmorga.view.createevent.createeventdate.CreateEventDateScreenTestTags.CREATE_EVENT_DATE_NEXT_BUTTON
import cmm.apps.esmorga.view.createevent.createeventdate.CreateEventDateScreenTestTags.CREATE_EVENT_DATE_TITLE
import cmm.apps.esmorga.view.createevent.createeventdate.model.CreateEventFormDateEffect
import cmm.apps.esmorga.view.createevent.createeventdate.model.CreateEventFormDateUiState
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Screen
@Composable
fun CreateEventFormDateScreen(
    viewModel: CreateEventFormDateViewModel,
    onBackPressed: () -> Unit,
    onNextClick: () -> Unit
) {
    val uiState: CreateEventFormDateUiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { eff ->
            when (eff) {
                is CreateEventFormDateEffect.NavigateNext -> onNextClick()
                is CreateEventFormDateEffect.NavigateBack -> onBackPressed()
            }
        }
    }

    val startOfToday = remember {
        LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = uiState.selectedDateMillis ?: System.currentTimeMillis(),
        selectableDates = PossibleSelectableDates(startOfToday)
    )

    val deadlineDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = uiState.selectedDeadlineDateMillis
            ?: datePickerState.selectedDateMillis
            ?: System.currentTimeMillis(),
        selectableDates = DeadlineSelectableDates(
            startOfToday = startOfToday,
            eventDateMidnightMillis = datePickerState.selectedDateMillis ?: Long.MAX_VALUE
        )
    )

    LaunchedEffect(datePickerState.selectedDateMillis) {
        if (datePickerState.selectedDateMillis != null && uiState.selectedDateMillis != datePickerState.selectedDateMillis) {
            viewModel.onDateSelected(datePickerState.selectedDateMillis)
        }
    }

    LaunchedEffect(uiState.selectedDateMillis) {
        val currentMillis = datePickerState.selectedDateMillis
        val targetMillis = uiState.selectedDateMillis
        if (targetMillis != null && currentMillis != targetMillis) {
            datePickerState.selectedDateMillis = targetMillis
        }
    }

    LaunchedEffect(deadlineDatePickerState.selectedDateMillis) {
        if (uiState.selectedDeadlineDateMillis != deadlineDatePickerState.selectedDateMillis) {
            viewModel.onDeadlineDateChanged(datePickerState.selectedDateMillis, deadlineDatePickerState.selectedDateMillis)
        }
    }

    LaunchedEffect(uiState.selectedDeadlineDateMillis) {
        val currentMillis = deadlineDatePickerState.selectedDateMillis
        val targetMillis = uiState.selectedDeadlineDateMillis
        if (targetMillis != null && currentMillis != targetMillis) {
            deadlineDatePickerState.selectedDateMillis = targetMillis
        }
    }

    EsmorgaTheme {
        CreateEventFormDateView(
            uiState = uiState,
            onBackPressed = viewModel::onBackClick,
            datePickerState = datePickerState,
            deadlineDatePickerState = deadlineDatePickerState,
            onTimeSelected = viewModel::onTimeSelected,
            formattedTime = viewModel::formattedTime,
            onToggleChanged = viewModel::onDeadlineToggleChanged,
            onDeadlineTimeSelected = { deadlineDateMillis, time ->
                viewModel.onDeadlineTimeSelected(datePickerState.selectedDateMillis, deadlineDateMillis, time)
            },
            onDeadlineDateChanged = { deadlineDateMillis ->
                viewModel.onDeadlineDateChanged(datePickerState.selectedDateMillis, deadlineDateMillis)
            },
            onNextClick = viewModel::onNextClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventFormDateView(
    uiState: CreateEventFormDateUiState,
    onBackPressed: () -> Unit,
    datePickerState: DatePickerState,
    deadlineDatePickerState: DatePickerState,
    onTimeSelected: (String) -> Unit,
    formattedTime: (Int, Int) -> String,
    onToggleChanged: (Boolean) -> Unit,
    onDeadlineTimeSelected: (Long?, String) -> Unit,
    onDeadlineDateChanged: (Long?) -> Unit,
    onNextClick: (Date, String, Date?, String) -> Unit
) {
    var shownEventTimeDialog by rememberSaveable { mutableStateOf(false) }
    var shownDeadlineTimeDialog by rememberSaveable { mutableStateOf(false) }

    val eventTimeParts = remember(uiState.eventTime) { uiState.eventTime.parseTimeParts() }
    val deadlineTimeParts = remember(uiState.deadlineTime) { uiState.deadlineTime.parseTimeParts(defaultHour = 23, defaultMinute = 59) }

    val eventTimeState = key(eventTimeParts) {
        rememberTimePickerState(
            initialHour = eventTimeParts.first,
            initialMinute = eventTimeParts.second
        )
    }

    val deadlineTimeState = key(deadlineTimeParts) {
        rememberTimePickerState(
            initialHour = deadlineTimeParts.first,
            initialMinute = deadlineTimeParts.second
        )
    }

    LaunchedEffect(deadlineDatePickerState.selectedDateMillis) {
        onDeadlineDateChanged(deadlineDatePickerState.selectedDateMillis)
    }

    if (shownEventTimeDialog) {
        EsmorgaTimePickerDialog(
            modifier = Modifier,
            onDismiss = { shownEventTimeDialog = false },
            onConfirm = { time ->
                shownEventTimeDialog = false
                onTimeSelected(time)
            },
            formattedTime = formattedTime,
            confirmButtonText = stringResource(R.string.confirm_button_dialog),
            dismissButtonText = stringResource(R.string.cancel_button_dialog),
            timeState = eventTimeState,
            confirmButtonTestTag = CreateEventDateScreenTestTags.CREATE_EVENT_DATE_TIME_CONFIRM_BUTTON
        )
    }

    if (shownDeadlineTimeDialog) {
        EsmorgaTimePickerDialog(
            modifier = Modifier,
            onDismiss = { shownDeadlineTimeDialog = false },
            onConfirm = { time ->
                shownDeadlineTimeDialog = false
                onDeadlineTimeSelected(deadlineDatePickerState.selectedDateMillis, time)
            },
            formattedTime = formattedTime,
            confirmButtonText = stringResource(R.string.confirm_button_dialog),
            dismissButtonText = stringResource(R.string.cancel_button_dialog),
            timeState = deadlineTimeState,
            confirmButtonTestTag = CreateEventDateScreenTestTags.CREATE_EVENT_DATE_DEADLINE_TIME_CONFIRM_BUTTON
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = onBackPressed,
                        modifier = Modifier.testTag(CREATE_EVENT_DATE_BACK_BUTTON)
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.content_description_back_icon))
                    }
                },
            )
        }) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = 8.dp,
                    end = 16.dp,
                    start = 16.dp
                )
                .verticalScroll(rememberScrollState())
        ) {
            EsmorgaText(
                text = stringResource(R.string.screen_create_event_title),
                style = EsmorgaTextStyle.HEADING_1,
                modifier = Modifier
                    .padding(bottom = 12.dp, top = 8.dp)
                    .testTag(CREATE_EVENT_DATE_TITLE)
            )

            EsmorgaText(
                text = stringResource(R.string.step_3_screen_title),
                style = EsmorgaTextStyle.BODY_1,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            EsmorgaDatePicker(state = datePickerState)

            EsmorgaRow(
                title = stringResource(R.string.step_3_screen_row_time),
                onClick = { shownEventTimeDialog = true },
                caption = uiState.eventTime.take(5),
                modifier = Modifier.testTag(CreateEventDateScreenTestTags.CREATE_EVENT_DATE_TIME_ROW)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                EsmorgaText(
                    text = stringResource(R.string.field_title_join_deadline),
                    style = EsmorgaTextStyle.HEADING_2,
                    modifier = Modifier
                        .weight(1f)
                        .testTag(CreateEventDateScreenTestTags.CREATE_EVENT_DATE_DEADLINE_TOGGLE_LABEL)
                )
                Switch(
                    checked = uiState.isDeadlineToggleOn,
                    onCheckedChange = onToggleChanged,
                    modifier = Modifier.testTag(CreateEventDateScreenTestTags.CREATE_EVENT_DATE_DEADLINE_TOGGLE)
                )
            }

            if (uiState.isDeadlineToggleOn) {
                EsmorgaDatePicker(state = deadlineDatePickerState)

                uiState.deadlineErrorRes?.let { errorRes ->
                    Text(
                        text = stringResource(errorRes),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                EsmorgaRow(
                    title = stringResource(R.string.field_title_join_deadline_time),
                    onClick = { shownDeadlineTimeDialog = true },
                    caption = uiState.deadlineTime.take(5),
                    modifier = Modifier.testTag(CreateEventDateScreenTestTags.CREATE_EVENT_DATE_DEADLINE_TIME_ROW)
                )
            }

            EsmorgaButton(
                text = stringResource(R.string.step_continue_button),
                isEnabled = uiState.isButtonEnabled,
                modifier = Modifier
                    .padding(top = 32.dp, bottom = 16.dp)
                    .testTag(CREATE_EVENT_DATE_NEXT_BUTTON),
            ) {
                val date = Date(datePickerState.selectedDateMillis ?: uiState.selectedDateMillis ?: System.currentTimeMillis())
                val joinDeadlineDate = Date(deadlineDatePickerState.selectedDateMillis ?: uiState.selectedDeadlineDateMillis ?: System.currentTimeMillis())
                onNextClick(date, uiState.eventTime, joinDeadlineDate, uiState.deadlineTime)
            }
        }
    }
}

object CreateEventDateScreenTestTags {
    const val CREATE_EVENT_DATE_TITLE = "create_event_date_title"
    const val CREATE_EVENT_DATE_BACK_BUTTON = "create_event_date_back_button"
    const val CREATE_EVENT_DATE_NEXT_BUTTON = "create_event_date_next_button"
    const val CREATE_EVENT_DATE_TIME_ROW = "create_event_date_time_row"
    const val CREATE_EVENT_DATE_TIME_CONFIRM_BUTTON = "create_event_date_time_confirm_button"
    const val CREATE_EVENT_DATE_DEADLINE_TOGGLE = "create_event_date_deadline_toggle"
    const val CREATE_EVENT_DATE_DEADLINE_TOGGLE_LABEL = "create_event_date_deadline_toggle_label"
    const val CREATE_EVENT_DATE_DEADLINE_TIME_ROW = "create_event_date_deadline_time_row"
    const val CREATE_EVENT_DATE_DEADLINE_TIME_CONFIRM_BUTTON = "create_event_date_deadline_time_confirm_button"
}

private fun String.parseTimeParts(defaultHour: Int = 0, defaultMinute: Int = 0): Pair<Int, Int> {
    if (isBlank()) return defaultHour to defaultMinute
    val parts = split(":")
    val hour = parts.getOrNull(0)?.toIntOrNull() ?: defaultHour
    val minute = parts.getOrNull(1)?.toIntOrNull() ?: defaultMinute
    return hour to minute
}
