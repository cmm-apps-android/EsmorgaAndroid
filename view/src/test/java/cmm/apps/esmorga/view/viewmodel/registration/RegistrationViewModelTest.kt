package cmm.apps.esmorga.view.viewmodel.registration

import android.app.Application
import app.cash.turbine.test
import cmm.apps.esmorga.domain.result.Success
import cmm.apps.esmorga.domain.user.PerformRegistrationUserCase
import cmm.apps.esmorga.view.registration.RegistrationViewModel
import cmm.apps.esmorga.view.registration.model.RegistrationEffect
import cmm.apps.esmorga.view.viewmodel.mock.LoginViewMock
import cmm.apps.esmorga.view.viewmodel.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test


class RegistrationViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

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

}