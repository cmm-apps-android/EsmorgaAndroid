package cmm.apps.esmorga.view.navigation

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import cmm.apps.esmorga.view.errors.EsmorgaErrorScreen
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArguments
import cmm.apps.esmorga.view.eventdetails.EventDetailsScreen
import cmm.apps.esmorga.view.eventlist.EventListScreen
import cmm.apps.esmorga.view.login.LoginScreen
import cmm.apps.esmorga.view.registration.RegistrationScreen
import cmm.apps.esmorga.view.welcome.WelcomeScreen
import kotlinx.serialization.Serializable
import org.jetbrains.annotations.VisibleForTesting
import kotlin.reflect.typeOf

sealed class Navigation {

    @Serializable
    data object WelcomeScreen : Navigation()

    @Serializable
    data object EventListScreen : Navigation()

    @Serializable
    data class EventDetailScreen(val eventId: String) : Navigation()

    @Serializable
    data object LoginScreen : Navigation()

    @Serializable
    data object RegistrationScreen : Navigation()

    @Serializable
    data class FullScreenError(val esmorgaErrorScreenArguments: EsmorgaErrorScreenArguments) : Navigation()
}

const val GOOGLE_MAPS_PACKAGE = "com.google.android.apps.maps"

@Composable
fun EsmorgaNavigationGraph(navigationController: NavHostController, loggedIn: Boolean) {
    val startDestination = if (loggedIn) Navigation.EventListScreen else Navigation.WelcomeScreen
    EsmorgaNavHost(navigationController = navigationController, startDestination = startDestination)
}

/**
 * Function created setting up the navigation graph from a given starting point. **DO NOT CALL FROM APP CODE**, it only exists for testing purposes.
 * */
@VisibleForTesting
@Composable
internal fun EsmorgaNavHost(navigationController: NavHostController, startDestination: Navigation) {
    NavHost(navController = navigationController, startDestination = startDestination) {
        loginFlow(navigationController)
        eventFlow(navigationController)
        errorFlow(navigationController)
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
            onRegisterClicked = {
                navigationController.navigate(Navigation.RegistrationScreen)
            },
            onLoginSuccess = {
                navigationController.navigate(Navigation.EventListScreen) {
                    popUpTo(Navigation.WelcomeScreen) {
                        inclusive = true
                    }
                }
            },
            onLoginError = { esmorgaFullScreenArguments ->
                navigationController.navigate(Navigation.FullScreenError(esmorgaErrorScreenArguments = esmorgaFullScreenArguments))
            },
            onBackClicked = {
                navigationController.popBackStack()
            })
    }
    composable<Navigation.RegistrationScreen> {
        RegistrationScreen(
            onRegistrationSuccess = {
                navigationController.navigate(Navigation.EventListScreen) {
                    popUpTo(Navigation.WelcomeScreen) {
                        inclusive = true
                    }
                }
            },
            onRegistrationError = { esmorgaFullScreenArguments ->
                navigationController.navigate(Navigation.FullScreenError(esmorgaErrorScreenArguments = esmorgaFullScreenArguments))
            },
            onBackClicked = {
                navigationController.popBackStack()
            }
        )
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

private fun NavGraphBuilder.errorFlow(navigationController: NavHostController) {
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

fun openNavigationApp(context: Context, latitude: Double, longitude: Double) {
    val uri = Uri.parse("geo:$latitude,$longitude")
    if (isPackageAvailable(context, GOOGLE_MAPS_PACKAGE)) {
        val mapIntent = Intent(Intent.ACTION_VIEW, uri)
        mapIntent.setPackage(GOOGLE_MAPS_PACKAGE)
        context.startActivity(mapIntent)
    } else {
        // do nothing
    }
}

private fun isPackageAvailable(context: Context, appPackage: String) = try {
    val appInfo = context.packageManager?.getApplicationInfo(appPackage, 0)
    appInfo != null && appInfo.enabled
} catch (e: PackageManager.NameNotFoundException) {
    false
}