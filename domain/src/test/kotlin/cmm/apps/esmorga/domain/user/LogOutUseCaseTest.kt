package cmm.apps.esmorga.domain.user

import cmm.apps.esmorga.domain.user.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class LogOutUseCaseTest {

    @Test
    fun `given a log out when user request then user data is deleted`() = runTest {
        val repo = mockk<UserRepository>()
        coEvery { repo.logout() } returns Unit

        val sut = LogOutUseCaseImpl(repo)
        val result = sut.invoke()
        Assert.assertTrue(result.data!!)
    }

    @Test
    fun `given a log out when request fails then failure is returned`() = runTest {
        val repo = mockk<UserRepository>()
        coEvery { repo.logout() } throws Exception()

        val sut = LogOutUseCaseImpl(repo)
        val result = sut.invoke()
        Assert.assertFalse(result.data!!)
    }

}