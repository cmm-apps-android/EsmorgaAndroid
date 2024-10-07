package cmm.apps.esmorga.view.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import cmm.apps.designsystem.ErrorScreenTestTags.ERROR_RETRY_BUTTON
import cmm.apps.designsystem.ErrorScreenTestTags.ERROR_TITLE
import cmm.apps.esmorga.domain.event.GetEventDetailsUseCase
import cmm.apps.esmorga.domain.event.GetEventListUseCase
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.domain.result.Source
import cmm.apps.esmorga.domain.user.GetSavedUserUseCase
import cmm.apps.esmorga.domain.user.PerformLoginUseCase
import cmm.apps.esmorga.domain.user.PerformRegistrationUserCase
import cmm.apps.esmorga.view.di.ViewDIModule
import cmm.apps.esmorga.view.eventdetails.EventDetailsScreenTestTags
import cmm.apps.esmorga.view.eventdetails.EventDetailsScreenTestTags.EVENT_DETAILS_BACK_BUTTON
import cmm.apps.esmorga.view.eventdetails.EventDetailsScreenTestTags.EVENT_DETAILS_EVENT_NAME
import cmm.apps.esmorga.view.eventlist.EventListScreenTestTags.EVENT_LIST_EVENT_NAME
import cmm.apps.esmorga.view.eventlist.EventListScreenTestTags.EVENT_LIST_TITLE
import cmm.apps.esmorga.view.login.LoginScreenTestTags.LOGIN_EMAIL_INPUT
import cmm.apps.esmorga.view.login.LoginScreenTestTags.LOGIN_LOGIN_BUTTON
import cmm.apps.esmorga.view.login.LoginScreenTestTags.LOGIN_PASSWORD_INPUT
import cmm.apps.esmorga.view.login.LoginScreenTestTags.LOGIN_REGISTER_BUTTON
import cmm.apps.esmorga.view.login.LoginScreenTestTags.LOGIN_TITLE
import cmm.apps.esmorga.view.navigation.HomeScreenTestTags.MY_EVENTS_TITLE
import cmm.apps.esmorga.view.navigation.HomeScreenTestTags.PROFILE__TITLE
import cmm.apps.esmorga.view.registration.RegistrationScreenTestTags.REGISTRATION_BACK_BUTTON
import cmm.apps.esmorga.view.registration.RegistrationScreenTestTags.REGISTRATION_TITLE
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
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.robolectric.shadows.ShadowLog


