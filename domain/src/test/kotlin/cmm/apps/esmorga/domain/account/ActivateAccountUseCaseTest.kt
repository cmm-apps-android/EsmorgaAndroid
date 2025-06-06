package cmm.apps.esmorga.domain.account

import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.domain.result.Source
import cmm.apps.esmorga.domain.user.model.User
import cmm.apps.esmorga.domain.user.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class ActivateAccountUseCaseImplTest {

    @Test
    fun `given a successful repository when activate account is called then return success`() = runTest {
        val code = "validCode"
        val repo = mockk<UserRepository>(relaxed = true)

        coEvery { repo.activateAccount(code) } returns Unit

        val sut = ActivateAccountUseCaseImpl(repo)

        val result = sut.invoke(code)

        Assert.assertEquals(EsmorgaResult.success(Unit), result)
    }

    @Test
    fun `given a repository failure when activate account is called then return failure`() = runTest {
        val code = "invalidCode"
        val exception = EsmorgaException(
            message = "Código inválido",
            source = Source.REMOTE,
            code = ErrorCodes.UNKNOWN_ERROR
        )
        val repo = mockk<UserRepository>(relaxed = true)
        coEvery { repo.activateAccount(code) } throws exception

        val sut = ActivateAccountUseCaseImpl(repo)

        val result = sut.invoke(code)

        Assert.assertTrue(result.error is EsmorgaException)
        val error = result.error as EsmorgaException
        Assert.assertEquals("Código inválido", error.message)
        Assert.assertEquals(Source.REMOTE, error.source)
        Assert.assertEquals(ErrorCodes.UNKNOWN_ERROR, error.code)
    }

}