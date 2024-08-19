package cmm.apps.esmorga.view.viewmodel.login

import android.app.Application
import app.cash.turbine.test
import cmm.apps.esmorga.domain.result.Success
import cmm.apps.esmorga.domain.user.PerformLoginUseCase
import cmm.apps.esmorga.view.login.LoginViewModel
import cmm.apps.esmorga.view.login.model.LoginEffect
import cmm.apps.esmorga.view.viewmodel.mock.LoginViewMock
import cmm.apps.esmorga.view.viewmodel.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `given a successful usecase when login method is called usecase executed and UI effect for successful login is emitted`() = runTest {
        val user = LoginViewMock.provideUser()
        val app = mockk<Application>(relaxed = true)
        val useCase = mockk<PerformLoginUseCase>(relaxed = true)
        coEvery { useCase(any(), any()) } returns Result.success(Success(user))

        val sut = LoginViewModel(app, useCase)

        sut.effect.test{
            sut.onLoginClicked(user.email, "Test@123")

            val effect = awaitItem()
            Assert.assertTrue(effect is LoginEffect.NavigateToEventList)
        }
    }
}