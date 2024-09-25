package cmm.apps.esmorga.view.screenshot.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import cmm.apps.esmorga.view.HomeBottomBar
import cmm.apps.esmorga.view.navigation.Navigation
import cmm.apps.esmorga.view.screenshot.BaseScreenshotTest
import cmm.apps.esmorga.view.viewmodel.mock.BottomBarMock
import org.junit.Test

class HomeViewScreenShotTest : BaseScreenshotTest() {

    @Test
    fun bottombar_eventLst_selected() {
        snapshotWithState(Navigation.EventListScreen.toString())
    }

    @Test
    fun bottombar_myEvents_selected() {
        snapshotWithState(Navigation.MyEventsScreen.toString())
    }

    @Test
    fun bottombar_profile_selected() {
        snapshotWithState(Navigation.ProfileScreen.toString())
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    private fun snapshotWithState(currentRoute: String) {
        paparazzi.snapshot {
            val navigationController = rememberNavController()
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
