package cmm.apps.esmorga.view.screenshot.profile

import cmm.apps.designsystem.EsmorgaDialog
import cmm.apps.esmorga.view.screenshot.BaseScreenshotTest
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import org.junit.Test

class ProfileLogOutDialogViewScreenshotTest : BaseScreenshotTest() {

    @Test
    fun logoutDialog_default() {
        paparazzi.snapshot {
            EsmorgaTheme {
                EsmorgaDialog(
                    title = "Are you sure you want to log out?",
                    confirmButtonText = "Yes, log out",
                    dismissButtonText = "No, cancel",
                    onConfirm = {},
                    onDismiss = {}
                )
            }
        }
    }
}
