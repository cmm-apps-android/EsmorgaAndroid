package cmm.apps.esmorga.domain.user

import cmm.apps.esmorga.domain.mock.UserDomainMock
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.Source
import cmm.apps.esmorga.domain.user.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test


class GetSavedUserUserCaseTest {

    @Test
    fun `given a successful repository when user requested then user is successfully returned`() = runTest {
        val repoUserName = "Io"
        val repoUser = UserDomainMock.provideUser(name = repoUserName)
        val repo = mockk<UserRepository>(relaxed = true)
        coEvery { repo.getUser() } returns repoUser

        val sut = GetSavedUserUseCaseImpl(repo)
        val result = sut.invoke()
        Assert.assertEquals(repoUserName, result.data!!.name)
    }

    @Test
    fun `given an empty repository when user requested then failure is returned`() = runTest {
        val repo = mockk<UserRepository>(relaxed = true)
        coEvery { repo.getUser() } throws EsmorgaException(message = "User not found", source = Source.LOCAL, code = ErrorCodes.NO_DATA)

        val sut = GetSavedUserUseCaseImpl(repo)
        val result = sut.invoke()
        Assert.assertTrue(result.error is EsmorgaException)
    }

}