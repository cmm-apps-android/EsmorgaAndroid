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
import cmm.apps.esmorga.domain.event.model.CreateEventForm
import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.domain.poll.model.Poll
import cmm.apps.esmorga.view.activateaccount.ActivateAccountScreen
import cmm.apps.esmorga.view.changepassword.ChangePasswordScreen
import cmm.apps.esmorga.view.createevent.CreateEventFormScreen
import cmm.apps.esmorga.view.createeventdate.CreateEventFormDateScreen
import cmm.apps.esmorga.view.createeventlocation.CreateEventFormLocationScreen
import cmm.apps.esmorga.view.createeventtype.CreateEventFormTypeScreen
import cmm.apps.esmorga.view.deeplink.DeeplinkManager.navigateFromDeeplink
import cmm.apps.esmorga.view.errors.EsmorgaErrorScreen
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArguments
import cmm.apps.esmorga.view.eventattendees.EventAttendeesScreen
import cmm.apps.esmorga.view.eventdetails.EventDetailsScreen
import cmm.apps.esmorga.view.explore.ExploreScreen
import cmm.apps.esmorga.view.login.LoginScreen
import cmm.apps.esmorga.view.myeventlist.MyEventListScreen
import cmm.apps.esmorga.view.password.RecoverPasswordScreen
import cmm.apps.esmorga.view.password.ResetPasswordScreen
import cmm.apps.esmorga.view.polldetails.PollDetailsScreen
import cmm.apps.esmorga.view.profile.ProfileScreen
import cmm.apps.esmorga.view.registration.RegistrationConfirmationScreen
import cmm.apps.esmorga.view.registration.RegistrationScreen
import kotlinx.serialization.Serializable
import org.jetbrains.annotations.VisibleForTesting
import kotlin.reflect.typeOf

sealed class Navigation {

    @Serializable
    data object ExploreScreen : Navigation()

    @Serializable
    data class EventDetailScreen(val event: Event) : Navigation()

    @Serializable
    data class PollDetailScreen(val poll: Poll) : Navigation()

    @Serializable
    data class EventAttendeesScreen(val event: Event) : Navigation()

    @Serializable
    data class LoginScreen(val snackbarArguments: String? = null) : Navigation()

    @Serializable
    data object RegistrationScreen : Navigation()

    @Serializable
    data class RegistrationConfirmationScreen(val email: String) : Navigation()

    @Serializable
    data class FullScreenError(val esmorgaErrorScreenArguments: EsmorgaErrorScreenArguments) : Navigation()

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
    data object ChangePasswordScreen : Navigation()

    @Serializable
    data object CreateEventFormTitleScreen : Navigation()

    @Serializable
    data class CreateEventFormTypeScreen(val form: CreateEventForm) : Navigation()

    @Serializable
    data class CreateEventFormDateScreen(val form: CreateEventForm) : Navigation()

    @Serializable
    data class CreateEventFormLocationScreen(val form: CreateEventForm) : Navigation()
}

const val GOOGLE_MAPS_PACKAGE = "com.google.android.apps.maps"

@Composable
fun EsmorgaNavigationGraph(navigationController: NavHostController, deeplinkPath: Uri?) {
    val startDestination = if (deeplinkPath != null) {
        navigateFromDeeplink(deeplinkPath)
    } else {
        Navigation.ExploreScreen
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
        homeFlow(navigationController)
        loginFlow(navigationController)
        createEventFlow(navigationController)
        resetPasswordFlow(navigationController)
        errorFlow(navigationController)
    }
}

