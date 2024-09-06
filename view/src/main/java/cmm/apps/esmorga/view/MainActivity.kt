package cmm.apps.esmorga.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
import cmm.apps.esmorga.view.registration.RegistrationScreen
import cmm.apps.esmorga.view.welcome.WelcomeScreen
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.reflect.typeOf

class MainActivity : ComponentActivity() {

    private val mvm: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        var uiState: MainUiState by mutableStateOf(MainUiState())
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                mvm.uiState.onEach { uiState = it }.collect {
                    if (!it.loading) {
                        setupNavigation(it.loggedIn)
                    }
                }
            }
        }

        splashScreen.setKeepOnScreenCondition {
            uiState.loading
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
    }

    private fun setupNavigation(loggedIn: Boolean) {
        setContent {
            val navigationController = rememberNavController()
            val startDestination = if (loggedIn) Navigation.EventListScreen else Navigation.WelcomeScreen
            NavHost(navController = navigationController, startDestination = startDestination) {
                loginFlow(navigationController)
                eventFlow(navigationController)
                errorFlow(navigationController)
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
}
