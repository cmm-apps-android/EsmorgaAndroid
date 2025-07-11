package cmm.apps.esmorga.view.screenshot.profile

import cmm.apps.esmorga.domain.user.model.User
import cmm.apps.esmorga.view.profile.ProfileView
import cmm.apps.esmorga.view.profile.model.ProfileUiState
import cmm.apps.esmorga.view.screenshot.BaseScreenshotTest
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import org.junit.Test

class ProfileViewScreenshotTest : BaseScreenshotTest() {

    @Test
    fun profileView_lightTheme_logged() {
        snapshotWithState(
            user = User(
                name = "User",
                lastName = "User",
                email = "user@email.com",
                role = "USER"
            )
        )
    }

    @Test
    fun profileView_lightTheme_not_logged() {
        snapshotWithState(user = null)
    }

    private fun snapshotWithState(user: User?) {
        paparazzi.snapshot {
            EsmorgaTheme(darkTheme = false) {
                ProfileView(
                    uiState = ProfileUiState(
                        user = user
                    ),
                    shownLogOutDialog = {},
                    onNavigateLogin = {},
                    onChangePassword = {}
                )
            }
        }
    }

}