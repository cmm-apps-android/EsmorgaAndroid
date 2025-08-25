package cmm.apps.esmorga.domain.user

import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.Source
import cmm.apps.esmorga.domain.user.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class PerformChangePasswordUseCaseImplTest {

    @Test
    fun `given a successful repository when reset password is performed then Unit is returned`() = runTest {
        val repo = mockk<UserRepository>(relaxed = true)
        coEvery { repo.changePassword(any(), any()) } returns Unit

        val sut = PerformChangePasswordUseCaseImpl(repo)
        val result = sut.invoke("password1", "password2")

        Assert.assertEquals(Unit, result.data)
    }

    @Test
    fun `given a faulty repository when reset password is performed then exception is thrown`() = runTest {
        val repo = mockk<UserRepository>(relaxed = true)
        coEvery { repo.changePassword(any(), any()) } throws EsmorgaException("error", Source.REMOTE, 400)

        val sut = PerformChangePasswordUseCaseImpl(repo)
        val result = sut.invoke("password1", "password2")

        Assert.assertTrue(result.error is EsmorgaException)
    }
}