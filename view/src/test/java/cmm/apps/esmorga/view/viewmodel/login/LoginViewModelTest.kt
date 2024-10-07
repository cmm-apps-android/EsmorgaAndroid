package cmm.apps.esmorga.view.viewmodel.login

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.domain.result.Source
import cmm.apps.esmorga.domain.user.PerformLoginUseCase
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.login.LoginViewModel
import cmm.apps.esmorga.view.login.model.LoginEffect
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

@RunWith(AndroidJUnit4::class)
class LoginViewModelTest {

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
    fun `given a successful usecase when login method is called usecase executed and UI effect for successful login is emitted`() = runTest {
        val user = LoginViewMock.provideUser()
        val useCase = mockk<PerformLoginUseCase>(relaxed = true)
        coEvery { useCase(any(), any()) } returns EsmorgaResult.success(user)

        val sut = LoginViewModel(useCase)

        sut.effect.test {
            sut.onLoginClicked(user.email, "Test@123")

            val effect = awaitItem()
            Assert.assertTrue(effect is LoginEffect.NavigateToEventList)
        }
    }

    @Test
    fun `given a failure usecase when login method is called usecase executed and UI effect for error is emitted`() = runTest {
        val user = LoginViewMock.provideUser()
        val useCase = mockk<PerformLoginUseCase>(relaxed = true)
        coEvery { useCase(any(), any()) } returns EsmorgaResult.failure(EsmorgaException("Mock Login Error", Source.REMOTE, 401))

        val sut = LoginViewModel(useCase)

        sut.effect.test {
            sut.onLoginClicked(user.email, "Test@123")

            val effect = awaitItem()
            Assert.assertTrue(effect is LoginEffect.ShowFullScreenError)
        }
    }

    @Test
    fun `given no internet connection when login method is called usecase executed and UI effect for no connection is emitted`() = runTest {
        val user = LoginViewMock.provideUser()
        val useCase = mockk<PerformLoginUseCase>(relaxed = true)
        coEvery { useCase(any(), any()) } returns EsmorgaResult.failure(EsmorgaException("Mock Login Error", Source.REMOTE, ErrorCodes.NO_CONNECTION))

        val sut = LoginViewModel(useCase)

        sut.effect.test {
            sut.onLoginClicked(user.email, "Test@123")

            val effect = awaitItem()
            Assert.assertTrue(effect is LoginEffect.ShowNoNetworkSnackbar)
        }
    }

    @Test
    fun `given invalid fields inputted when login method is called then ui shows errors in all fields`() = runTest {
        val useCase = mockk<PerformLoginUseCase>(relaxed = true)
        coEvery { useCase(any(), any()) } returns EsmorgaResult.success(LoginViewMock.provideUser())

        val sut = LoginViewModel(useCase)
        sut.onLoginClicked("", "")

        val state = sut.uiState.value
        Assert.assertEquals(mockContext.getString(R.string.inline_error_empty_field), state.emailError)
        Assert.assertEquals(mockContext.getString(R.string.inline_error_empty_field), state.passwordError)
    }

    @Test
    fun `given an invalid email inputted when login method is called then ui shows email error`() = runTest {
        val user = LoginViewMock.provideUser()
        val useCase = mockk<PerformLoginUseCase>(relaxed = true)
        coEvery { useCase(any(), any()) } returns EsmorgaResult.success(user)

        val invalidEmailList = listOf(
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@example.com",
            "space@domain .com",
            "invalidemail",
            "user+alias@domain.com",
            "invalid@domain,com",
            "invalid@domain.c"
        )

        val sut = LoginViewModel(useCase)

        for (invalidEmail in invalidEmailList) {
            sut.onLoginClicked(invalidEmail, "Test@123")
            val state = sut.uiState.value
            Assert.assertEquals("$invalidEmail did not result in an error", mockContext.getString(R.string.inline_error_email), state.emailError)
        }
    }

    @Test
    fun `given an invalid password inputted when login method is called then ui shows password error`() = runTest {
        val user = LoginViewMock.provideUser()
        val useCase = mockk<PerformLoginUseCase>(relaxed = true)
        coEvery { useCase(any(), any()) } returns EsmorgaResult.success(user)

        val invalidPasswordList = listOf(
            "12ab@",
            "@abcdefg",
            "1234abcd",
            "1234567@"
        )

        val sut = LoginViewModel(useCase)

        for (invalidPass in invalidPasswordList) {
            sut.onLoginClicked(user.email, invalidPass)
            val state = sut.uiState.value
            Assert.assertEquals("$invalidPass did not result in an error", mockContext.getString(R.string.inline_error_password), state.passwordError)
        }
    }

    @Test
    fun `given a ui state with a email error when email text changes error is hidden`() = runTest {
        val useCase = mockk<PerformLoginUseCase>(relaxed = true)
        val sut = LoginViewModel(useCase)
        sut.validateEmail("wrongEmail")
        Assert.assertFalse(sut.uiState.value.emailError.isNullOrEmpty())

        sut.onEmailChanged()
        Assert.assertTrue(sut.uiState.value.emailError.isNullOrEmpty())
    }

    @Test
    fun `given a ui state with a password error when password text changes error is hidden`() = runTest {
        val useCase = mockk<PerformLoginUseCase>(relaxed = true)
        val sut = LoginViewModel(useCase)
        sut.validatePass("wrongpass")
        Assert.assertFalse(sut.uiState.value.passwordError.isNullOrEmpty())

        sut.onPassChanged()
        Assert.assertTrue(sut.uiState.value.passwordError.isNullOrEmpty())
    }

    @Test
    fun `given email and password are correctly filled when login clicked then ui shows loading state`() = runTest {
        val user = LoginViewMock.provideUser()
        val useCase = mockk<PerformLoginUseCase>(relaxed = true)
        coEvery { useCase(any(), any()) } returns EsmorgaResult.success(user)

        val sut = LoginViewModel(useCase)

        sut.onLoginClicked(user.email, "Test@123")
        val state = sut.uiState.value
        Assert.assertTrue(state.loading)
    }
}