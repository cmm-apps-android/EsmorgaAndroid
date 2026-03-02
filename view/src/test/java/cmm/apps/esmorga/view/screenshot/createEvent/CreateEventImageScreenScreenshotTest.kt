package cmm.apps.esmorga.view.screenshot.createEvent

import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.create_event.createeventimage.CreateEventFormImageView
import cmm.apps.esmorga.view.create_event.createeventimage.model.CreateEventFormImageUiState
import cmm.apps.esmorga.view.screenshot.BaseScreenshotTest
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import org.junit.Test

class CreateEventImageScreenScreenshotTest : BaseScreenshotTest() {

    @Test
    fun createEventImageScreenDefault() {
        snapshotWithState(
            CreateEventFormImageUiState(
                imageUrl = "",
                showPreview = false,
                isLoading = false,
                imageError = null,
            )
        )
    }

    @Test
    fun createEventImageScreenWithPreview() {
        snapshotWithState(
            CreateEventFormImageUiState(
                imageUrl = "https://example.com/image.jpg",
                showPreview = true,
                isLoading = false,
                imageError = null,
            )
        )
    }

    @Test
    fun createEventImageScreenWithError() {
        snapshotWithState(
            CreateEventFormImageUiState(
                imageUrl = "",
                showPreview = false,
                isLoading = false,
                imageError = R.string.inline_error_image_url_required,
            )
        )
    }

    private fun snapshotWithState(uiState: CreateEventFormImageUiState) {
        paparazzi.snapshot {
            EsmorgaTheme(darkTheme = false) {
                CreateEventFormImageView(
                    uiState = uiState,
                    onBackPressed = {},
                    onImageUrlChange = {},
                    onPreviewClick = {},
                    onDeleteClick = {},
                    onCreateEventClick = {}
                )
            }
        }
    }
}
