package cmm.apps.esmorga.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import cmm.apps.esmorga.view.errors.EsmorgaErrorScreen
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArguments
import cmm.apps.esmorga.view.eventdetails.EventDetailsScreen
import cmm.apps.esmorga.view.eventlist.EventListScreen
import cmm.apps.esmorga.view.login.LoginScreen
import cmm.apps.esmorga.view.navigation.Navigation
import cmm.apps.esmorga.view.navigation.serializableType
import cmm.apps.esmorga.view.welcome.WelcomeScreen
import kotlin.reflect.typeOf

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navigationController = rememberNavController()
            NavHost(navController = navigationController, startDestination = Navigation.WelcomeScreen) {
                eventFlow(navigationController)
                loginFlow(navigationController)
                composable<Navigation.FullScreenError>(
                    typeMap = mapOf(typeOf<EsmorgaErrorScreenArguments>() to serializableType<EsmorgaErrorScreenArguments>())
                ) { backStackEntry ->
                    val esmorgaErrorScreenArguments = backStackEntry.toRoute<Navigation.FullScreenError>().esmorgaErrorScreenArguments
                    EsmorgaErrorScreen(
                        esmorgaErrorScreenArguments = esmorgaErrorScreenArguments,
                        onButtonPressed = {
                            navigationController.popBackStack()
                        })
                }
            }
        }
    }

    private fun NavGraphBuilder.loginFlow(navigationController: NavHostController) {
        composable<Navigation.WelcomeScreen> {
            WelcomeScreen(
                onEnterAsGuestClicked = {
                    navigationController.navigate(Navigation.EventListScreen) {
                        popUpTo(Navigation.WelcomeScreen) {
                            inclusive = true
                        }
                    }
                },
                onLoginRegisterClicked = {
                    navigationController.navigate(Navigation.LoginScreen)
                })
        }
        composable<Navigation.LoginScreen> {
            LoginScreen(
                onLoginSuccess = {
                    navigationController.navigate(Navigation.EventListScreen)
                },
                onLoginError = { esmorgaFullScreenArguments ->
                    navigationController.navigate(Navigation.FullScreenError(esmorgaErrorScreenArguments = esmorgaFullScreenArguments))
                })
        }
    }

    private fun NavGraphBuilder.eventFlow(navigationController: NavHostController) {
        composable<Navigation.EventListScreen> {
            EventListScreen(
                onEventClick = { eventId ->
                    navigationController.navigate(Navigation.EventDetailScreen(eventId))
                })
        }
        composable<Navigation.EventDetailScreen> { backStackEntry ->
            EventDetailsScreen(
                eventId = backStackEntry.toRoute<Navigation.EventDetailScreen>().eventId,
                onBackPressed = { navigationController.popBackStack() }
            )
        }
    }
}
