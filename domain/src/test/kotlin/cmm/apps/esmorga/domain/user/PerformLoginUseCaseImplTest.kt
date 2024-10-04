package cmm.apps.esmorga.domain.user

import cmm.apps.esmorga.domain.mock.UserDomainMock
import cmm.apps.esmorga.domain.user.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class PerformLoginUseCaseImplTest {

    @Test
    fun `given a successful repository when login is performed then user is returned`() = runTest {
        val repoUserName = "Luna"
        val repoUser = UserDomainMock.provideUser(name = repoUserName)
        val repo = mockk<UserRepository>(relaxed = true)
        coEvery { repo.login(any(), any()) } returns repoUser

        val sut = PerformLoginUseCaseImpl(repo)
        val result = sut.invoke(repoUser.email, "password")
        Assert.assertEquals(repoUserName, result.data!!.name)
    }
}