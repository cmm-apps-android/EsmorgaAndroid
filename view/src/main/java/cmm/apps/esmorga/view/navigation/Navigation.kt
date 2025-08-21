package cmm.apps.esmorga.view.navigation

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.view.activateaccount.ActivateAccountScreen
import cmm.apps.esmorga.view.createevent.CreateEventFormScreen
import cmm.apps.esmorga.view.createeventtype.CreateEventTypeScreen
import cmm.apps.esmorga.view.createeventtype.CreateEventTypeViewModel
import cmm.apps.esmorga.view.createeventtype.model.CreateEventTypeScreenEffect
import cmm.apps.esmorga.view.deeplink.DeeplinkManager.navigateFromDeeplink
import cmm.apps.esmorga.view.errors.EsmorgaErrorScreen
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArguments
import cmm.apps.esmorga.view.eventdetails.EventDetailsScreen
import cmm.apps.esmorga.view.eventlist.EventListScreen
import cmm.apps.esmorga.view.eventlist.MyEventListScreen
import cmm.apps.esmorga.view.login.LoginScreen
import cmm.apps.esmorga.view.password.RecoverPasswordScreen
import cmm.apps.esmorga.view.password.ResetPasswordScreen
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
    data class LoginScreen(val snackbarArguments: String? = null) : Navigation()

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
    data object RecoverPasswordScreen : Navigation()

    @Serializable
    data class ActivateAccountScreen(val verificationCode: String) : Navigation()

    @Serializable
    data class ResetPasswordScreen(val forgotPasswordCode: String) : Navigation()

    @Serializable
    data object CreateEventFormScreen : Navigation()

    @Serializable
    data class CreateEventTypeScreen(val eventName: String, val description: String) : Navigation() {
        fun toRoute() = "CreateEventTypeScreen?eventName=$eventName&description=$description"
    }
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
        resetPasswordFlow(navigationController)
        createEventFlow(navigationController)
    }
}

private fun NavGraphBuilder.resetPasswordFlow(navigationController: NavHostController) {
    composable<Navigation.ResetPasswordScreen> { backStackEntry ->
        ResetPasswordScreen(
            forgotPasswordCode = backStackEntry.toRoute<Navigation.ResetPasswordScreen>().forgotPasswordCode,
            onResetPasswordError = { esmorgaFullScreenArguments ->
                navigationController.navigate(Navigation.FullScreenError(esmorgaErrorScreenArguments = esmorgaFullScreenArguments))
            },
            onResetPasswordSuccess = { message ->
                navigationController.navigate(Navigation.LoginScreen(message)) {
                    popUpTo(0) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        )

    }
}

private fun NavGraphBuilder.createEventFlow(navigationController: NavHostController) {
    composable<Navigation.CreateEventFormScreen> {
        CreateEventFormScreen(
            onBack = { navigationController.popBackStack() },
            onNext = { eventName, description ->
                navigationController.navigate(
                    Navigation.CreateEventTypeScreen(eventName = eventName, description = description)
                )
            }
        )
    }

    composable<Navigation.CreateEventTypeScreen> { navBackStackEntry ->
        val args = navBackStackEntry.toRoute<Navigation.CreateEventTypeScreen>()
        val eventName = args.eventName
        val description = args.description

        val viewModel: CreateEventTypeViewModel = viewModel(
            factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(CreateEventTypeViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return CreateEventTypeViewModel(eventName, description) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        )

        val lifecycleOwner = LocalLifecycleOwner.current

        LaunchedEffect(lifecycleOwner) {
            viewModel.effect.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect { effect ->
                    when (effect) {
                        is CreateEventTypeScreenEffect.NavigateBack -> {
                            navigationController.popBackStack()
                        }

                        is CreateEventTypeScreenEffect.NavigateNext -> {
                        }
                    }
                }
        }

        CreateEventTypeScreen(
            viewModel = viewModel,
            onBackClick = {
                viewModel.onBackClick()
            },
            onNextClick = {
                viewModel.onNextClick()
            }
        )
    }
}

private fun NavGraphBuilder.accountActivationFlow(navigationController: NavHostController) {
    composable<Navigation.ActivateAccountScreen> { backStackEntry ->
        ActivateAccountScreen(
            backStackEntry.toRoute<Navigation.ActivateAccountScreen>().verificationCode,
            onContinueClick = {
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
            onLoginClicked = { navigationController.navigate(Navigation.LoginScreen()) },
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
            navigationController.navigate(Navigation.LoginScreen())
        }, onCreateEventClick = {
            navigationController.navigate(Navigation.CreateEventFormScreen)
        })
    }
    composable<Navigation.ProfileScreen> {
        ProfileScreen(
            navigateLogIn = { navigationController.navigate(Navigation.LoginScreen()) },
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
                navigationController.navigate(Navigation.LoginScreen())
            })
    }
    composable<Navigation.LoginScreen> { backStackEntry ->
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
            },
            snackbarMessage = backStackEntry.toRoute<Navigation.LoginScreen>().snackbarArguments
        )
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
            }
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

fun openNavigationApp(context: Context, latitude: Double, longitude: Double, markerLabel: String) {
    val uri = Uri.parse("geo:?q=loc:$latitude,$longitude ($markerLabel)")
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
