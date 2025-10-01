package cmm.apps.esmorga.view.viewmodel.profile

import app.cash.turbine.test
import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.domain.user.GetSavedUserUseCaseV2
import cmm.apps.esmorga.domain.user.LogOutUseCaseV2
import cmm.apps.esmorga.domain.user.model.RoleType
import cmm.apps.esmorga.domain.user.model.User
import cmm.apps.esmorga.view.profileV2.ProfileViewModelV2
import cmm.apps.esmorga.view.profileV2.model.ProfileEffectV2
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTestV2 {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `given saved user exists when showUser is called then update state with user`() = runTest {
        val user = User("Name", "LastName", "email@email.com", RoleType.USER)
        val getUserUseCase = mockk<GetSavedUserUseCaseV2>()
        coEvery { getUserUseCase.invoke() } returns EsmorgaResult.success(user)

        val logoutUseCase = mockk<LogOutUseCaseV2>()
        coJustRun { logoutUseCase.invoke() }

        val sut = ProfileViewModelV2(getUserUseCase, logoutUseCase)
        advanceUntilIdle()

        sut.showUser()

        assertEquals(user, sut.uiState.value.user)
    }

    @Test
    fun `given error occurs when showUser is called then update state with null user`() = runTest {
        val getUserUseCase = mockk<GetSavedUserUseCaseV2>()
        coEvery { getUserUseCase.invoke() } returns EsmorgaResult.failure(Exception("DB error"))

        val logoutUseCase = mockk<LogOutUseCaseV2>()
        coJustRun { logoutUseCase.invoke() }

        val sut = ProfileViewModelV2(getUserUseCase, logoutUseCase)

        sut.showUser()

        assertNull(sut.uiState.value.user)
    }

    @Test
    fun `given user is logged in when logOut is called then clear user from state`() = runTest {
        val user = User("Name", "LastName", "email@email.com", RoleType.USER)
        val getUserUseCase = mockk<GetSavedUserUseCaseV2>()
        coEvery { getUserUseCase.invoke() } returns EsmorgaResult.success(user)

        val logoutUseCase = mockk<LogOutUseCaseV2>()
        coEvery { logoutUseCase.invoke() } returns EsmorgaResult.success(true)

        val sut = ProfileViewModelV2(getUserUseCase, logoutUseCase)
        advanceUntilIdle()

        assertNotNull(sut.uiState.value.user)

        sut.logOut()
        advanceUntilIdle()

        assertNull(sut.uiState.value.user)
    }


    @Test
    fun `given navigation action when triggered then emit correct effect`() = runTest {
        val getUserUseCase = mockk<GetSavedUserUseCaseV2>()
        coEvery { getUserUseCase.invoke() } returns EsmorgaResult.failure(Exception())

        val logoutUseCase = mockk<LogOutUseCaseV2>()
        coJustRun { logoutUseCase.invoke() }

        val sut = ProfileViewModelV2(getUserUseCase, logoutUseCase)

        sut.effect.test {
            sut.navigateTologIn()
            assertEquals(ProfileEffectV2.NavigateToLogIn, awaitItem())

            sut.navigateToChangePassword()
            assertEquals(ProfileEffectV2.NavigateToChangePassword, awaitItem())
        }
    }
}