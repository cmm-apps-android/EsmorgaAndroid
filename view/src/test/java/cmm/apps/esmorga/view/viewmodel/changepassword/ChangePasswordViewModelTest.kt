package cmm.apps.esmorga.view.viewmodel.changepassword

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.domain.result.Source
import cmm.apps.esmorga.domain.user.PerformChangePasswordUseCase
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.changepassword.ChangePasswordViewModel
import cmm.apps.esmorga.view.changepassword.model.ChangePasswordEffect
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
class ChangePasswordViewModelTest {
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
    fun `given a successful usecase, when change password is called then navigates to login screen`() = runTest {
        val useCase = mockk<PerformChangePasswordUseCase>(relaxed = true)
        coEvery { useCase.invoke(any(), any()) } returns EsmorgaResult.success(Unit)

        val sut = ChangePasswordViewModel(useCase)

        sut.effect.test {
            sut.onChangePasswordClicked("password", "password1")

            val effect = awaitItem()
            Assert.assertTrue(effect is ChangePasswordEffect.NavigateToLogin)
            Assert.assertEquals(mockContext.getString(R.string.password_set_snackbar), (effect as ChangePasswordEffect.NavigateToLogin).snackbarMessage)
        }
    }

    @Test
    fun `given a failure usecase, when change password is called then full screen error is shown`() = runTest {
        val useCase = mockk<PerformChangePasswordUseCase>(relaxed = true)
        coEvery { useCase.invoke(any(), any()) } returns EsmorgaResult.failure(EsmorgaException("Error", Source.REMOTE, 400))

        val sut = ChangePasswordViewModel(useCase)

        sut.effect.test {
            sut.onChangePasswordClicked("password", "password1")

            val effect = awaitItem()
            Assert.assertTrue(effect is ChangePasswordEffect.ShowFullScreenError)

        }
    }

    @Test
    fun `given no internet connection when change password is called then no connection snackbar shown`() = runTest {
        val useCase = mockk<PerformChangePasswordUseCase>(relaxed = true)
        coEvery { useCase.invoke(any(), any()) } returns EsmorgaResult.failure(EsmorgaException("Reset Password Error", Source.REMOTE, ErrorCodes.NO_CONNECTION))

        val sut = ChangePasswordViewModel(useCase)

        sut.effect.test {
            sut.onChangePasswordClicked("password", "password1")

            val effect = awaitItem()
            Assert.assertTrue(effect is ChangePasswordEffect.ShowNoConnectionSnackbar)
        }
    }

    @Test
    fun `given change password screen when fields are empty then empty field error is shown`() = runTest {
        val pass = ""
        val newPass = ""
        val repeatPass = ""
        val useCase = mockk<PerformChangePasswordUseCase>(relaxed = true)
        coEvery { useCase.invoke(any(), any()) } returns EsmorgaResult.success(Unit)

        val sut = ChangePasswordViewModel(useCase)

        sut.validateField(ChangePasswordViewModel.ChangePasswordField.PASS, pass, null, null, hasFocused = true)
        Assert.assertNotNull(sut.uiState.value.currentPasswordError)
        sut.validateField(ChangePasswordViewModel.ChangePasswordField.NEW_PASS, pass, newPass, null, hasFocused = true)
        Assert.assertNotNull(sut.uiState.value.newPasswordError)
        sut.validateField(ChangePasswordViewModel.ChangePasswordField.REPEAT_NEW_PASS, pass, newPass, repeatPass, hasFocused = true)
        Assert.assertNotNull(sut.uiState.value.repeatPasswordError)

        val state = sut.uiState.value

        Assert.assertEquals(mockContext.getString(R.string.inline_error_empty_field), state.currentPasswordError)
        Assert.assertEquals(mockContext.getString(R.string.inline_error_empty_field), state.newPasswordError)
        Assert.assertEquals(mockContext.getString(R.string.inline_error_empty_field), state.repeatPasswordError)

        Assert.assertFalse(sut.uiState.value.enableButton(pass, newPass, repeatPass))
    }

