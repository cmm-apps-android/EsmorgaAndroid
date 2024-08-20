package cmm.apps.esmorga.view.viewmodel.registration

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import cmm.apps.esmorga.domain.result.Success
import cmm.apps.esmorga.domain.user.PerformRegistrationUserCase
import cmm.apps.esmorga.view.registration.RegistrationViewModel
import cmm.apps.esmorga.view.registration.model.RegistrationEffect
import cmm.apps.esmorga.view.viewmodel.mock.LoginViewMock
import cmm.apps.esmorga.view.viewmodel.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.asExecutor
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
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RegistrationViewModelTest : KoinTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun init() {
        startKoin {
            androidContext(ApplicationProvider.getApplicationContext())
        }
    }

    @After
    fun shutDown() {
        stopKoin()
    }

    @Test
    fun `given a successful usecase when register method is called then usecase executed and UI effect for successful register is emitted`() = runTest {
        val user = LoginViewMock.provideUser()
        val app = mockk<Application>(relaxed = true)
        val useCase = mockk<PerformRegistrationUserCase>(relaxed = true)
        coEvery { useCase(any(), any(), any(), any()) } returns Result.success(Success(user))

        val sut = RegistrationViewModel(app, useCase)

        sut.effect.test {
            sut.onRegisterClicked(user.name, user.lastName, user.email, "Test@123", "Test@123")

            val effect = awaitItem()
            Assert.assertTrue(effect is RegistrationEffect.NavigateToEventList)
        }
    }

    @Test
    fun `given a incorrect name inputed when register method is called then ui shows name error`() = runTest {
        val user = LoginViewMock.provideUser()
        val app = mockk<Application>(relaxed = true)
        val useCase = mockk<PerformRegistrationUserCase>(relaxed = true)
        coEvery { useCase(any(), any(), any(), any()) } returns Result.success(Success(user))

        val sut = RegistrationViewModel(app, useCase)
        sut.onRegisterClicked("", user.lastName, user.email, "Test@123", "Test@123")

        val state = sut.uiState.value
        Assert.assertFalse(state.nameError.isNullOrBlank())
    }

}