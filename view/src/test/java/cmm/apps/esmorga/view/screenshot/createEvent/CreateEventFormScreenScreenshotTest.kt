package cmm.apps.esmorga.view.screenshot.createevent

import cmm.apps.esmorga.view.createevent.CreateEventStep1ScreenContent
import cmm.apps.esmorga.view.screenshot.BaseScreenshotTest
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import org.junit.Test
import cmm.apps.esmorga.view.R

class CreateEventStep1ScreenContentScreenshotTest : BaseScreenshotTest() {

    @Test
    fun createEventStep1Screen_default() {
        snapshotWithState(
            eventName = "",
            eventNameError = null,
            description = "",
            descriptionError = null,
            isFormValid = false
        )
    }

    @Test
    fun createEventStep1Screen_withNameError() {
        snapshotWithState(
            eventName = "1",
            eventNameError = R.string.inline_error_invalid_length_name,
            description = "Some description",
            descriptionError = null,
            isFormValid = false
        )
    }

    @Test
    fun createEventStep1Screen_withDescriptionError() {
        snapshotWithState(
            eventName = "My Event",
            eventNameError = null,
            description = "1",
            descriptionError = R.string.inline_error_invalid_length_description,
            isFormValid = false
        )
    }

    @Test
    fun createEventStep1Screen_formValid() {
        snapshotWithState(
            eventName = "My Event",
            eventNameError = null,
            description = "Good description",
            descriptionError = null,
            isFormValid = true
        )
    }

    private fun snapshotWithState(
        eventName: String,
        eventNameError: Int?,
        description: String,
        descriptionError: Int?,
        isFormValid: Boolean
    ) {
        paparazzi.snapshot {
            EsmorgaTheme(darkTheme = false) {
                CreateEventStep1ScreenContent(
                    eventName = eventName,
                    onEventNameChange = {},
                    eventNameError = eventNameError,
                    description = description,
                    onDescriptionChange = {},
                    descriptionError = descriptionError,
                    isFormValid = isFormValid,
                    onBackClick = {},
                    onNextClick = {}
                )
            }
        }
    }
}