private fun NavGraphBuilder.homeFlow(navigationController: NavHostController) {
    composable<Navigation.ExploreScreen> {
        ExploreScreen(onEventClick = { event ->
            navigationController.navigate(Navigation.EventDetailScreen(event))
        }, onPollClick = { poll ->
            navigationController.navigate(Navigation.PollDetailScreen(poll))
        })
    }
    composable<Navigation.EventDetailScreen>(
        typeMap = mapOf(typeOf<Event>() to serializableType<Event>())
    ) { backStackEntry ->
        EventDetailsScreen(
            event = backStackEntry.toRoute<Navigation.EventDetailScreen>().event,
            onBackPressed = { navigationController.popBackStack() },
            onLoginClicked = { navigationController.navigate(Navigation.LoginScreen()) },
            onViewAttendeesClicked = { event -> navigationController.navigate(Navigation.EventAttendeesScreen(event)) },
            onJoinEventError = { navigationController.navigate(Navigation.FullScreenError(esmorgaErrorScreenArguments = it)) },
            onNoNetworkError = { navigationController.navigate(Navigation.FullScreenError(esmorgaErrorScreenArguments = it)) }
        )
    }
    composable<Navigation.PollDetailScreen>(
        typeMap = mapOf(typeOf<Poll>() to serializableType<Poll>())
    ) { backStackEntry ->
        PollDetailsScreen(
            poll = backStackEntry.toRoute<Navigation.PollDetailScreen>().poll,
            onBackPressed = { navigationController.popBackStack() },
            onVoteError = { navigationController.navigate(Navigation.FullScreenError(esmorgaErrorScreenArguments = it)) },
            onNoNetworkError = { navigationController.navigate(Navigation.FullScreenError(esmorgaErrorScreenArguments = it)) }
        )
    }
    composable<Navigation.EventAttendeesScreen>(
        typeMap = mapOf(typeOf<Event>() to serializableType<Event>())
    ) { backStackEntry ->
        EventAttendeesScreen(
            event = backStackEntry.toRoute<Navigation.EventAttendeesScreen>().event,
            onBackPressed = { navigationController.popBackStack() },
            onError = { navigationController.navigate(Navigation.FullScreenError(esmorgaErrorScreenArguments = it)) }
        )
    }
    composable<Navigation.MyEventsScreen> {
        MyEventListScreen(onEventClick = { event ->
            navigationController.navigate(Navigation.EventDetailScreen(event))
        }, onSignInClick = {
            navigationController.navigate(Navigation.LoginScreen())
        }, onCreateEventClick = {
            navigationController.navigate(Navigation.CreateEventFormTitleScreen)
        })
    }
    composable<Navigation.ProfileScreen> {
        ProfileScreen(
            navigateLogIn = { navigationController.navigate(Navigation.LoginScreen()) },
            onNoNetworkError = { navigationController.navigate(Navigation.FullScreenError(esmorgaErrorScreenArguments = it)) },
            onChangePasswordClick = { navigationController.navigate(Navigation.ChangePasswordScreen) }
        )
    }

    composable<Navigation.ChangePasswordScreen> {
        ChangePasswordScreen(
            onChangePasswordSuccess = { message ->
                navigationController.navigate(Navigation.LoginScreen(message)) {
                    popUpTo(0) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            onChangePasswordError = { errorArguments ->
                navigationController.navigate(Navigation.FullScreenError(esmorgaErrorScreenArguments = errorArguments))
            },
            onBackPressed = {
                navigationController.popBackStack()
            }
        )
    }
}

private fun NavGraphBuilder.loginFlow(navigationController: NavHostController) {
    composable<Navigation.LoginScreen> { backStackEntry ->
        LoginScreen(
            onRegisterClicked = {
                navigationController.navigate(Navigation.RegistrationScreen)
            },
            onForgotPasswordClicked = {
                navigationController.navigate(Navigation.RecoverPasswordScreen)
            },
            onLoginSuccess = {
                navigationController.navigate(Navigation.ExploreScreen) {
                    popUpTo(0) {
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
    composable<Navigation.ActivateAccountScreen> { backStackEntry ->
        ActivateAccountScreen(
            backStackEntry.toRoute<Navigation.ActivateAccountScreen>().verificationCode,
            onContinueClick = {
                navigationController.navigate(Navigation.ExploreScreen) {
                    popUpTo(0) {
                        inclusive = true
                    }
                }
            },
            onError = {
                navigationController.navigate(Navigation.FullScreenError(it))
            },
            onLastTryError = { arguments ->
                navigationController.navigate(Navigation.FullScreenError(arguments)) {
                    popUpTo<Navigation.ActivateAccountScreen> {
                        inclusive = true
                    }
                }
            }
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

private fun NavGraphBuilder.createEventFlow(navController: NavHostController) {
    composable<Navigation.CreateEventFormTitleScreen> {
        CreateEventFormScreen(
            onBack = { navController.popBackStack() },
            onNext = { form ->
                navController.navigate(Navigation.CreateEventFormTypeScreen(form))
            }
        )
    }

    composable<Navigation.CreateEventFormTypeScreen>(
        typeMap = mapOf(typeOf<CreateEventForm>() to serializableType<CreateEventForm>())
    ) { backStackEntry ->
        val form = backStackEntry.toRoute<Navigation.CreateEventFormTypeScreen>().form
        CreateEventFormTypeScreen(
            eventForm = form,
            onBackClick = { navController.popBackStack() },
            onNextClick = { updatedForm ->
                navController.navigate(Navigation.CreateEventFormDateScreen(updatedForm))
            }
        )
    }

    composable<Navigation.CreateEventFormDateScreen>(
        typeMap = mapOf(typeOf<CreateEventForm>() to serializableType<CreateEventForm>())
    ) { backStackEntry ->
        val eventForm = backStackEntry.toRoute<Navigation.CreateEventFormDateScreen>().form
        CreateEventFormDateScreen(
            eventForm = eventForm,
            onBackPressed = { navController.popBackStack() },
            onNextClick = { updatedForm ->
                navController.navigate(Navigation.CreateEventFormLocationScreen(updatedForm))
            }
        )
    }

    composable<Navigation.CreateEventFormLocationScreen>(
        typeMap = mapOf(typeOf<CreateEventForm>() to serializableType<CreateEventForm>())
    ) { backStackEntry ->
        val eventForm = backStackEntry.toRoute<Navigation.CreateEventFormLocationScreen>().form
        CreateEventFormLocationScreen(
            eventForm = eventForm,
            onBackPressed = { navController.popBackStack() },
            onNextClick = { updatedForm -> }
        )
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
