package cmm.apps.esmorga.view.screenshot.error

import cmm.apps.designsystem.EsmorgaFullScreenError
import cmm.apps.esmorga.view.screenshot.BaseScreenshotTest
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import cmm.apps.esmorga.view.R
import org.junit.Test

class FullErrorScreenshotTest : BaseScreenshotTest() {

    @Test
    fun fullScreenErrorView_lightTheme_data() {
        snapshotWithState()
    }

    @Test
    fun fullScreenNoInternetErrorView_lightTheme_data() {
        snapshotWithState(R.raw.no_connection_anim, "There is no internet connection")
    }

    private fun snapshotWithState(animation: Int? = null, subtitle: String? = null) {
        paparazzi.snapshot {
            EsmorgaTheme(darkTheme = false) {
                EsmorgaFullScreenError(
                    animation = animation,
                    title = "Something has failed, please try again later.",
                    subtitle = subtitle,
                    buttonText = "Retry",
                    buttonAction = {}
                )
            }
        }
    }

}