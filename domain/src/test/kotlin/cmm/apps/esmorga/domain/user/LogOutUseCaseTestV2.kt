package cmm.apps.esmorga.domain.user

import cmm.apps.esmorga.domain.user.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class LogOutUseCaseTestV2 {

    @Test
    fun `given user is logged in when logging out then return success`() = runTest {
        val repo = mockk<UserRepository>()
        coJustRun { repo.logout() }

        val sut = LogOutUseCaseV2Impl(repo)

        val result = sut.invoke()

        Assert.assertTrue(result.data == true)
    }

    @Test
    fun `given error during logout when logging out then return failure`() = runTest {
        val repo = mockk<UserRepository>()
        val exception = RuntimeException("Logout failed")
        coEvery { repo.logout() } throws exception

        val sut = LogOutUseCaseV2Impl(repo)

        val result = sut.invoke()

        Assert.assertTrue(result.data == false)
    }
}