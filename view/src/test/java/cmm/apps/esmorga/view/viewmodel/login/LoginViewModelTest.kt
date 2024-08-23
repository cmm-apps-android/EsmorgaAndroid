package cmm.apps.esmorga.view.viewmodel.login

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import cmm.apps.esmorga.domain.result.Success
import cmm.apps.esmorga.domain.user.PerformLoginUseCase
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
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
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
        coEvery { useCase(any(), any()) } returns Result.success(Success(user))

        val sut = LoginViewModel(useCase)

        sut.effect.test {
            sut.onLoginClicked(user.email, "Test@123")

            val effect = awaitItem()
            Assert.assertTrue(effect is LoginEffect.NavigateToEventList)
        }
    }


}