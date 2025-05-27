package cmm.apps.esmorga.view.viewmodel.activateAccount

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import cmm.apps.esmorga.domain.account.ActivateAccountUseCase
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.domain.result.Source
import cmm.apps.esmorga.view.activateaccount.ActivateAccountViewModel
import cmm.apps.esmorga.view.activateaccount.model.ActivateAccountEffect
import cmm.apps.esmorga.view.viewmodel.util.MainDispatcherRule
import cmm.apps.esmorga.view.R
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
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
class ActivateAccountViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val lifeCycleOwner = mockk<LifecycleOwner>()
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
    fun `given valid verification code when activateAccount is called then UI state is not loading`() = runTest {
        val verificationCode = "validCode"
        val activateAccountUseCase = mockk<ActivateAccountUseCase>()
        coEvery { activateAccountUseCase(verificationCode) } returns EsmorgaResult.success(Unit)

        val sut = ActivateAccountViewModel(verificationCode, activateAccountUseCase)
        sut.onStart(lifeCycleOwner)

        val state = sut.uiState.value
        Assert.assertFalse(state.isLoading)
    }

    @Test
    fun `given invalid verification code when activateAccount is called then full screen error is shown`() = runTest {
        val verificationCode = "invalidCode"
        val activateAccountUseCase = mockk<ActivateAccountUseCase>(relaxed = true)

        coEvery {
            activateAccountUseCase(verificationCode)
        } returns EsmorgaResult.failure(EsmorgaException(message = "error", source = Source.REMOTE, code = 400))

        val sut = ActivateAccountViewModel(verificationCode, activateAccountUseCase)


        sut.effect.test {
            sut.onStart(lifeCycleOwner)
            val effect = awaitItem()
            Assert.assertTrue(effect is ActivateAccountEffect.ShowFullScreenError)
            assertEquals(mockContext.getString(R.string.register_confirmation_error_title), (effect as ActivateAccountEffect.ShowFullScreenError).esmorgaErrorScreenArguments.title)
            assertEquals(mockContext.getString(R.string.register_confirmation_button_retry), effect.esmorgaErrorScreenArguments.buttonText)
        }
    }


    @Test
    fun `given multiple failed attempts when activateAccount is called then last try full screen error is shown`() = runTest {
        val verificationCode = "expiredCode"
        val activateAccountUseCase = mockk<ActivateAccountUseCase>()
        coEvery {
            activateAccountUseCase(verificationCode)
        } returns EsmorgaResult(error = EsmorgaException("Expired code", Source.REMOTE, ErrorCodes.NO_DATA))

        val sut = ActivateAccountViewModel(verificationCode, activateAccountUseCase)

        sut.effect.test {
            sut.onStart(lifeCycleOwner)
            awaitItem()

            sut.onStart(lifeCycleOwner)
            awaitItem()

            sut.onStart(lifeCycleOwner)
            val effect = awaitItem()

            Assert.assertTrue(effect is ActivateAccountEffect.ShowLastTryFullScreenError)
            assertEquals(mockContext.getString(R.string.default_error_title), (effect as ActivateAccountEffect.ShowLastTryFullScreenError).esmorgaErrorScreenArguments.title)
            assertEquals(mockContext.getString(R.string.register_confirmation_button_cancel), effect.esmorgaErrorScreenArguments.buttonText)
        }
    }


    @Test
    fun `given network failure when activateAccount is called then full screen error is shown`() = runTest {
        val verificationCode = "networkFail"
        val activateAccountUseCase = mockk<ActivateAccountUseCase>()
        coEvery {
            activateAccountUseCase(verificationCode)
        } returns EsmorgaResult(error = EsmorgaException("No connection", Source.REMOTE, ErrorCodes.NO_CONNECTION))

        val sut = ActivateAccountViewModel(verificationCode, activateAccountUseCase)

        sut.effect.test {
            sut.onStart(lifeCycleOwner)

            val effect = awaitItem()
            Assert.assertTrue(effect is ActivateAccountEffect.ShowFullScreenError)
            assertEquals(mockContext.getString(R.string.register_confirmation_error_title), (effect as ActivateAccountEffect.ShowFullScreenError).esmorgaErrorScreenArguments.title)
            assertEquals(mockContext.getString(R.string.register_confirmation_button_retry), effect.esmorgaErrorScreenArguments.buttonText)
        }
    }

    @Test
    fun `given successful activation when onContinueClicked is called then navigate to welcome screen`() = runTest {
        val verificationCode = "code"
        val activateAccountUseCase = mockk<ActivateAccountUseCase>()
        coEvery {
            activateAccountUseCase(verificationCode)
        } returns EsmorgaResult(Unit)

        val sut = ActivateAccountViewModel(verificationCode, activateAccountUseCase)

        sut.effect.test {
            sut.onContinueClicked()
            val effect = awaitItem()
            Assert.assertTrue(effect is ActivateAccountEffect.NavigateToWelcomeScreen)
        }
    }
}
