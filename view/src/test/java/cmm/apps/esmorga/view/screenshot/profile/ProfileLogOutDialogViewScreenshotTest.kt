package cmm.apps.esmorga.view.screenshot.profile

import androidx.compose.material3.MaterialTheme
import cmm.apps.esmorga.view.profile.LogoutDialog
import cmm.apps.esmorga.view.screenshot.BaseScreenshotTest
import org.junit.Test

class ProfileLogOutDialogViewScreenshotTest : BaseScreenshotTest() {

    @Test
    fun logoutDialog_default() {
        paparazzi.snapshot {
            MaterialTheme {
                LogoutDialog(
                    onConfirm = {},
                    onDismiss = {}
                )
            }
        }
    }
}
