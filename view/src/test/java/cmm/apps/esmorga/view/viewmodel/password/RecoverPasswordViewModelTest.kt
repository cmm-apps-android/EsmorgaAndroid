package cmm.apps.esmorga.view.viewmodel.password

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.domain.user.PerformRecoverPasswordUseCase
import cmm.apps.esmorga.view.password.RecoverPasswordViewModel
import cmm.apps.esmorga.view.password.model.RecoverPasswordEffect
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
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class RecoverPasswordViewModelTest {

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
    fun `given invalid email format when validateEmail is called then emailError is not null`() = runTest {
        val useCase = mockk<PerformRecoverPasswordUseCase>(relaxed = true)
        val sut = RecoverPasswordViewModel(useCase)
        val emailError = "wrongEmail"
        sut.validateEmail(emailError, false)
        Assert.assertFalse(sut.uiState.value.emailError.isNullOrEmpty())
    }

    @Test
    fun `given empty email when validateEmail is called then emailError is not null`() = runTest {
        val useCase = mockk<PerformRecoverPasswordUseCase>(relaxed = true)
        val sut = RecoverPasswordViewModel(useCase)
        val emailError = ""
        sut.validateEmail(emailError, false)
        Assert.assertFalse(sut.uiState.value.emailError.isNullOrEmpty())
    }

    @Test
    fun `given valid email when validateEmail is called then emailError is null`() = runTest {
        val useCase = mockk<PerformRecoverPasswordUseCase>(relaxed = true)
        val sut = RecoverPasswordViewModel(useCase)
        val emailError = "test@example.com"
        sut.validateEmail(emailError, false)
        Assert.assertTrue(sut.uiState.value.emailError.isNullOrEmpty())
    }

    @Test
    fun `given valid email and use case success when onResendEmailClicked then ShowSnackbarSuccess effect is emitted`() = runTest {
        val useCase = mockk<PerformRecoverPasswordUseCase>(relaxed = true)
        val sut = RecoverPasswordViewModel(useCase)
        val validEmail = "test@example.com"

        coEvery { useCase(validEmail.trim()) } returns EsmorgaResult.success(Unit)

        sut.effect.test {
            sut.onResendEmailClicked(validEmail)
            assertNull(sut.uiState.value.emailError)
            assertFalse(sut.uiState.value.hasAnyError())

            val effect = awaitItem()
            Assert.assertTrue(effect is RecoverPasswordEffect.ShowSnackbarSuccess)
        }
    }

    @Test
    fun `given valid email and use case failure when onResendEmailClicked then ShowFullScreenError effect is emitted`() = runTest {
        val useCase = mockk<PerformRecoverPasswordUseCase>(relaxed = true)
        val sut = RecoverPasswordViewModel(useCase)
        val validEmail = "test@example.com"

        coEvery { useCase(validEmail.trim()) } returns EsmorgaResult.failure(Exception("Mock Error"))

        sut.effect.test {
            sut.onResendEmailClicked(validEmail)
            assertNull(sut.uiState.value.emailError)
            assertFalse(sut.uiState.value.hasAnyError())

            val effect = awaitItem()
            Assert.assertTrue(effect is RecoverPasswordEffect.ShowFullScreenError)
        }
    }

    @Test
    fun `given invalid email when onResendEmailClicked then use case is not called and no success or error effect emitted`() = runTest {
        val useCase = mockk<PerformRecoverPasswordUseCase>(relaxed = true)
        val sut = RecoverPasswordViewModel(useCase)
        val validEmail = "test"

        sut.effect.test {
            sut.onResendEmailClicked(validEmail)
            assertNotNull(sut.uiState.value.emailError)
            assertTrue(sut.uiState.value.hasAnyError())
        }
    }
}