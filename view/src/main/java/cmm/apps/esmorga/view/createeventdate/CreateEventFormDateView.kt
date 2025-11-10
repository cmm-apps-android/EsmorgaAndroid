package cmm.apps.esmorga.view.createeventdate

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
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
import cmm.apps.designsystem.EsmorgaDatePicker
import cmm.apps.designsystem.EsmorgaRow
import cmm.apps.designsystem.EsmorgaText
import cmm.apps.designsystem.EsmorgaTextStyle
import cmm.apps.designsystem.EsmorgaTimePickerDialog
import cmm.apps.designsystem.PossibleSelectableDates
import cmm.apps.esmorga.domain.event.model.CreateEventForm
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.Screen
import cmm.apps.esmorga.view.createeventdate.CreateEventDateScreenTestTags.CREATE_EVENT_DATE_BACK_BUTTON
import cmm.apps.esmorga.view.createeventdate.CreateEventDateScreenTestTags.CREATE_EVENT_DATE_NEXT_BUTTON
import cmm.apps.esmorga.view.createeventdate.CreateEventDateScreenTestTags.CREATE_EVENT_DATE_TITLE
import cmm.apps.esmorga.view.createeventdate.model.CreateEventFormDateEffect
import cmm.apps.esmorga.view.createeventdate.model.CreateEventFormDateUiState
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Screen
@Composable
fun CreateEventFormDateScreen(
    eventForm: CreateEventForm,
    viewModel: CreateEventFormDateViewModel = koinViewModel(parameters = { parametersOf(eventForm) }),
    onBackPressed: () -> Unit,
    onNextClick: (CreateEventForm) -> Unit
) {
    val uiState: CreateEventFormDateUiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { eff ->
            when (eff) {
                is CreateEventFormDateEffect.NavigateNext -> onNextClick(eff.eventForm)
                is CreateEventFormDateEffect.NavigateBack -> onBackPressed()
            }
        }
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis(),
        selectableDates = PossibleSelectableDates(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli())
    )

    EsmorgaTheme {
        CreateEventFormDateView(
            onBackPressed = { viewModel.onBackClick() },
            datePickerState = datePickerState,
            isButtonEnabled = uiState.isButtonEnabled,
            onTimeSelected = { viewModel.onTimeSelected(it) },
            formattedTime = { hour, minute -> viewModel.formattedTime(hour, minute) },
            onNextClick = { date, time -> viewModel.onNextClick(date, time) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventFormDateView(
    onBackPressed: () -> Unit,
    datePickerState: DatePickerState,
    isButtonEnabled: Boolean,
    onTimeSelected: (String) -> Unit,
    formattedTime: (Int, Int) -> String,
    onNextClick: (Date, String) -> Unit
) {
    var shownDialog by remember { mutableStateOf(false) }
    var timeSelected by remember { mutableStateOf("") }

    val timeState = rememberTimePickerState(
        initialHour = LocalTime.now().hour,
        initialMinute = LocalTime.now().minute
    )

    if (shownDialog) {
        EsmorgaTimePickerDialog(
            modifier = Modifier,
            onDismiss = { shownDialog = false },
            onConfirm = { time ->
                shownDialog = false
                timeSelected = time
                onTimeSelected(time)
            },
            formattedTime = formattedTime,
            confirmButtonText = stringResource(R.string.confirm_button_dialog),
            dismissButtonText = stringResource(R.string.cancel_button_dialog),
            timeState = timeState
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onBackPressed()
                        },
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

            EsmorgaRow(title = stringResource(R.string.step_3_screen_row_time), onClick = { shownDialog = true }, caption = timeSelected.take(5))
            EsmorgaButton(
                text = stringResource(R.string.step_continue_button),
                isEnabled = isButtonEnabled,
                modifier = Modifier
                    .padding(top = 32.dp, bottom = 16.dp)
                    .testTag(CREATE_EVENT_DATE_NEXT_BUTTON),
            ) {
                val date = Date(datePickerState.selectedDateMillis ?: 0)
                onNextClick(date, timeSelected)
            }
        }

    }
}

object CreateEventDateScreenTestTags {
    const val CREATE_EVENT_DATE_TITLE = "create_event_date_title"
    const val CREATE_EVENT_DATE_BACK_BUTTON = "create_event_date_back_button"
    const val CREATE_EVENT_DATE_NEXT_BUTTON = "create_event_date_next_button"
}