@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: NavHostController

    private val getEventListUseCase = mockk<GetEventListUseCase>(relaxed = true).also { useCase ->
        coEvery { useCase() } returns EsmorgaResult.success(EventViewMock.provideEventList(listOf("event")))
    }

    private val getEventDetailsUseCase = mockk<GetEventDetailsUseCase>(relaxed = true).also { useCase ->
        coEvery { useCase(any()) } returns EsmorgaResult.success(EventViewMock.provideEvent("event"))
    }

    private val performLoginUseCase = mockk<PerformLoginUseCase>(relaxed = true).also { useCase ->
        coEvery { useCase(any(), any()) } returns EsmorgaResult.success(LoginViewMock.provideUser())
    }

    private val performRegistrationUserCase = mockk<PerformRegistrationUserCase>(relaxed = true).also { useCase ->
        coEvery { useCase(any(), any(), any(), any()) } returns EsmorgaResult.success(LoginViewMock.provideUser())
    }

    private val getSavedUserUseCase = mockk<GetSavedUserUseCase>(relaxed = true).also { useCase ->
        coEvery { useCase() } returns EsmorgaResult.success(LoginViewMock.provideUser())
    }

    @Before
    @Throws(Exception::class)
    fun setUp() {
        ShadowLog.stream = System.out // Redirect Logcat to console

        startKoin {
            allowOverride(true)
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
        setNavigationFromAppLaunch(loggedIn = false)

        composeTestRule.onNodeWithTag(WELCOME_PRIMARY_BUTTON).assertIsDisplayed()
        composeTestRule.onNodeWithTag(WELCOME_SECONDARY_BUTTON).assertIsDisplayed()
    }

    @Test
    fun `given user logged, when app is open, then event list screen is shown`() {
        setNavigationFromAppLaunch(loggedIn = true)

        composeTestRule.onNodeWithTag(EVENT_LIST_TITLE).assertIsDisplayed()
    }

    @Test
    fun `given user not logged, when app is open and continue as guest is clicked, then events screen is shown`() {
        setNavigationFromAppLaunch(loggedIn = false)

        composeTestRule.onNodeWithTag(WELCOME_SECONDARY_BUTTON).performClick()

        composeTestRule.onNodeWithTag(EVENT_LIST_TITLE).assertIsDisplayed()
    }

    @Test
    fun `given user not logged, when app is open and login is clicked, then login screen is shown`() {
        setNavigationFromAppLaunch(loggedIn = false)

        composeTestRule.onNodeWithTag(WELCOME_PRIMARY_BUTTON).performClick()

        composeTestRule.onNodeWithTag(LOGIN_TITLE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(LOGIN_LOGIN_BUTTON).assertIsDisplayed()
        composeTestRule.onNodeWithTag(LOGIN_REGISTER_BUTTON).assertIsDisplayed()
    }

    @Test
    fun `given user not logged, when login visited and login is performed, then event list screen is shown`() {
        setNavigationFromDestination(Navigation.LoginScreen)

        composeTestRule.onNodeWithTag(LOGIN_EMAIL_INPUT).performTextInput("simple@man.com")
        composeTestRule.onNodeWithTag(LOGIN_PASSWORD_INPUT).performTextInput("Test@123")
        composeTestRule.onNodeWithTag(LOGIN_LOGIN_BUTTON).performClick()

        composeTestRule.onNodeWithTag(EVENT_LIST_TITLE).assertIsDisplayed()
    }

    @Test
    fun `given user not logged, when login visited and register is clicked, then register screen is shown`() {
        setNavigationFromDestination(Navigation.LoginScreen)

        composeTestRule.onNodeWithTag(LOGIN_REGISTER_BUTTON).performClick()

        composeTestRule.onNodeWithTag(REGISTRATION_TITLE).assertIsDisplayed()
    }

    @Test
    fun `given user not logged, when login, register and back are clicked, then login screen is shown`() {
        setNavigationFromDestination(Navigation.LoginScreen)

        composeTestRule.onNodeWithTag(LOGIN_REGISTER_BUTTON).performClick()
        composeTestRule.onNodeWithTag(REGISTRATION_BACK_BUTTON).performClick()

        composeTestRule.onNodeWithTag(LOGIN_TITLE).assertIsDisplayed()
    }

    @Test
    fun `given user not logged, when login is visited and login fails, then error screen is shown`() {
        val failurePerformLoginUseCase = mockk<PerformLoginUseCase>(relaxed = true).also { useCase ->
            coEvery { useCase(any(), any()) } returns EsmorgaResult.failure(EsmorgaException("Mock error", Source.REMOTE, 401))
        }
        loadKoinModules(module { factory<PerformLoginUseCase> { failurePerformLoginUseCase } })

        setNavigationFromDestination(Navigation.LoginScreen)

        composeTestRule.onNodeWithTag(LOGIN_TITLE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(LOGIN_EMAIL_INPUT).performTextInput("simple@man.com")
        composeTestRule.onNodeWithTag(LOGIN_PASSWORD_INPUT).performTextInput("Test@123")
        composeTestRule.onNodeWithTag(LOGIN_LOGIN_BUTTON).performClick()

        composeTestRule.onNodeWithTag(ERROR_TITLE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(ERROR_RETRY_BUTTON).performClick()

        composeTestRule.onNodeWithTag(LOGIN_TITLE).assertIsDisplayed()
    }

    @Test
    fun `given user logged, when event list screen visited and event is clicked, then event detail is shown`() {
        setNavigationFromDestination(Navigation.EventListScreen)

        composeTestRule.onNodeWithTag(EVENT_LIST_EVENT_NAME, true).performClick()
        composeTestRule.onNodeWithTag(EVENT_DETAILS_EVENT_NAME).assertIsDisplayed()
    }

    @Test
    fun `given user logged, when visiting event list, details and back clicked, then event list is shown`() {
        setNavigationFromDestination(Navigation.EventListScreen)

        composeTestRule.onNodeWithTag(EVENT_LIST_EVENT_NAME, true).performClick()
        composeTestRule.onNodeWithTag(EVENT_DETAILS_BACK_BUTTON).performClick()
        composeTestRule.onNodeWithTag(EVENT_LIST_TITLE).assertIsDisplayed()
    }

    @Test
    fun `given user logged, when event detail screen visited and login button is clicked, then login screen is visited`() {
        val getSavedUserUseCaseFailure = mockk<GetSavedUserUseCase>(relaxed = true).also { useCase ->
            coEvery { useCase() } returns EsmorgaResult.failure(Exception())
        }
        loadKoinModules(module { factory<GetSavedUserUseCase> { getSavedUserUseCaseFailure } })

        setNavigationFromDestination(Navigation.EventListScreen)

        composeTestRule.onNodeWithTag(EVENT_LIST_TITLE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(EVENT_LIST_EVENT_NAME, true).performClick()
        composeTestRule.onNodeWithTag(EVENT_DETAILS_EVENT_NAME).assertIsDisplayed()
        composeTestRule.onNodeWithTag(EventDetailsScreenTestTags.EVENT_DETAIL_PRIMARY_BUTTON, true).performClick()
        composeTestRule.onNodeWithTag(LOGIN_TITLE).assertIsDisplayed()
    }

    //TODO Modify this two last tests with the correct screen when the screens will be done
    @Test
    fun `given main screen, when clicks on bottom bar my events item, then my events screen is shown`() {
        setNavigationFromDestination(Navigation.MyEventsScreen)
        composeTestRule.onNodeWithTag(MY_EVENTS_TITLE).assertIsDisplayed()
    }

    @Test
    fun `given main screen, when clicks on profile nav item, then profile screen is shown`() {
        setNavigationFromDestination(Navigation.ProfileScreen)
        composeTestRule.onNodeWithTag(PROFILE__TITLE).assertIsDisplayed()
    }

    private fun setNavigationFromAppLaunch(loggedIn: Boolean) {
        composeTestRule.setContent {
            KoinContext {
                navController = rememberNavController()
                EsmorgaNavigationGraph(navigationController = navController, loggedIn = loggedIn)
            }
        }
    }

    private fun setNavigationFromDestination(startDestination: Navigation) {
        composeTestRule.setContent {
            KoinContext {
                navController = rememberNavController()
                EsmorgaNavHost(navigationController = navController, startDestination = startDestination)
            }
        }
    }


    private fun printComposeUiTreeToLog(testTag: String? = null) {
        if (testTag.isNullOrEmpty()) {
            composeTestRule.onRoot().printToLog("TAG")
        } else {
            composeTestRule.onNodeWithTag(testTag).printToLog("TAG")
        }
    }
}