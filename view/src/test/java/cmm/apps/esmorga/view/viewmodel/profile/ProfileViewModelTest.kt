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
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin

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
        startKoin {
            androidContext(context)
        }
        sut = ProfileViewModel(
            getSavedUserUseCase = getSavedUserUseCase,
            logOutUseCase = logOutUseCase,
        )
    }

    @After
    fun shutDown() {
        stopKoin()
    }

    @Test
    fun `given a successful usecase when loadUser is called then uiState is updated with the user`() = runTest {
        val user = User("1", "User", "email@example.com", "USER")

        coEvery { getSavedUserUseCase() } returns EsmorgaResult.success(user)

        sut.loadUser()

        assertEquals(user, sut.uiState.value.user)
    }

    @Test
    fun `given a successful logout usecase when logout is called then uiState is cleared`() = runTest {
        coEvery { logOutUseCase() } returns EsmorgaResult.success(true)

        sut.logout()

        assertNull(sut.uiState.value.user)

        sut.effect.test {
            expectNoEvents()
        }
    }


    @Test
    fun `given logIn is called then NavigateToLogIn effect is emitted`() = runTest {
        sut.effect.test {
            sut.logIn()

            val effect = awaitItem()
            assertTrue(effect is ProfileEffect.NavigateToLogIn)
        }
    }
}