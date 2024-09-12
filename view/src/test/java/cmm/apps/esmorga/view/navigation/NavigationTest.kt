package cmm.apps.esmorga.view.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import cmm.apps.esmorga.domain.event.GetEventDetailsUseCase
import cmm.apps.esmorga.domain.event.GetEventListUseCase
import cmm.apps.esmorga.domain.result.Success
import cmm.apps.esmorga.domain.user.GetSavedUserUseCase
import cmm.apps.esmorga.domain.user.PerformLoginUseCase
import cmm.apps.esmorga.domain.user.PerformRegistrationUserCase
import cmm.apps.esmorga.view.di.ViewDIModule
import cmm.apps.esmorga.view.eventlist.EventListScreenTestTags.EVENT_LIST_TITLE
import cmm.apps.esmorga.view.viewmodel.mock.EventViewMock
import cmm.apps.esmorga.view.viewmodel.mock.LoginViewMock
import cmm.apps.esmorga.view.welcome.WelcomeScreenTestTags.WELCOME_PRIMARY_BUTTON
import cmm.apps.esmorga.view.welcome.WelcomeScreenTestTags.WELCOME_SECONDARY_BUTTON
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.compose.KoinContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.robolectric.shadows.ShadowLog


@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    lateinit var navController: NavHostController

    val getEventListUseCase: GetEventListUseCase = mockk<GetEventListUseCase>(relaxed = true).also { useCase ->
        coEvery { useCase() } returns Result.success(Success(listOf()))
    }

    val getEventDetailsUseCase: GetEventDetailsUseCase = mockk<GetEventDetailsUseCase>(relaxed = true).also { useCase ->
        coEvery { useCase(any()) } returns Result.success(Success(EventViewMock.provideEvent("event")))
    }

    val performLoginUseCase: PerformLoginUseCase = mockk<PerformLoginUseCase>(relaxed = true).also { useCase ->
        coEvery { useCase(any(), any()) } returns Result.success(Success(LoginViewMock.provideUser()))
    }

    val performRegistrationUserCase: PerformRegistrationUserCase = mockk<PerformRegistrationUserCase>(relaxed = true).also { useCase ->
        coEvery { useCase(any(), any(), any(), any()) } returns Result.success(Success(LoginViewMock.provideUser()))
    }

    val getSavedUserUseCase: GetSavedUserUseCase = mockk<GetSavedUserUseCase>(relaxed = true).also { useCase ->
        coEvery { useCase() } returns Result.success(Success(LoginViewMock.provideUser()))
    }

    @Before
    @Throws(Exception::class)
    fun setUp() {
        ShadowLog.stream = System.out // Redirect Logcat to console

        startKoin {
            androidContext(ApplicationProvider.getApplicationContext())
            modules(
                ViewDIModule.module,
                module {
                    factory<GetEventListUseCase> { getEventListUseCase }
                    factory<GetEventDetailsUseCase> { getEventDetailsUseCase }
                    factory<PerformLoginUseCase> { performLoginUseCase }
                    factory<PerformRegistrationUserCase> { performRegistrationUserCase }
                    factory<GetSavedUserUseCase> { getSavedUserUseCase }
                }
            )
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `given user not logged, when app is open, then welcome screen is shown`() {
        composeTestRule.setContent {
            KoinContext {
                navController = rememberNavController()
                EsmorgaNavHost(navigationController = navController, loggedIn = false)
            }
        }

        composeTestRule.onNodeWithTag(WELCOME_PRIMARY_BUTTON).assertIsDisplayed()
        composeTestRule.onNodeWithTag(WELCOME_SECONDARY_BUTTON).assertIsDisplayed()
    }

    @Test
    fun `given user logged, when app is open, then event list screen is shown`() {
        composeTestRule.setContent {
            KoinContext {
                navController = rememberNavController()
                EsmorgaNavHost(navigationController = navController, loggedIn = true)
            }
        }

        composeTestRule.onNodeWithTag(EVENT_LIST_TITLE).assertIsDisplayed()
    }

    private fun printComposeUiTreeToLog(testTag: String? = null) {
        if (testTag.isNullOrEmpty()) {
            composeTestRule.onRoot().printToLog("TAG")
        } else {
            composeTestRule.onNodeWithTag(testTag).printToLog("TAG")
        }
    }
}