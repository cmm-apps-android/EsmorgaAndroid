package cmm.apps.esmorga.view.viewmodel.resetPassword

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.domain.result.Source
import cmm.apps.esmorga.domain.user.repository.PerformResetPasswordUseCase
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.password.ResetPasswordViewModel
import cmm.apps.esmorga.view.password.model.ResetPasswordEffect
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
class ResetPasswordViewModelTest {

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
    fun `given a successful usecase, when reset password method is called usecase executed and UI effect for successful reset password is emitted`() = runTest {
        val useCase = mockk<PerformResetPasswordUseCase>(relaxed = true)
        coEvery { useCase.invoke(any(), any()) } returns EsmorgaResult.success(Unit)

        val sut = ResetPasswordViewModel(useCase)

        sut.effect.test {
            sut.onResetPasswordClicked("347638", "password")

            val effect = awaitItem()
            Assert.assertTrue(effect is ResetPasswordEffect.NavigateToLogin)
        }
    }

    @Test
    fun `given a failure usecase, when reset password method is called usecase executed and UI effect for reset password error is emitted`() = runTest {
        val useCase = mockk<PerformResetPasswordUseCase>(relaxed = true)
        coEvery { useCase.invoke(any(), any()) } returns EsmorgaResult.failure(EsmorgaException("Reset Password Error", Source.REMOTE, 40))

        val sut = ResetPasswordViewModel(useCase)

        sut.effect.test {
            sut.onResetPasswordClicked("347638", "password")

            val effect = awaitItem()
            Assert.assertTrue(effect is ResetPasswordEffect.ShowFullScreenError)

        }
    }

    @Test
    fun `given no internet connection when login method is called usecase executed and UI effect for no connection snackbar is emitted`() = runTest {
        val useCase = mockk<PerformResetPasswordUseCase>(relaxed = true)
        coEvery { useCase.invoke(any(), any()) } returns EsmorgaResult.failure(EsmorgaException("Reset Password Error", Source.REMOTE, ErrorCodes.NO_CONNECTION))

        val sut = ResetPasswordViewModel(useCase)

        sut.effect.test {
            sut.onResetPasswordClicked("347638", "password")

            val effect = awaitItem()
            Assert.assertTrue(effect is ResetPasswordEffect.ShowNoConnectionSnackbar)
        }
    }

    @Test
    fun `given an empty fields when check enable button method is called then return false`() = runTest {
        val pass = ""
        val repeatPass = ""
        val useCase = mockk<PerformResetPasswordUseCase>(relaxed = true)
        coEvery { useCase.invoke(any(), any()) } returns EsmorgaResult.success(Unit)

        val sut = ResetPasswordViewModel(useCase)

        sut.validateField(ResetPasswordViewModel.ResetPasswordField.PASS, pass)
        Assert.assertNull(sut.uiState.value.passwordError)
        sut.validateField(ResetPasswordViewModel.ResetPasswordField.REPEAT_PASS, repeatPass)
        Assert.assertNull(sut.uiState.value.repeatPasswordError)

        sut.onValueChange(pass, repeatPass)
        val state = sut.uiState.value

        Assert.assertFalse(state.enableButton(pass, repeatPass))
        Assert.assertEquals(mockContext.getString(R.string.inline_error_empty_field), state.passwordError)
        Assert.assertEquals(mockContext.getString(R.string.inline_error_empty_field), state.repeatPasswordError)
    }

    @Test
    fun `given a valid fields when check enable button method is called then return true`() = runTest {
        val pass = "Test@123"
        val repeatPass = "Test@123"
        val useCase = mockk<PerformResetPasswordUseCase>(relaxed = true)
        coEvery { useCase.invoke(any(), any()) } returns EsmorgaResult.success(Unit)

        val sut = ResetPasswordViewModel(useCase)
        sut.validateField(ResetPasswordViewModel.ResetPasswordField.PASS, pass)
        Assert.assertNull(sut.uiState.value.passwordError)

        sut.onValueChange(pass, repeatPass)

        val state = sut.uiState.value
        Assert.assertTrue(state.enableButton(pass, repeatPass))
        Assert.assertNull(state.passwordError)
        Assert.assertNull(state.repeatPasswordError)
    }

    @Test
    fun `given an invalid passwords, when on value changed, then ui shows error`() = runTest {
        val pass = "password"
        val repeatPass = "password"
        val useCase = mockk<PerformResetPasswordUseCase>(relaxed = true)
        coEvery { useCase.invoke(any(), any()) } returns EsmorgaResult.success(Unit)

        val sut = ResetPasswordViewModel(useCase)
        sut.validateField(ResetPasswordViewModel.ResetPasswordField.PASS, pass)
        Assert.assertEquals(mockContext.getString(R.string.inline_error_password_invalid), sut.uiState.value.passwordError)

        sut.onValueChange(pass, repeatPass)

        val state = sut.uiState.value
        Assert.assertFalse(state.enableButton(pass, repeatPass))
        Assert.assertEquals(mockContext.getString(R.string.inline_error_password_invalid), state.passwordError)
    }

    @Test
    fun `given different passwords, when on value changed, then ui shows error`() = runTest {
        val pass = "Test@123"
        val repeatPass = "password"
        val useCase = mockk<PerformResetPasswordUseCase>(relaxed = true)
        coEvery { useCase.invoke(any(), any()) } returns EsmorgaResult.success(Unit)

        val sut = ResetPasswordViewModel(useCase)
        sut.validateField(ResetPasswordViewModel.ResetPasswordField.REPEAT_PASS, repeatPass)
        Assert.assertEquals(mockContext.getString(R.string.inline_error_password_mismatch), sut.uiState.value.repeatPasswordError)

        sut.onValueChange(pass, repeatPass)

        val state = sut.uiState.value
        Assert.assertFalse(state.enableButton(pass, repeatPass))
        Assert.assertEquals(mockContext.getString(R.string.inline_error_password_mismatch), state.repeatPasswordError)
    }
}