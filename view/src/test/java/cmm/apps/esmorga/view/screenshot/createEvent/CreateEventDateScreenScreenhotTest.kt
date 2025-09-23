package cmm.apps.esmorga.view.screenshot.createEvent

import cmm.apps.esmorga.view.createeventdate.CreateEventFormDateView
import cmm.apps.esmorga.view.screenshot.BaseScreenshotTest
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import org.junit.Test

class CreateEventFormDateScreenScreenshotTest : BaseScreenshotTest() {

    @Test
    fun createEventDateScreenDefault() {
        snapshotWithState(
            isButtonEnabled = false
        )
    }

    @Test
    fun createEventDateScreenWithButtonEnabled() {
        snapshotWithState(
            isButtonEnabled = true
        )
    }

    private fun snapshotWithState(
        isButtonEnabled: Boolean = false,
    ) {
        paparazzi.snapshot {
            EsmorgaTheme(darkTheme = false) {
                CreateEventFormDateView(
                    isButtonEnabled = isButtonEnabled,
                    onBackPressed = {},
                    onNextClick = { _, _ -> },
                    onTimeSelected = { _ -> },
                    formattedTime = { _, _ -> "" }
                )
            }
        }
    }
}