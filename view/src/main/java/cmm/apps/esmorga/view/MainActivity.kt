package cmm.apps.esmorga.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
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
import cmm.apps.esmorga.view.navigation.EsmorgaNavHost
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
            EsmorgaNavHost(navigationController = navigationController, loggedIn = loggedIn)
        }
    }

}
