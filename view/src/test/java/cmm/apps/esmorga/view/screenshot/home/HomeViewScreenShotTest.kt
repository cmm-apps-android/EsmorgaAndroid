package cmm.apps.esmorga.view.screenshot.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import cmm.apps.esmorga.view.HomeBottomBar
import cmm.apps.esmorga.view.home.BottomNavItemRoute
import cmm.apps.esmorga.view.screenshot.BaseScreenshotTest
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import cmm.apps.esmorga.view.viewmodel.mock.BottomBarMock
import org.junit.Test

class HomeViewScreenShotTest : BaseScreenshotTest() {

    @Test
    fun bottombar_eventLst_selected() {
        snapshotWithState(BottomNavItemRoute.EVENT_LIST)
    }

    @Test
    fun bottombar_myEvents_selected() {
        snapshotWithState(BottomNavItemRoute.MY_EVENTS)
    }

    @Test
    fun bottombar_profile_selected() {
        snapshotWithState(BottomNavItemRoute.PROFILE)
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    private fun snapshotWithState(currentRoute: BottomNavItemRoute) {
        paparazzi.snapshot {
            val navigationController = rememberNavController()
            EsmorgaTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        HomeBottomBar(BottomBarMock.provideBottomNavItems(), true, navigationController, currentRoute)
                    }
                ) {

                }
            }
        }
    }
}
