package cmm.apps.esmorga.view.viewmodel.profile

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.domain.user.GetSavedUserUseCase
import cmm.apps.esmorga.domain.user.LogOutUseCase
import cmm.apps.esmorga.domain.user.model.User
import cmm.apps.esmorga.view.profile.ProfileViewModel
import cmm.apps.esmorga.view.profile.model.ProfileEffect
import cmm.apps.esmorga.view.viewmodel.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var context: Context
    private lateinit var sut: ProfileViewModel

    private val getSavedUserUseCase = mockk<GetSavedUserUseCase>(relaxed = true)
    private val logOutUseCase = mockk<LogOutUseCase>(relaxed = true)

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        sut = ProfileViewModel(
            getSavedUserUseCase = getSavedUserUseCase,
            logOutUseCase = logOutUseCase,
            isInternetAvailable = { false } // Simula no tener internet
        )
    }

    @Test
    fun `given a successful usecase when loadUser is called then uiState is updated with the user`() = runTest {
        val user = User("1", "User", "email@example.com")

        coEvery { getSavedUserUseCase() } returns EsmorgaResult.success(user)

        sut.loadUser()

        // Verifica que el estado de UI contiene al usuario
        assertEquals(user, sut.uiState.value.user)
    }

    @Test
    fun `given a successful logout usecase when logout is called then uiState is cleared and NavigateToLogIn effect is emitted`() = runTest {
        coEvery { logOutUseCase() } returns EsmorgaResult.success(true)

        sut.effect.test {
            sut.logout()

            // Verifica que el usuario ha sido borrado
            assertNull(sut.uiState.value.user)

            // Verifica que el efecto de navegación a Login se ha emitido
            val effect = awaitItem()
            assertTrue(effect is ProfileEffect.NavigateToLogIn)
        }
    }

    @Test
    fun `given logIn is called then NavigateToLogIn effect is emitted`() = runTest {
        sut.effect.test {
            sut.logIn()

            // Verifica que el efecto de navegación a Login se emite
            val effect = awaitItem()
            assertTrue(effect is ProfileEffect.NavigateToLogIn)
        }
    }

    @Test
    fun `given internet is available when changePassword is called then NavigateToChangePassword effect is emitted`() = runTest {
        // Usamos un isInternetAvailable que devuelve true (simula que hay internet)
        sut = ProfileViewModel(
            getSavedUserUseCase = getSavedUserUseCase,
            logOutUseCase = logOutUseCase,
            isInternetAvailable = { true } // Simula que hay internet
        )

        sut.effect.test {
            sut.changePassword(context)

            // Verifica que el efecto de navegación a Change Password se emite
            val effect = awaitItem()
            assertTrue(effect is ProfileEffect.NavigateToChangePassword)
        }
    }
}
