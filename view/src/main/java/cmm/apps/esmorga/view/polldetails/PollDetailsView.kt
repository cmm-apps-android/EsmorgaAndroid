package cmm.apps.esmorga.view.polldetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmm.apps.designsystem.EsmorgaButton
import cmm.apps.designsystem.EsmorgaCheckboxRow
import cmm.apps.designsystem.EsmorgaRadioButton
import cmm.apps.esmorga.domain.poll.model.Poll
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.Screen
import cmm.apps.esmorga.view.details.DetailsDescriptionSection
import cmm.apps.esmorga.view.details.DetailsHeaderSection
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArguments
import cmm.apps.esmorga.view.extensions.observeLifecycleEvents
import cmm.apps.esmorga.view.polldetails.EventDetailsScreenTestTags.POLL_DETAILS_BACK_BUTTON
import cmm.apps.esmorga.view.polldetails.EventDetailsScreenTestTags.POLL_DETAILS_EVENT_NAME
import cmm.apps.esmorga.view.polldetails.model.PollDetailsEffect
import cmm.apps.esmorga.view.polldetails.model.PollDetailsUiState
import cmm.apps.esmorga.view.polldetails.model.PollOptionUiModel
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Screen
@Composable
fun PollDetailsScreen(
    poll: Poll,
    vm: PollDetailsViewModel = koinViewModel(parameters = { parametersOf(poll) }),
    onBackPressed: () -> Unit,
    onVoteError: (EsmorgaErrorScreenArguments) -> Unit,
    onNoNetworkError: (EsmorgaErrorScreenArguments) -> Unit
) {
    val uiState: PollDetailsUiState by vm.uiState.collectAsStateWithLifecycle()

    val voteSuccessMessage = stringResource(R.string.snackbar_vote_successful)
    val snackbarHostState = remember { SnackbarHostState() }
    val localCoroutineScope = rememberCoroutineScope()
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    vm.observeLifecycleEvents(lifecycle)
    LaunchedEffect(Unit) {
        vm.effect.collect { eff ->
            when (eff) {
                is PollDetailsEffect.NavigateBack -> {
                    onBackPressed()
                }

                is PollDetailsEffect.ShowVoteSuccess -> {
                    localCoroutineScope.launch {
                        snackbarHostState.showSnackbar(message = voteSuccessMessage)
                    }
                }

                is PollDetailsEffect.ShowFullScreenError -> {
                    onVoteError(eff.esmorgaErrorScreenArguments)
                }

                is PollDetailsEffect.ShowNoNetworkError -> {
                    onNoNetworkError(eff.esmorgaNoNetworkArguments)
                }
            }
        }
    }
    EsmorgaTheme {
        PollDetailsView(
            uiState = uiState,
            snackbarHostState = snackbarHostState,
            onOptionSelected = { optionId, isSelected -> vm.onOptionSelected(optionId, isSelected) },
            onPrimaryButtonClicked = { vm.onPrimaryButtonClicked() },
            onBackPressed = { vm.onBackPressed() })
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PollDetailsView(
    uiState: PollDetailsUiState,
    snackbarHostState: SnackbarHostState,
    onOptionSelected: (String, Boolean) -> Unit,
    onPrimaryButtonClicked: () -> Unit,
    onBackPressed: () -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(
            title = {},
            navigationIcon = {
                IconButton(
                    onClick = { onBackPressed() }, modifier = Modifier.testTag(POLL_DETAILS_BACK_BUTTON)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.content_description_back_icon)
                    )
                }
            },
        )
    }, snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(
                    top = innerPadding.calculateTopPadding()
                )
                .verticalScroll(state = rememberScrollState())
        ) {
            DetailsHeaderSection(image = uiState.image, title = uiState.title, date = uiState.voteDeadline, titleTestTag = POLL_DETAILS_EVENT_NAME)

            DetailsDescriptionSection(description = uiState.description)

            PollDetailsOptionsSection(
                isMultipleChoice = uiState.isMultipleChoice,
                options = uiState.options,
                onOptionSelected = onOptionSelected
            )

            PollDetailsButtonSection(
                isPrimaryButtonEnabled = uiState.isPrimaryButtonEnabled,
                isPrimaryButtonLoading = uiState.isPrimaryButtonLoading,
                primaryButtonTitle = uiState.primaryButtonTitle,
                onPrimaryButtonClicked = onPrimaryButtonClicked
            )
        }
    }
}

@Composable
fun PollDetailsOptionsSection(
    isMultipleChoice: Boolean,
    options: List<PollOptionUiModel>,
    onOptionSelected: (String, Boolean) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        if (isMultipleChoice) {
            HorizontalDivider(color = MaterialTheme.colorScheme.secondary, thickness = 1.dp)
            options.forEach { option ->
                EsmorgaCheckboxRow(
                    text = option.text,
                    shouldShowChecked = true,
                    checked = option.isSelected,
                    onCheckedChanged = { checked ->
                        onOptionSelected(option.id, checked)
                    }
                )
            }
        } else {
            options.forEach { option ->
                EsmorgaRadioButton(
                    text = option.text,
                    selected = option.isSelected,
                    onClick = { onOptionSelected(option.id, true) },
                    modifier = Modifier.padding(start = 32.dp, end = 16.dp)
                )
            }
        }
        Spacer(modifier = Modifier.padding(bottom = 32.dp))
    }
}


@Composable
fun PollDetailsButtonSection(
    isPrimaryButtonEnabled: Boolean,
    isPrimaryButtonLoading: Boolean,
    primaryButtonTitle: String,
    onPrimaryButtonClicked: () -> Unit
) {
    EsmorgaButton(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 32.dp)
            .testTag(EventDetailsScreenTestTags.POLL_DETAILS_PRIMARY_BUTTON),
        text = primaryButtonTitle,
        primary = true,
        isLoading = isPrimaryButtonLoading,
        isEnabled = isPrimaryButtonEnabled
    ) {
        onPrimaryButtonClicked()
    }
}

object EventDetailsScreenTestTags {
    const val POLL_DETAILS_EVENT_NAME = "poll details poll name"
    const val POLL_DETAILS_BACK_BUTTON = "poll details back button"
    const val POLL_DETAILS_PRIMARY_BUTTON = "poll details primary button"
}