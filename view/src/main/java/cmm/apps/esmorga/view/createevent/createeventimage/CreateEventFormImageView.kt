package cmm.apps.esmorga.view.createevent.createeventimage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmm.apps.designsystem.EsmorgaButton
import cmm.apps.designsystem.EsmorgaText
import cmm.apps.designsystem.EsmorgaTextField
import cmm.apps.designsystem.EsmorgaTextStyle
import cmm.apps.esmorga.domain.event.model.CreateEventForm
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.Screen
import cmm.apps.esmorga.view.createevent.createeventimage.model.CreateEventFormImageEffect
import cmm.apps.esmorga.view.createevent.createeventimage.model.CreateEventFormImageUiState
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Screen
@Composable
fun CreateEventFormImageScreen(
    eventForm: CreateEventForm,
    viewModel: CreateEventFormImageViewModel = koinViewModel(parameters = { parametersOf(eventForm) }),
    onBackPressed: () -> Unit,
    onNextClick: (CreateEventForm) -> Unit
) {
    val uiState: CreateEventFormImageUiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { eff ->
            when (eff) {
                is CreateEventFormImageEffect.NavigateNext -> onNextClick(eff.eventForm)
                is CreateEventFormImageEffect.NavigateBack -> onBackPressed()
            }
        }
    }

    EsmorgaTheme {
        CreateEventFormImageView(
            uiState = uiState,
            onBackPressed = { viewModel.onBackClick() },
            onImageUrlChange = viewModel::onImageUrlChanged,
            onPreviewClick = viewModel::onPreviewClick,
            onDeleteClick = viewModel::onDeleteImageClick,
            onCreateEventClick = viewModel::onCreateEventClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventFormImageView(
    uiState: CreateEventFormImageUiState,
    onBackPressed: () -> Unit,
    onImageUrlChange: (String) -> Unit,
    onPreviewClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onCreateEventClick: () -> Unit
) {
    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = onBackPressed,
                        modifier = Modifier.testTag(CreateEventImageScreenTestTags.CREATE_EVENT_IMAGE_BACK_BUTTON)
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
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(padding)
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                EsmorgaText(
                    text = stringResource(R.string.screen_create_event_title),
                    style = EsmorgaTextStyle.HEADING_1,
                    modifier = Modifier.testTag(CreateEventImageScreenTestTags.CREATE_EVENT_IMAGE_TITLE)
                )

                EsmorgaTextField(
                    value = uiState.imageUrl,
                    onValueChange = onImageUrlChange,
                    title = R.string.field_title_event_image,
                    placeholder = R.string.placeholder_event_image,
                    errorText = uiState.imageError?.let { stringResource(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(CreateEventImageScreenTestTags.CREATE_EVENT_IMAGE_URL_FIELD)
                )

                EsmorgaButton(
                    text = stringResource(
                        if (uiState.showPreview) R.string.button_delete else R.string.button_preview
                    ),
                    primary = false,
                    onClick = {
                        if (uiState.showPreview) onDeleteClick() else onPreviewClick()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(CreateEventImageScreenTestTags.CREATE_EVENT_IMAGE_PREVIEW_BUTTON)
                )

                if (uiState.showPreview && uiState.imageUrl.isNotBlank()) {
                    AsyncImage(
                        model = uiState.imageUrl,
                        contentDescription = stringResource(R.string.add_image_event_content_description),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .padding(horizontal = 0.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .testTag(CreateEventImageScreenTestTags.CREATE_EVENT_IMAGE_PREVIEW),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            EsmorgaButton(
                text = stringResource(R.string.button_create_event),
                onClick = onCreateEventClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .testTag(CreateEventImageScreenTestTags.CREATE_EVENT_IMAGE_CREATE_BUTTON)
            )
        }
    }
}

object CreateEventImageScreenTestTags {
    const val CREATE_EVENT_IMAGE_BACK_BUTTON = "create_event_image_back_button"
    const val CREATE_EVENT_IMAGE_TITLE = "create_event_image_title"
    const val CREATE_EVENT_IMAGE_URL_FIELD = "create_event_image_url_field"
    const val CREATE_EVENT_IMAGE_PREVIEW_BUTTON = "create_event_image_preview_button"
    const val CREATE_EVENT_IMAGE_PREVIEW = "create_event_image_preview"
    const val CREATE_EVENT_IMAGE_CREATE_BUTTON = "create_event_image_create_button"
}