package cmm.apps.esmorga.view.viewmodel.registration

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.domain.result.Source
import cmm.apps.esmorga.domain.user.PerformRegistrationConfirmationUseCase
import cmm.apps.esmorga.view.registration.RegistrationConfirmationViewModel
import cmm.apps.esmorga.view.registration.model.RegistrationConfirmationEffect
import cmm.apps.esmorga.view.viewmodel.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest

@RunWith(AndroidJUnit4::class)
class RegistrationConfirmationViewModelTest : KoinTest {

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
    fun `given a successful useCase when resend email method is called then useCase executed and UI effect for successful register is emitted`() = runTest {
        val useCase = mockk<PerformRegistrationConfirmationUseCase>(relaxed = true)
        coEvery { useCase(any()) } returns EsmorgaResult.success(Unit)

        val sut = RegistrationConfirmationViewModel(useCase)

        sut.effect.test {
            sut.onResendEmailClicked("email@email.com")

            val effect = awaitItem()
            assertTrue(effect is RegistrationConfirmationEffect.ShowSnackbarSuccess)
        }
    }

    @Test
    fun `given a failure useCase when resend email method is called then useCase executed and UI effect for error is emitted`() = runTest {
        val useCase = mockk<PerformRegistrationConfirmationUseCase>(relaxed = true)
        coEvery { useCase(any()) } returns EsmorgaResult.failure(
            EsmorgaException("Fake error", Source.REMOTE, 500)
        )

        val sut = RegistrationConfirmationViewModel(useCase)

        sut.effect.test {
            sut.onResendEmailClicked("email@email.com")

            val effect = awaitItem()
            assertTrue(effect is RegistrationConfirmationEffect.ShowSnackbarFailure)
        }
    }

    @Test
    fun `given a viewModel when open email button is clicked then UI effect for navigate is emitted`() = runTest {
        val useCase = mockk<PerformRegistrationConfirmationUseCase>(relaxed = true)
        coEvery { useCase(any()) } returns EsmorgaResult.success(Unit)

        val sut = RegistrationConfirmationViewModel(useCase)

        sut.effect.test {
            sut.onNavigateEmailApp()

            val effect = awaitItem()
            assertTrue(effect is RegistrationConfirmationEffect.NavigateToEmailApp)
        }
    }
}