    @Test
    fun `given change password screen when fields has no valid passwords then invalid password error is shown`() = runTest {
        val pass = "pass"
        val newPass = "pass1"
        val repeatPass = "pass1"
        val useCase = mockk<PerformChangePasswordUseCase>(relaxed = true)
        coEvery { useCase.invoke(any(), any()) } returns EsmorgaResult.success(Unit)

        val sut = ChangePasswordViewModel(useCase)

        sut.validateField(ChangePasswordViewModel.ChangePasswordField.PASS, pass, null, null, hasFocused = true)
        Assert.assertNotNull(sut.uiState.value.currentPasswordError)
        sut.validateField(ChangePasswordViewModel.ChangePasswordField.NEW_PASS, pass, newPass, null, hasFocused = true)
        Assert.assertNotNull(sut.uiState.value.newPasswordError)
        sut.validateField(ChangePasswordViewModel.ChangePasswordField.REPEAT_NEW_PASS, pass, newPass, repeatPass, hasFocused = true)
        Assert.assertNotNull(sut.uiState.value.repeatPasswordError)

        val state = sut.uiState.value

        Assert.assertEquals(mockContext.getString(R.string.inline_error_password), state.currentPasswordError)
        Assert.assertEquals(mockContext.getString(R.string.inline_error_password), state.newPasswordError)
        Assert.assertEquals(mockContext.getString(R.string.inline_error_password), state.repeatPasswordError)

        Assert.assertFalse(sut.uiState.value.enableButton(pass, newPass, repeatPass))
    }

    @Test
    fun `given change password screen when new passwords are the same as current password then reused password error is shown`() = runTest {
        val pass = "Password1!"
        val newPass = "Password1!"
        val repeatPass = "Password1!"
        val useCase = mockk<PerformChangePasswordUseCase>(relaxed = true)
        coEvery { useCase.invoke(any(), any()) } returns EsmorgaResult.success(Unit)

        val sut = ChangePasswordViewModel(useCase)

        sut.validateField(ChangePasswordViewModel.ChangePasswordField.PASS, pass, null, null, hasFocused = true)
        sut.validateField(ChangePasswordViewModel.ChangePasswordField.NEW_PASS, pass, newPass, null, hasFocused = true)
        sut.validateField(ChangePasswordViewModel.ChangePasswordField.REPEAT_NEW_PASS, pass, newPass, repeatPass, hasFocused = true)
        Assert.assertNotNull(sut.uiState.value.repeatPasswordError)

        val state = sut.uiState.value

        Assert.assertEquals(mockContext.getString(R.string.registration_reused_password_error), state.repeatPasswordError)

        Assert.assertFalse(sut.uiState.value.enableButton(pass, newPass, repeatPass))
    }

    @Test
    fun `given change password screen when new passwords are different then password mismatch error is shown`() = runTest {
        val pass = "Password1!"
        val newPass = "Password2!"
        val repeatPass = "Password3!"
        val useCase = mockk<PerformChangePasswordUseCase>(relaxed = true)
        coEvery { useCase.invoke(any(), any()) } returns EsmorgaResult.success(Unit)

        val sut = ChangePasswordViewModel(useCase)

        sut.validateField(ChangePasswordViewModel.ChangePasswordField.PASS, pass, null, null, hasFocused = true)
        sut.validateField(ChangePasswordViewModel.ChangePasswordField.NEW_PASS, pass, newPass, null, hasFocused = true)
        sut.validateField(ChangePasswordViewModel.ChangePasswordField.REPEAT_NEW_PASS, pass, newPass, repeatPass, hasFocused = true)
        Assert.assertNotNull(sut.uiState.value.repeatPasswordError)

        val state = sut.uiState.value

        Assert.assertEquals(mockContext.getString(R.string.registration_password_mismatch_error), state.repeatPasswordError)

        Assert.assertFalse(sut.uiState.value.enableButton(pass, newPass, repeatPass))
    }
}