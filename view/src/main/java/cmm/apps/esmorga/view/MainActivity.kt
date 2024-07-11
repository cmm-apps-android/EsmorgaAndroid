package cmm.apps.esmorga.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cmm.apps.esmorga.view.eventdetails.EventDetailsScreen
import cmm.apps.esmorga.view.eventlist.EventListScreen
import cmm.apps.esmorga.view.navigation.Navigation
import cmm.apps.esmorga.view.welcome.WelcomeScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navigationController = rememberNavController()
            NavHost(navController = navigationController, startDestination = Navigation.WelcomeScreen.route) {
                composable(Navigation.WelcomeScreen.route) {
                    WelcomeScreen(onEnterAsGuestClicked = {
                        navigationController.navigate(Navigation.EventListScreen.route){
                            popUpTo(Navigation.WelcomeScreen.route) {
                                inclusive = true
                            }
                        }
                    }, onLoginRegisterClicked = {})
                }
                composable(Navigation.EventListScreen.route) {
                    EventListScreen(onEventClick = { eventId ->
                        navigationController.navigate(Navigation.EventDetailScreen.createRoute(eventId))
                    })
                }
                composable(Navigation.EventDetailScreen.route, arguments = listOf(navArgument("eventId") { type = NavType.StringType })) { backStackEntry ->
                    EventDetailsScreen(
                        eventId = backStackEntry.arguments?.getString("eventId")!!,
                        onBackPressed = { navigationController.popBackStack() }
                    )
                }
            }
        }
    }
}
