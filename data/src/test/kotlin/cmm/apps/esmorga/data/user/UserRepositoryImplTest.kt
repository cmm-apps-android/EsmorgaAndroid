package cmm.apps.esmorga.data.user

import cmm.apps.esmorga.data.mock.UserDataMock
import cmm.apps.esmorga.data.user.datasource.UserDatasource
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.Source
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class UserRepositoryImplTest {
    @Test
    fun `given local data when user requested then local user is returned`() = runTest {
        val name = "Harry"

        val localDS = mockk<UserDatasource>(relaxed = true)
        val remoteDS = mockk<UserDatasource>(relaxed = true)
        coEvery { localDS.getUser() } returns UserDataMock.provideUserDataModel(name = name)

        val sut = UserRepositoryImpl(localDS, remoteDS)
        val result = sut.getUser()

        Assert.assertEquals(name, result.data.name)
    }

    @Test
    fun `given valid credentials when login succeed then user is returned`() = runTest {
        val name = "Ron"
        val localDS = mockk<UserDatasource>(relaxed = true)
        val remoteDS = mockk<UserDatasource>(relaxed = true)
        coEvery { remoteDS.login(any(), any()) } returns Result.success(UserDataMock.provideUserDataModel(name = name))
        val sut = UserRepositoryImpl(localDS, remoteDS)
        val result = sut.login("email", "password")

        Assert.assertEquals(name, result.data.name)
    }

    @Test(expected = Exception::class)
    fun `given invalid credentials when login fails then exception is thrown`() = runTest {
        val localDS = mockk<UserDatasource>(relaxed = true)
        val remoteDS = mockk<UserDatasource>(relaxed = true)
        coEvery { remoteDS.login("validEmail", "validPassword") } returns Result.success(UserDataMock.provideUserDataModel(name = "Hermione"))
        val sut = UserRepositoryImpl(localDS, remoteDS)

        sut.login("invalidEmail", "invalidPassword")
    }

    @Test(expected = EsmorgaException::class)
    fun `given valid credentials when login fails then exception is thrown`() = runTest {
        val localDS = mockk<UserDatasource>(relaxed = true)
        val remoteDS = mockk<UserDatasource>(relaxed = true)
        coEvery { remoteDS.login("validEmail", "validPassword") } returns Result.failure(EsmorgaException("error", Source.REMOTE, 500))
        val sut = UserRepositoryImpl(localDS, remoteDS)

        sut.login("validEmail", "validPassword")
    }
}