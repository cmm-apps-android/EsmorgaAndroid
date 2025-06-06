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
import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.view.activateaccount.ActivateAccountScreen
import cmm.apps.esmorga.view.deeplink.DeeplinkManager.navigateFromDeeplink
import cmm.apps.esmorga.view.errors.EsmorgaErrorScreen
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArguments
import cmm.apps.esmorga.view.eventdetails.EventDetailsScreen
import cmm.apps.esmorga.view.eventlist.EventListScreen
import cmm.apps.esmorga.view.eventlist.MyEventListScreen
import cmm.apps.esmorga.view.login.LoginScreen
import cmm.apps.esmorga.view.password.RecoverPasswordScreen
import cmm.apps.esmorga.view.profile.ProfileScreen
import cmm.apps.esmorga.view.registration.RegistrationConfirmationScreen
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
    data class EventDetailScreen(val event: Event) : Navigation()

    @Serializable
    data object LoginScreen : Navigation()

    @Serializable
    data object RegistrationScreen : Navigation()

    @Serializable
    data class RegistrationConfirmationScreen(val email: String) : Navigation()

    @Serializable
    data class FullScreenError(val esmorgaErrorScreenArguments: EsmorgaErrorScreenArguments, val redirectToWelcome: Boolean = false) : Navigation()

    @Serializable
    data object MyEventsScreen : Navigation()

    @Serializable
    data object ProfileScreen : Navigation()

    @Serializable
    data class ActivateAccountScreen(val verificationCode: String) : Navigation()

    @Serializable
    data object RecoverPasswordScreen : Navigation()
}

const val GOOGLE_MAPS_PACKAGE = "com.google.android.apps.maps"

@Composable
fun EsmorgaNavigationGraph(navigationController: NavHostController, loggedIn: Boolean, deeplinkPath: Uri?) {
    val startDestination = if (deeplinkPath != null) {
        navigateFromDeeplink(deeplinkPath)
    } else {
        if (loggedIn) Navigation.EventListScreen else Navigation.WelcomeScreen
    }
    EsmorgaNavHost(navigationController, startDestination)
}

/**
 * Function created setting up the navigation graph from a given starting point. **DO NOT CALL FROM APP CODE**, it only exists for testing purposes.
 * */
@VisibleForTesting
@Composable
internal fun EsmorgaNavHost(navigationController: NavHostController, startDestination: Navigation) {
    NavHost(navigationController, startDestination = startDestination) {
        loginFlow(navigationController)
        homeFlow(navigationController)
        errorFlow(navigationController)
        accountActivationFlow(navigationController)
    }
}

private fun NavGraphBuilder.accountActivationFlow(navigationController: NavHostController) {
    composable<Navigation.ActivateAccountScreen> { backStackEntry ->
        ActivateAccountScreen(
            backStackEntry.toRoute<Navigation.ActivateAccountScreen>().verificationCode, onContinueClick = {
                navigationController.navigate(Navigation.EventListScreen) {
                    popUpTo(0) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            onError = {
                navigationController.navigate(Navigation.FullScreenError(it))
            },
            onLastTryError = { arguments, redirectToWelcome ->
                navigationController.navigate(Navigation.FullScreenError(arguments, redirectToWelcome)) {
                    popUpTo<Navigation.ActivateAccountScreen> {
                        inclusive = true
                    }
                }
            }
        )
    }
}

private fun NavGraphBuilder.homeFlow(navigationController: NavHostController) {
    composable<Navigation.EventListScreen>(
        typeMap = mapOf(typeOf<Event>() to serializableType<Event>())
    ) {
        EventListScreen(onEventClick = { event ->
            navigationController.navigate(Navigation.EventDetailScreen(event))
        })
    }
    composable<Navigation.EventDetailScreen>(
        typeMap = mapOf(typeOf<Event>() to serializableType<Event>())
    ) { backStackEntry ->
        EventDetailsScreen(
            event = backStackEntry.toRoute<Navigation.EventDetailScreen>().event,
            onBackPressed = { navigationController.popBackStack() },
            onLoginClicked = { navigationController.navigate(Navigation.LoginScreen) },
            onJoinEventError = { navigationController.navigate(Navigation.FullScreenError(esmorgaErrorScreenArguments = it)) },
            onNoNetworkError = { navigationController.navigate(Navigation.FullScreenError(esmorgaErrorScreenArguments = it)) }
        )
    }
    composable<Navigation.MyEventsScreen>(
        typeMap = mapOf(typeOf<Event>() to serializableType<Event>())
    ) {
        MyEventListScreen(onEventClick = { event ->
            navigationController.navigate(Navigation.EventDetailScreen(event))
        }, onSignInClick = {
            navigationController.navigate(Navigation.LoginScreen)
        })
    }
    composable<Navigation.ProfileScreen> {
        ProfileScreen(
            navigateLogIn = { navigationController.navigate(Navigation.LoginScreen) },
            onNoNetworkError = { navigationController.navigate(Navigation.FullScreenError(esmorgaErrorScreenArguments = it)) }
        )
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
            onForgotPasswordClicked = {
                navigationController.navigate(Navigation.RecoverPasswordScreen)
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
            onRegistrationSuccess = { email ->
                navigationController.navigate(Navigation.RegistrationConfirmationScreen(email = email))
            },
            onRegistrationError = { esmorgaFullScreenArguments ->
                navigationController.navigate(Navigation.FullScreenError(esmorgaErrorScreenArguments = esmorgaFullScreenArguments))
            },
            onBackClicked = {
                navigationController.popBackStack()
            }
        )
    }
    composable<Navigation.RegistrationConfirmationScreen> { backStackEntry ->
        val email = backStackEntry.toRoute<Navigation.RegistrationConfirmationScreen>().email
        RegistrationConfirmationScreen(
            onBackClicked = { navigationController.popBackStack() },
            email = email
        )
    }
    composable<Navigation.RecoverPasswordScreen> {
        RecoverPasswordScreen(
            onBackClicked = { navigationController.popBackStack() },
            onRecoverPasswordError = { esmorgaFullScreenArguments ->
                navigationController.navigate(Navigation.FullScreenError(esmorgaErrorScreenArguments = esmorgaFullScreenArguments))
            },
        )
    }
}

private fun NavGraphBuilder.errorFlow(navigationController: NavHostController) {
    composable<Navigation.FullScreenError>(
        typeMap = mapOf(typeOf<EsmorgaErrorScreenArguments>() to serializableType<EsmorgaErrorScreenArguments>())
    ) { backStackEntry ->
        val esmorgaErrorScreenArguments = backStackEntry.toRoute<Navigation.FullScreenError>().esmorgaErrorScreenArguments
        val redirectToWelcome = backStackEntry.toRoute<Navigation.FullScreenError>().redirectToWelcome
        EsmorgaErrorScreen(
            esmorgaErrorScreenArguments = esmorgaErrorScreenArguments,
            onButtonPressed = {
                if (redirectToWelcome) {
                    navigationController.navigate(Navigation.WelcomeScreen) {
                        popUpTo<Navigation.FullScreenError> {
                            inclusive = true
                        }
                    }
                } else {
                    navigationController.popBackStack()
                }
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

fun openEmailApp(context: Context) {
    val intent = Intent(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_APP_EMAIL)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)
}

private fun isPackageAvailable(context: Context, appPackage: String) = try {
    val appInfo = context.packageManager?.getApplicationInfo(appPackage, 0)
    appInfo != null && appInfo.enabled
} catch (e: PackageManager.NameNotFoundException) {
    false
}