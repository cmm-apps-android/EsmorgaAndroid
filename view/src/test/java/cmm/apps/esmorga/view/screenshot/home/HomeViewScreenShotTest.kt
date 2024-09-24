package cmm.apps.esmorga.view.screenshot.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import cmm.apps.esmorga.view.home.EsmorgaBottomBar
import cmm.apps.esmorga.view.screenshot.BaseScreenshotTest
import cmm.apps.esmorga.view.theme.Pearl
import cmm.apps.esmorga.view.viewmodel.mock.BottomBarMock
import org.junit.Test

class HomeViewScreenShotTest : BaseScreenshotTest() {

    @Test
    fun bottombar_eventLst_selected() {
        snapshotWithState("EventListScreen")
    }

    @Test
    fun bottombar_myEvents_selected() {
        snapshotWithState("MyEventsScreen")
    }

    @Test
    fun bottombar_profile_selected() {
        snapshotWithState("ProfileScreen")
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    private fun snapshotWithState(currentRoute: String) {
        paparazzi.snapshot {
            val navigationController = rememberNavController()
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    Column {
                        HorizontalDivider(Modifier.fillMaxWidth(), thickness = 1.dp, color = Pearl)
                        EsmorgaBottomBar(navigationController, BottomBarMock.provideBottomNavItems(), currentRoute)
                    }
                }
            ) {

            }
        }
    }
}
