package cmm.apps.esmorga.domain.user

import cmm.apps.esmorga.domain.mock.UserDomainMock
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.Source
import cmm.apps.esmorga.domain.result.Success
import cmm.apps.esmorga.domain.user.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class PerformRegistrationUseCaseImplTest {

    @Test
    fun `given a successful repository when registration is performed then user is returned`() = runTest {
        val repoUserName = "Io"
        val repoUser = UserDomainMock.provideUser(name = repoUserName)
        val repo = mockk<UserRepository>(relaxed = true)
        coEvery { repo.register(any(), any(), any(), any()) } returns Success(repoUser)

        val sut = PerformRegistrationUserCaseImpl(repo)
        val result = sut.invoke(repoUser.name, repoUser.lastName, repoUser.email, "password")
        Assert.assertTrue(result.isSuccess)
        Assert.assertEquals(repoUserName, result.getOrThrow().data.name)
    }

    @Test
    fun `given a faulty repository when registration is performed then exception is thrown`() = runTest {
        val repoUser = UserDomainMock.provideUser(name = "Callisto")
        val repo = mockk<UserRepository>(relaxed = true)
        coEvery { repo.register(any(), any(), any(), any()) } throws EsmorgaException("error", Source.REMOTE, 500)

        val sut = PerformRegistrationUserCaseImpl(repo)
        val result = sut.invoke(repoUser.name, repoUser.lastName, repoUser.email, "password")

        Assert.assertTrue(result.isFailure)
        Assert.assertTrue(result.exceptionOrNull() is EsmorgaException)
    }
}