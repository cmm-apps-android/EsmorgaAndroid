package cmm.apps.esmorga.view

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cmm.apps.esmorga.view.home.BottomNavItem
import cmm.apps.esmorga.view.home.BottomNavItemRoute
import cmm.apps.esmorga.view.home.EsmorgaBottomBar
import cmm.apps.esmorga.view.navigation.EsmorgaNavigationGraph
import cmm.apps.esmorga.view.theme.EsmorgaTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen() //shows splash and handles setting the right theme

        val deeplinkPath: Uri? = intent?.data

        setupNavigation(deeplinkPath)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
    }

    private fun setupNavigation(deeplinkPath: Uri?) {
        setContent {
            EsmorgaTheme {
                val navigationController = rememberNavController()
                val bottomNavItems = listOf(
                    BottomNavItem.Explore,
                    BottomNavItem.MyEvents,
                    BottomNavItem.Profile
                )

                HomeView(bottomNavItems, navigationController) {
                    EsmorgaNavigationGraph(navigationController = navigationController, deeplinkPath = deeplinkPath)
                }
            }
        }
    }
}

@Composable
fun HomeView(bottomNavItems: List<BottomNavItem>, navigationController: NavHostController, content: @Composable () -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            val navBackStackEntry by navigationController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.hierarchy?.first()?.route?.substringAfterLast(".")
            val route = bottomNavItems.find { currentRoute == it.route.screen }?.route

            val visibility = route != null
            HomeBottomBar(bottomNavItems, visibility, navigationController, route)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(
                bottom = innerPadding.calculateBottomPadding(),
            )
        ) {
            content.invoke()
        }
    }
}

@Composable
fun HomeBottomBar(bottomNavItems: List<BottomNavItem>, visibility: Boolean, navigationController: NavHostController, currentRoute: BottomNavItemRoute?) {
    AnimatedVisibility(
        visible = visibility
    ) {
        Column {
            HorizontalDivider(Modifier.fillMaxWidth(), thickness = 1.dp, color = colorScheme.surfaceVariant)
            EsmorgaBottomBar(navigationController, bottomNavItems, currentRoute)
        }
    }
}
