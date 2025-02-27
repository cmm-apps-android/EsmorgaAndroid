package cmm.apps.esmorga.domain.user

import cmm.apps.esmorga.domain.mock.UserDomainMock
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.Source
import cmm.apps.esmorga.domain.user.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class PerformRegistrationConfirmationUseCaseImplTest {

    @Test
    fun `given a successful repository when registration confirmation is performed then success is return`() = runTest {
        val repoUser = UserDomainMock.provideUser()
        val repo = mockk<UserRepository>(relaxed = true)
        coEvery { repo.emailVerification(any()) } returns Unit

        val sut = PerformRegistrationConfirmationUseCaseImpl(repo)
        val result = sut.invoke(repoUser.email)
        Assert.assertEquals(Unit, result.data!!)
    }

    @Test
    fun `given a faulty repository when registration confirmation is performed then exception is thrown`() = runTest {
        val repoUser = UserDomainMock.provideUser()
        val repo = mockk<UserRepository>(relaxed = true)
        coEvery { repo.emailVerification(any()) } throws EsmorgaException("error", Source.REMOTE, 500)

        val sut = PerformRegistrationConfirmationUseCaseImpl(repo)
        val result = sut.invoke(repoUser.email)

        Assert.assertTrue(result.error is EsmorgaException)
    }
}