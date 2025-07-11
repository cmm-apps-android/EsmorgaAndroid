package cmm.apps.esmorga.view.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import cmm.apps.designsystem.ErrorScreenTestTags.ERROR_ANIMATION
import cmm.apps.designsystem.ErrorScreenTestTags.ERROR_RETRY_BUTTON
import cmm.apps.designsystem.ErrorScreenTestTags.ERROR_SUBTITLE
import cmm.apps.designsystem.ErrorScreenTestTags.ERROR_TITLE
import cmm.apps.esmorga.domain.account.ActivateAccountUseCase
import cmm.apps.esmorga.domain.device.GetDeviceIdUseCase
import cmm.apps.esmorga.domain.device.ShowDeviceIdIfNeededUseCase
import cmm.apps.esmorga.domain.event.GetEventListUseCase
import cmm.apps.esmorga.domain.event.GetMyEventListUseCase
import cmm.apps.esmorga.domain.event.JoinEventUseCase
import cmm.apps.esmorga.domain.event.LeaveEventUseCase
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.domain.result.Source
import cmm.apps.esmorga.domain.user.GetSavedUserUseCase
import cmm.apps.esmorga.domain.user.LogOutUseCase
import cmm.apps.esmorga.domain.user.PerformLoginUseCase
import cmm.apps.esmorga.domain.user.PerformRecoverPasswordUseCase
import cmm.apps.esmorga.domain.user.PerformRegistrationConfirmationUseCase
import cmm.apps.esmorga.domain.user.PerformRegistrationUserCase
import cmm.apps.esmorga.domain.user.repository.PerformResetPasswordUseCase
import cmm.apps.esmorga.view.activateaccount.RegistrationConfirmationScreenTestTags.ACTIVATE_ACCOUNT_BUTTON
import cmm.apps.esmorga.view.activateaccount.RegistrationConfirmationScreenTestTags.ACTIVATE_ACCOUNT_IMAGE
import cmm.apps.esmorga.view.activateaccount.RegistrationConfirmationScreenTestTags.ACTIVATE_ACCOUNT_SUBTITLE
import cmm.apps.esmorga.view.activateaccount.RegistrationConfirmationScreenTestTags.ACTIVATE_ACCOUNT_TITLE
import cmm.apps.esmorga.view.di.ViewDIModule
import cmm.apps.esmorga.view.eventdetails.EventDetailsScreenTestTags.EVENT_DETAILS_BACK_BUTTON
import cmm.apps.esmorga.view.eventdetails.EventDetailsScreenTestTags.EVENT_DETAILS_EVENT_NAME
import cmm.apps.esmorga.view.eventdetails.EventDetailsScreenTestTags.EVENT_DETAIL_PRIMARY_BUTTON
import cmm.apps.esmorga.view.eventlist.EventListScreenTestTags.EVENT_LIST_EVENT_NAME
import cmm.apps.esmorga.view.eventlist.EventListScreenTestTags.EVENT_LIST_TITLE
import cmm.apps.esmorga.view.eventlist.MyEventListScreenTestTags.MY_EVENT_LIST_TITLE
import cmm.apps.esmorga.view.login.LoginScreenTestTags.LOGIN_EMAIL_INPUT
import cmm.apps.esmorga.view.login.LoginScreenTestTags.LOGIN_FORGOT_PASSWORD_BUTTON
import cmm.apps.esmorga.view.login.LoginScreenTestTags.LOGIN_LOGIN_BUTTON
import cmm.apps.esmorga.view.login.LoginScreenTestTags.LOGIN_PASSWORD_INPUT
import cmm.apps.esmorga.view.login.LoginScreenTestTags.LOGIN_REGISTER_BUTTON
import cmm.apps.esmorga.view.login.LoginScreenTestTags.LOGIN_TITLE
import cmm.apps.esmorga.view.password.RecoverPasswordScreenTestTags.RECOVER_PASSWORD_BACK_BUTTON
import cmm.apps.esmorga.view.password.RecoverPasswordScreenTestTags.RECOVER_PASSWORD_EMAIL_INPUT
import cmm.apps.esmorga.view.password.RecoverPasswordScreenTestTags.RECOVER_PASSWORD_SEND_EMAIL_BUTTON
import cmm.apps.esmorga.view.password.RecoverPasswordScreenTestTags.RECOVER_PASSWORD_SHOW_SNACKBAR
import cmm.apps.esmorga.view.password.RecoverPasswordScreenTestTags.RECOVER_PASSWORD_TITLE
import cmm.apps.esmorga.view.password.ResetPasswordScreenTestTags.RESET_PASSWORD_CHANGE_PASSWORD_BUTTON
import cmm.apps.esmorga.view.password.ResetPasswordScreenTestTags.RESET_PASSWORD_NEW_PASSWORD_INPUT
import cmm.apps.esmorga.view.password.ResetPasswordScreenTestTags.RESET_PASSWORD_REPEAT_PASSWORD_INPUT
import cmm.apps.esmorga.view.password.ResetPasswordScreenTestTags.RESET_PASSWORD_TITLE
import cmm.apps.esmorga.view.profile.HomeScreenTestTags.PROFILE_TITLE
import cmm.apps.esmorga.view.registration.RegistrationConfirmationScreenTestTags.REGISTRATION_CONFIRMATION_OPEN_BUTTON
import cmm.apps.esmorga.view.registration.RegistrationConfirmationScreenTestTags.REGISTRATION_CONFIRMATION_RESEND_BUTTON
import cmm.apps.esmorga.view.registration.RegistrationConfirmationScreenTestTags.REGISTRATION_CONFIRMATION_SHOW_SNACKBAR
import cmm.apps.esmorga.view.registration.RegistrationConfirmationScreenTestTags.REGISTRATION_CONFIRMATION_TITLE
import cmm.apps.esmorga.view.registration.RegistrationScreenTestTags.REGISTRATION_BACK_BUTTON
import cmm.apps.esmorga.view.registration.RegistrationScreenTestTags.REGISTRATION_EMAIL_INPUT
import cmm.apps.esmorga.view.registration.RegistrationScreenTestTags.REGISTRATION_LAST_NAME_INPUT
import cmm.apps.esmorga.view.registration.RegistrationScreenTestTags.REGISTRATION_LOGIN_BUTTON
import cmm.apps.esmorga.view.registration.RegistrationScreenTestTags.REGISTRATION_NAME_INPUT
import cmm.apps.esmorga.view.registration.RegistrationScreenTestTags.REGISTRATION_PASSWORD_INPUT
import cmm.apps.esmorga.view.registration.RegistrationScreenTestTags.REGISTRATION_REPEAT_PASSWORD_INPUT
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

    private val performLoginUseCase = mockk<PerformLoginUseCase>(relaxed = true).also { useCase ->
        coEvery { useCase(any(), any()) } returns EsmorgaResult.success(LoginViewMock.provideUser())
    }

    private val performRegistrationUserCase = mockk<PerformRegistrationUserCase>(relaxed = true).also { useCase ->
        coEvery { useCase(any(), any(), any(), any()) } returns EsmorgaResult.success(Unit)
    }

    private val getSavedUserUseCase = mockk<GetSavedUserUseCase>(relaxed = true).also { useCase ->
        coEvery { useCase() } returns EsmorgaResult.success(LoginViewMock.provideUser())
    }

    private val joinEventUseCase = mockk<JoinEventUseCase>(relaxed = true).also { useCase ->
        coEvery { useCase(any()) } returns EsmorgaResult.success(Unit)
    }

    private val leaveEventUseCase = mockk<LeaveEventUseCase>(relaxed = true).also { useCase ->
        coEvery { useCase(any()) } returns EsmorgaResult.success(Unit)
    }

    private val getMyEventListUseCase = mockk<GetMyEventListUseCase>(relaxed = true).also { useCase ->
        coEvery { useCase() } returns EsmorgaResult.success(EventViewMock.provideEventList(listOf("event")))
    }

    private val performRegistrationConfirmationUseCase = mockk<PerformRegistrationConfirmationUseCase>(relaxed = true).also { useCase ->
        coEvery { useCase(any()) } returns EsmorgaResult.success(Unit)
    }

    private val logOutUseCase = mockk<LogOutUseCase>(relaxed = true).also { useCase ->
        coEvery { useCase() } returns EsmorgaResult.success(false)
    }

    private val performRecoverPasswordUseCase = mockk<PerformRecoverPasswordUseCase>(relaxed = true).also { useCase ->
        coEvery { useCase(any()) } returns EsmorgaResult.success(Unit)
    }

    private val activateAccountUseCase = mockk<ActivateAccountUseCase>(relaxed = true).also { useCase ->
        coEvery { useCase(any()) } returns EsmorgaResult.success(Unit)
    }

    private val performResetPasswordUseCase = mockk<PerformResetPasswordUseCase>(relaxed = true).also { useCase ->
        coEvery { useCase(any(), any()) } returns EsmorgaResult.success(Unit)
    }

    private val getDeviceIdUseCase = mockk<GetDeviceIdUseCase>(relaxed = true).also { useCase ->
        coEvery { useCase() } returns EsmorgaResult.success("device_id")
    }

    private val showDeviceIdIfNeededUseCase = mockk<ShowDeviceIdIfNeededUseCase>(relaxed = true).also { useCase ->
        coEvery { useCase() } returns EsmorgaResult.success(true)
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
                    factory<PerformLoginUseCase> { performLoginUseCase }
                    factory<PerformRegistrationUserCase> { performRegistrationUserCase }
                    factory<GetSavedUserUseCase> { getSavedUserUseCase }
                    factory<JoinEventUseCase> { joinEventUseCase }
                    factory<GetMyEventListUseCase> { getMyEventListUseCase }
                    factory<LeaveEventUseCase> { leaveEventUseCase }
                    factory<PerformRegistrationConfirmationUseCase> { performRegistrationConfirmationUseCase }
                    factory<LogOutUseCase> { logOutUseCase }
                    factory<PerformRecoverPasswordUseCase> { performRecoverPasswordUseCase }
                    factory<ActivateAccountUseCase> { activateAccountUseCase }
                    factory<PerformResetPasswordUseCase> { performResetPasswordUseCase }
                    factory<GetDeviceIdUseCase> { getDeviceIdUseCase }
                    factory<ShowDeviceIdIfNeededUseCase> { showDeviceIdIfNeededUseCase }
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
        setNavigationFromDestination(Navigation.LoginScreen())

        composeTestRule.onNodeWithTag(LOGIN_EMAIL_INPUT).performTextInput("simple@man.com")
        composeTestRule.onNodeWithTag(LOGIN_PASSWORD_INPUT).performTextInput("Test@123")
        composeTestRule.onNodeWithTag(LOGIN_LOGIN_BUTTON).performClick()

        composeTestRule.onNodeWithTag(EVENT_LIST_TITLE).assertIsDisplayed()
    }

    @Test
    fun `given user not logged, when login visited and register is clicked, then register screen is shown`() {
        setNavigationFromDestination(Navigation.LoginScreen())

        composeTestRule.onNodeWithTag(LOGIN_REGISTER_BUTTON).performClick()

        composeTestRule.onNodeWithTag(REGISTRATION_TITLE).assertIsDisplayed()
    }

    @Test
    fun `given user not logged, when login, register and back are clicked, then login screen is shown`() {
        setNavigationFromDestination(Navigation.LoginScreen())

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

        setNavigationFromDestination(Navigation.LoginScreen())

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

        setNavigationFromDestination(Navigation.EventDetailScreen(EventViewMock.provideEvent("EventName")))

        composeTestRule.onNodeWithTag(EVENT_DETAILS_EVENT_NAME).assertIsDisplayed()
        composeTestRule.onNodeWithTag(EVENT_DETAIL_PRIMARY_BUTTON, true).performClick()
        composeTestRule.onNodeWithTag(LOGIN_TITLE).assertIsDisplayed()
    }

    @Test
    fun `given user in event details, when clicks on join event and api call fails, then error screen is shown`() {
        val failureJoinEventUseCase = mockk<JoinEventUseCase>(relaxed = true).also { useCase ->
            coEvery { useCase(any()) } returns EsmorgaResult.failure(Exception())
        }
        loadKoinModules(module { factory<JoinEventUseCase> { failureJoinEventUseCase } })

        setNavigationFromDestination(Navigation.EventDetailScreen(EventViewMock.provideEvent("EventName")))

        composeTestRule.onNodeWithTag(EVENT_DETAILS_EVENT_NAME).assertIsDisplayed()
        composeTestRule.onNodeWithTag(EVENT_DETAIL_PRIMARY_BUTTON).performClick()

        composeTestRule.onNodeWithTag(ERROR_TITLE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(ERROR_RETRY_BUTTON).performClick()

        composeTestRule.onNodeWithTag(EVENT_DETAILS_EVENT_NAME).assertIsDisplayed()
    }

    @Test
    fun `given user in event details, when clicks on join event and no network available, then no network screen is shown`() {
        val failureJoinEventUseCase = mockk<JoinEventUseCase>(relaxed = true).also { useCase ->
            coEvery { useCase(any()) } returns EsmorgaResult.failure(EsmorgaException("No Connection", Source.REMOTE, ErrorCodes.NO_CONNECTION))
        }
        loadKoinModules(module { factory<JoinEventUseCase> { failureJoinEventUseCase } })

        setNavigationFromDestination(Navigation.EventDetailScreen(EventViewMock.provideEvent("Event Name")))

        composeTestRule.onNodeWithTag(EVENT_DETAIL_PRIMARY_BUTTON).performClick()

        composeTestRule.onNodeWithTag(ERROR_ANIMATION).assertIsDisplayed()
        composeTestRule.onNodeWithTag(ERROR_TITLE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(ERROR_SUBTITLE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(ERROR_RETRY_BUTTON).performClick()

        composeTestRule.onNodeWithTag(EVENT_DETAILS_EVENT_NAME).assertIsDisplayed()
    }

    @Test
    fun `given main screen, when clicks on bottom bar my events item, then my events screen is shown`() {
        setNavigationFromDestination(Navigation.MyEventsScreen)
        composeTestRule.onNodeWithTag(MY_EVENT_LIST_TITLE).assertIsDisplayed()
    }

    @Test
    fun `given main screen, when clicks on profile nav item, then profile screen is shown`() {
        setNavigationFromDestination(Navigation.ProfileScreen)
        composeTestRule.onNodeWithTag(PROFILE_TITLE).assertIsDisplayed()
    }

    @Test
    fun `given user not logged, when register visited and register is clicked, then registration confirmation screen is shown`() {
        setNavigationFromDestination(Navigation.RegistrationScreen)

        composeTestRule.onNodeWithTag(REGISTRATION_NAME_INPUT).performTextInput("Name")
        composeTestRule.onNodeWithTag(REGISTRATION_LAST_NAME_INPUT).performTextInput("Last Name")
        composeTestRule.onNodeWithTag(REGISTRATION_EMAIL_INPUT).performTextInput("email@email.com")
        composeTestRule.onNodeWithTag(REGISTRATION_PASSWORD_INPUT).performTextInput("Test@123")
        composeTestRule.onNodeWithTag(REGISTRATION_REPEAT_PASSWORD_INPUT).performTextInput("Test@123")

        composeTestRule.onNodeWithTag(REGISTRATION_LOGIN_BUTTON).performScrollTo()
        composeTestRule.onNodeWithTag(REGISTRATION_LOGIN_BUTTON).performClick()

        composeTestRule.onNodeWithTag(REGISTRATION_CONFIRMATION_TITLE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(REGISTRATION_CONFIRMATION_OPEN_BUTTON).assertIsDisplayed()
        composeTestRule.onNodeWithTag(REGISTRATION_CONFIRMATION_RESEND_BUTTON).assertIsDisplayed()
    }

    @Test
    fun `given user not logged, when register confirmation visited and resend email is clicked, then snackBar is shown`() {
        setNavigationFromDestination(Navigation.RegistrationConfirmationScreen(email = "email@email.com"))
        composeTestRule.onNodeWithTag(REGISTRATION_CONFIRMATION_RESEND_BUTTON).assertIsDisplayed()
        composeTestRule.onNodeWithTag(REGISTRATION_CONFIRMATION_RESEND_BUTTON).performClick()

        composeTestRule.onNodeWithTag(REGISTRATION_CONFIRMATION_SHOW_SNACKBAR).assertIsDisplayed()
    }

    @Test
    fun `given user is on LoginScreen when forgot password button is clicked then RecoverPasswordScreen is displayed`() {
        setNavigationFromDestination(Navigation.LoginScreen())

        composeTestRule.onNodeWithTag(LOGIN_FORGOT_PASSWORD_BUTTON).performClick()

        composeTestRule.onNodeWithTag(RECOVER_PASSWORD_TITLE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(RECOVER_PASSWORD_SEND_EMAIL_BUTTON).assertIsDisplayed()

    }

    @Test
    fun `given user is on RecoverPasswordScreen when back button is clicked then LoginScreen is displayed`() {
        setNavigationFromDestination(Navigation.LoginScreen())

        composeTestRule.onNodeWithTag(LOGIN_FORGOT_PASSWORD_BUTTON).performClick()

        composeTestRule.onNodeWithTag(RECOVER_PASSWORD_TITLE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(RECOVER_PASSWORD_BACK_BUTTON).performClick()

        composeTestRule.onNodeWithTag(LOGIN_TITLE).assertIsDisplayed()
    }

    @Test
    fun `given user is on RecoverPasswordScreen when send button is clicked then Snackbar is displayed`() {
        setNavigationFromDestination(Navigation.RecoverPasswordScreen)
        composeTestRule.onNodeWithTag(RECOVER_PASSWORD_BACK_BUTTON).performClick()

        composeTestRule.onNodeWithTag(RECOVER_PASSWORD_EMAIL_INPUT).performTextInput("email@email.com")
        composeTestRule.onNodeWithTag(RECOVER_PASSWORD_SEND_EMAIL_BUTTON).performClick()
        composeTestRule.onNodeWithTag(RECOVER_PASSWORD_SHOW_SNACKBAR).assertIsDisplayed()
    }

    @Test
    fun `given account not activated, when user clicks the email deeplink, then activation screen is shown`() {
        setNavigationFromDestination(Navigation.ActivateAccountScreen("VerificationCode"))
        composeTestRule.onNodeWithTag(ACTIVATE_ACCOUNT_IMAGE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(ACTIVATE_ACCOUNT_TITLE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(ACTIVATE_ACCOUNT_SUBTITLE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(ACTIVATE_ACCOUNT_BUTTON).assertIsDisplayed()
    }

    @Test
    fun `given account not activated, when activation screen is visited and button clicked, then navigate to EventsScreen`() {
        setNavigationFromDestination(Navigation.ActivateAccountScreen("VerificationCode"))
        composeTestRule.onNodeWithTag(ACTIVATE_ACCOUNT_BUTTON).performClick()
        composeTestRule.onNodeWithTag(EVENT_LIST_TITLE).assertIsDisplayed()
    }

    @Test
    fun `given invalid activation code, when activation attempted, then navigate to FullScreenError`() {
        coEvery { activateAccountUseCase(any()) } returns EsmorgaResult.failure(EsmorgaException("Error", Source.REMOTE, 400))
        setNavigationFromDestination(Navigation.ActivateAccountScreen("VerificationCode"))

        composeTestRule.onNodeWithTag(ERROR_TITLE).assertIsDisplayed()
    }

    @Test
    fun `given reset password screen and success useCase, when user tap on change password, then login screen is shown`() {
        setNavigationFromDestination(Navigation.ResetPasswordScreen("code"))

        composeTestRule.onNodeWithTag(RESET_PASSWORD_TITLE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(RESET_PASSWORD_NEW_PASSWORD_INPUT).performTextInput("Test@123")
        composeTestRule.onNodeWithTag(RESET_PASSWORD_REPEAT_PASSWORD_INPUT).performTextInput("Test@123")
        composeTestRule.onNodeWithTag(RESET_PASSWORD_CHANGE_PASSWORD_BUTTON).assertIsDisplayed()

        composeTestRule.onNodeWithTag(RESET_PASSWORD_CHANGE_PASSWORD_BUTTON).performClick()

        composeTestRule.onNodeWithTag(LOGIN_TITLE).assertIsDisplayed()
    }

    @Test
    fun `given reset password screen and failure useCase, when user tap on change password, then error screen is shown`() {
        coEvery { performResetPasswordUseCase(any(), any()) } returns EsmorgaResult.failure(EsmorgaException("Error", Source.REMOTE, 400))
        setNavigationFromDestination(Navigation.ResetPasswordScreen("code"))

        composeTestRule.onNodeWithTag(RESET_PASSWORD_NEW_PASSWORD_INPUT).performTextInput("Test@123")
        composeTestRule.onNodeWithTag(RESET_PASSWORD_REPEAT_PASSWORD_INPUT).performTextInput("Test@123")
        composeTestRule.onNodeWithTag(RESET_PASSWORD_CHANGE_PASSWORD_BUTTON).performClick()

        composeTestRule.onNodeWithTag(ERROR_TITLE).assertIsDisplayed()
    }


    private fun setNavigationFromAppLaunch(loggedIn: Boolean) {
        composeTestRule.setContent {
            KoinContext {
                navController = rememberNavController()
                EsmorgaNavigationGraph(navigationController = navController, loggedIn = loggedIn, null)
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

