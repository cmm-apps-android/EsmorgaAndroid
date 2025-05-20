package cmm.apps.esmorga.domain.user

import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.Source
import cmm.apps.esmorga.domain.user.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class PerformRecoverPasswordUseCaseImplTest {

    @Test
    fun `given UserRepository successfully recovers password when use case is invoked then it returns success`() = runTest {
        val repo = mockk<UserRepository>(relaxed = true)
        coEvery { repo.recoverPassword(any()) } returns Unit

        val sut = PerformRecoverPasswordUseCaseImpl(repo)
        val result = sut.invoke("test@example.com")
        Assert.assertEquals(Unit, result.data)
    }

    @Test
    fun `given password recovery fails in repository when use case is invoked then it returns failure()`() = runTest {
        val repo = mockk<UserRepository>(relaxed = true)
        coEvery { repo.recoverPassword(any()) } throws EsmorgaException("error", Source.REMOTE, 500)

        val sut = PerformRecoverPasswordUseCaseImpl(repo)
        val result = sut.invoke("test@example.com")
        Assert.assertTrue(result.error is EsmorgaException)
    }
}