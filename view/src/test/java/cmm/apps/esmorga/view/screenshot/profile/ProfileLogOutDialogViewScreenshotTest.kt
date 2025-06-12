package cmm.apps.esmorga.view.screenshot.profile

import cmm.apps.esmorga.view.profile.LogoutDialog
import cmm.apps.esmorga.view.screenshot.BaseScreenshotTest
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import org.junit.Test

class ProfileLogOutDialogViewScreenshotTest : BaseScreenshotTest() {

    @Test
    fun logoutDialog_default() {
        paparazzi.snapshot {
            EsmorgaTheme {
                LogoutDialog(
                    onConfirm = {},
                    onDismiss = {}
                )
            }
        }
    }
}
