package cmm.apps.esmorga.view.viewmodel.registration

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import cmm.apps.esmorga.domain.result.ErrorCodes.NO_CONNECTION
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.Source
import cmm.apps.esmorga.domain.result.Success
import cmm.apps.esmorga.domain.user.PerformRegistrationUserCase
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.registration.RegistrationField
import cmm.apps.esmorga.view.registration.RegistrationViewModel
import cmm.apps.esmorga.view.registration.model.RegistrationEffect
import cmm.apps.esmorga.view.viewmodel.mock.LoginViewMock
import cmm.apps.esmorga.view.viewmodel.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RegistrationViewModelTest : KoinTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var mockContext: Context

    @Before
    fun init() {
        mockContext = ApplicationProvider.getApplicationContext()
        startKoin {
            androidContext(mockContext)
        }
    }

    @After
    fun shutDown() {
        stopKoin()
    }

    @Test
    fun `given a successful usecase when register method is called then usecase executed and UI effect for successful register is emitted`() = runTest {
        val user = LoginViewMock.provideUser()
        val useCase = mockk<PerformRegistrationUserCase>(relaxed = true)
        coEvery { useCase(any(), any(), any(), any()) } returns Result.success(Success(user))

        val sut = RegistrationViewModel(useCase)

        sut.effect.test {
            sut.onRegisterClicked(user.name, user.lastName, user.email, "Test@123", "Test@123")

            val effect = awaitItem()
            Assert.assertTrue(effect is RegistrationEffect.NavigateToEventList)
        }
    }

    @Test
    fun `given a failure usecase when register method is called then usecase executed and UI effect for error is emitted`() = runTest {
        val user = LoginViewMock.provideUser()
        val useCase = mockk<PerformRegistrationUserCase>(relaxed = true)
        coEvery { useCase(any(), any(), any(), any()) } returns Result.failure(EsmorgaException("Fake error", Source.REMOTE, 500))

        val sut = RegistrationViewModel(useCase)

        sut.effect.test {
            sut.onRegisterClicked(user.name, user.lastName, user.email, "Test@123", "Test@123")

            val effect = awaitItem()
            Assert.assertTrue(effect is RegistrationEffect.ShowFullScreenError)
        }
    }

    @Test
    fun `given no internet connection when register method is called then usecase executed and UI effect for snackbar is emitted`() = runTest {
        val user = LoginViewMock.provideUser()
        val useCase = mockk<PerformRegistrationUserCase>(relaxed = true)
        coEvery { useCase(any(), any(), any(), any()) } returns Result.failure(EsmorgaException("Fake No Internet error", Source.REMOTE, NO_CONNECTION))

        val sut = RegistrationViewModel(useCase)

        sut.effect.test {
            sut.onRegisterClicked(user.name, user.lastName, user.email, "Test@123", "Test@123")

            val effect = awaitItem()
            Assert.assertTrue(effect is RegistrationEffect.ShowNoNetworkSnackbar)
        }
    }

    @Test
    fun `given invalid fields inputted when register method is called then ui shows errors in all fields`() = runTest {
        val useCase = mockk<PerformRegistrationUserCase>(relaxed = true)
        coEvery { useCase(any(), any(), any(), any()) } returns Result.success(Success(LoginViewMock.provideUser()))

        val sut = RegistrationViewModel(useCase)
        sut.onRegisterClicked("", "", "", "", "")

        val state = sut.uiState.value
        Assert.assertEquals(mockContext.getString(R.string.inline_error_empty_field), state.nameError)
        Assert.assertEquals(mockContext.getString(R.string.inline_error_empty_field), state.lastNameError)
        Assert.assertEquals(mockContext.getString(R.string.inline_error_empty_field), state.emailError)
        Assert.assertEquals(mockContext.getString(R.string.inline_error_empty_field), state.passError)
        Assert.assertEquals(mockContext.getString(R.string.inline_error_empty_field), state.repeatPassError)
    }

    @Test
    fun `given an invalid name inputted when register method is called then ui shows name error`() = runTest {
        val user = LoginViewMock.provideUser()
        val useCase = mockk<PerformRegistrationUserCase>(relaxed = true)
        coEvery { useCase(any(), any(), any(), any()) } returns Result.success(Success(user))

        val invalidNameList = listOf(
            "a",
            "name!!!"
        )

        val sut = RegistrationViewModel(useCase)

        for (invalidName in invalidNameList) {
            sut.onRegisterClicked(invalidName, user.lastName, user.email, "Test@123", "Test@123")
            val state = sut.uiState.value
            Assert.assertEquals("$invalidName did not result in an error", mockContext.getString(R.string.inline_error_name), state.nameError)
        }
    }

    @Test
    fun `given an invalid last name inputted when register method is called then ui shows last name error`() = runTest {
        val user = LoginViewMock.provideUser()
        val useCase = mockk<PerformRegistrationUserCase>(relaxed = true)
        coEvery { useCase(any(), any(), any(), any()) } returns Result.success(Success(user))

        val invalidLastNameList = listOf(
            "a",
            "name!!!"
        )

        val sut = RegistrationViewModel(useCase)

        for (invalidLastName in invalidLastNameList) {
            sut.onRegisterClicked(user.name, invalidLastName, user.email, "Test@123", "Test@123")
            val state = sut.uiState.value
            Assert.assertEquals("$invalidLastName did not result in an error", mockContext.getString(R.string.inline_error_last_name), state.lastNameError)
        }
    }

    @Test
    fun `given an invalid email inputted when register method is called then ui shows email error`() = runTest {
        val user = LoginViewMock.provideUser()
        val useCase = mockk<PerformRegistrationUserCase>(relaxed = true)
        coEvery { useCase(any(), any(), any(), any()) } returns Result.success(Success(user))

        val invalidEmailList = listOf(
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@example.com",
            "space@domain .com",
            "invalidemail",
            "user+alias@domain.com",
            "invalid@domain,com",
            "invalid@domain.c"
        )

        val sut = RegistrationViewModel(useCase)

        for (invalidEmail in invalidEmailList) {
            sut.onRegisterClicked(user.name, user.lastName, invalidEmail, "Test@123", "Test@123")
            val state = sut.uiState.value
            Assert.assertEquals("$invalidEmail did not result in an error", mockContext.getString(R.string.inline_error_email), state.emailError)
        }
    }

    @Test
    fun `given an invalid password inputted when register method is called then ui shows password error`() = runTest {
        val user = LoginViewMock.provideUser()
        val useCase = mockk<PerformRegistrationUserCase>(relaxed = true)
        coEvery { useCase(any(), any(), any(), any()) } returns Result.success(Success(user))

        val invalidPasswordList = listOf(
            "12ab@",
            "@abcdefg",
            "1234abcd",
            "1234567@"
        )

        val sut = RegistrationViewModel(useCase)

        for (invalidPass in invalidPasswordList) {
            sut.onRegisterClicked(user.name, user.lastName, user.email, invalidPass, invalidPass)
            val state = sut.uiState.value
            Assert.assertEquals("$invalidPass did not result in an error", mockContext.getString(R.string.inline_error_password_invalid), state.passError)
        }
    }

    @Test
    fun `given an invalid repeated password inputted when register method is called then ui shows repeated password error`() = runTest {
        val user = LoginViewMock.provideUser()
        val useCase = mockk<PerformRegistrationUserCase>(relaxed = true)
        coEvery { useCase(any(), any(), any(), any()) } returns Result.success(Success(user))

        val sut = RegistrationViewModel(useCase)
        sut.onRegisterClicked(user.name, user.lastName, user.email, "Password01!", "Password02!")

        val state = sut.uiState.value
        Assert.assertEquals(mockContext.getString(R.string.inline_error_password_mismatch), state.repeatPassError)
    }

    @Test
    fun `given a ui state with a name error when name text changes error is hidden`() = runTest {
        val useCase = mockk<PerformRegistrationUserCase>(relaxed = true)
        val sut = RegistrationViewModel(useCase)
        sut.validateField(RegistrationField.NAME, "wrongname!")
        Assert.assertFalse(sut.uiState.value.nameError.isNullOrEmpty())

        sut.onFieldChanged(RegistrationField.NAME)
        Assert.assertTrue(sut.uiState.value.nameError.isNullOrEmpty())
    }

    @Test
    fun `given a ui state with a last name error when last name text changes error is hidden`() = runTest {
        val useCase = mockk<PerformRegistrationUserCase>(relaxed = true)
        val sut = RegistrationViewModel(useCase)
        sut.validateField(RegistrationField.LAST_NAME, "wrongLastName!")
        Assert.assertFalse(sut.uiState.value.lastNameError.isNullOrEmpty())

        sut.onFieldChanged(RegistrationField.LAST_NAME)
        Assert.assertTrue(sut.uiState.value.lastNameError.isNullOrEmpty())
    }

    @Test
    fun `given a ui state with a email error when email text changes error is hidden`() = runTest {
        val useCase = mockk<PerformRegistrationUserCase>(relaxed = true)
        val sut = RegistrationViewModel(useCase)
        sut.validateField(RegistrationField.EMAIL, "wrongemail")
        Assert.assertFalse(sut.uiState.value.emailError.isNullOrEmpty())

        sut.onFieldChanged(RegistrationField.EMAIL)
        Assert.assertTrue(sut.uiState.value.emailError.isNullOrEmpty())
    }

    @Test
    fun `given a ui state with a password error when password text changes error is hidden`() = runTest {
        val useCase = mockk<PerformRegistrationUserCase>(relaxed = true)
        val sut = RegistrationViewModel(useCase)
        sut.validateField(RegistrationField.PASS, "wrongpass")
        Assert.assertFalse(sut.uiState.value.passError.isNullOrEmpty())

        sut.onFieldChanged(RegistrationField.PASS)
        Assert.assertTrue(sut.uiState.value.passError.isNullOrEmpty())
    }

    @Test
    fun `given a ui state with a repeated password error when repeated password text changes error is hidden`() = runTest {
        val useCase = mockk<PerformRegistrationUserCase>(relaxed = true)
        val sut = RegistrationViewModel(useCase)
        sut.validateField(RegistrationField.REPEAT_PASS, "wrongpass", "normalPass")
        Assert.assertFalse(sut.uiState.value.repeatPassError.isNullOrEmpty())

        sut.onFieldChanged(RegistrationField.REPEAT_PASS)
        Assert.assertTrue(sut.uiState.value.repeatPassError.isNullOrEmpty())
    }

}