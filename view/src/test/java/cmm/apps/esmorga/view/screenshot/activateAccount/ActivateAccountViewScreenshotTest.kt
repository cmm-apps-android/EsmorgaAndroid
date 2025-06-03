package cmm.apps.esmorga.view.screenshot.activateAccount

import cmm.apps.esmorga.view.activateaccount.ActivateAccountView
import cmm.apps.esmorga.view.activateaccount.model.ActivateAccountUiState
import cmm.apps.esmorga.view.screenshot.BaseScreenshotTest
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import org.junit.Test

class ActivateAccountViewScreenshotTest : BaseScreenshotTest() {

    @Test
    fun activateAccountView_lightTheme_idleState() {
        snapshotWithState(
            ActivateAccountUiState(
                isLoading = false,
                error = null
            )
        )
    }

    @Test
    fun activateAccountView_lightTheme_loadingState() {
        snapshotWithState(
            ActivateAccountUiState(
                isLoading = true,
                error = null
            )
        )
    }

    private fun snapshotWithState(uiState: ActivateAccountUiState) {
        paparazzi.snapshot {
            EsmorgaTheme(darkTheme = false) {
                ActivateAccountView(
                    uiState = uiState,
                    onContinueClick = {}
                )
            }
        }
    }
}
