package cmm.apps.esmorga.data.user

import cmm.apps.esmorga.data.mock.UserDataMock
import cmm.apps.esmorga.data.user.datasource.UserDatasource
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.Source
import io.mockk.coEvery
import io.mockk.coVerify
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

        Assert.assertEquals(name, result.name)
    }

    @Test
    fun `given valid credentials when login succeed then user is returned`() = runTest {
        val name = "Ron"
        val localDS = mockk<UserDatasource>(relaxed = true)
        val remoteDS = mockk<UserDatasource>(relaxed = true)
        coEvery { remoteDS.login(any(), any()) } returns UserDataMock.provideUserDataModel(name = name)
        val sut = UserRepositoryImpl(localDS, remoteDS)
        val result = sut.login("email", "password")

        Assert.assertEquals(name, result.name)
    }

    @Test
    fun `given invalid credentials when login fails then exception is thrown`() = runTest {
        val errorCode = 401
        val localDS = mockk<UserDatasource>(relaxed = true)
        val remoteDS = mockk<UserDatasource>(relaxed = true)
        coEvery { remoteDS.login(any(), any()) } throws EsmorgaException("error", Source.REMOTE, errorCode)
        val sut = UserRepositoryImpl(localDS, remoteDS)

        val exception = try {
            sut.login("invalidEmail", "invalidPassword")
        } catch (exception: RuntimeException) {
            exception
        }

        Assert.assertTrue(exception is EsmorgaException)
        Assert.assertEquals(errorCode, (exception as EsmorgaException).code)
    }

    @Test(expected = EsmorgaException::class)
    fun `given valid credentials when login fails then exception is thrown`() = runTest {
        val localDS = mockk<UserDatasource>(relaxed = true)
        val remoteDS = mockk<UserDatasource>(relaxed = true)
        coEvery { remoteDS.login(any(), any()) } throws EsmorgaException("error", Source.REMOTE, 500)
        val sut = UserRepositoryImpl(localDS, remoteDS)

        sut.login("validEmail", "validPassword")
    }

    @Test
    fun `given valid data when registration succeeds then user is returned`() = runTest {
        val name = "Mezcal"
        val localDS = mockk<UserDatasource>(relaxed = true)
        val remoteDS = mockk<UserDatasource>(relaxed = true)
        coEvery { remoteDS.register(any(), any(), any(), any()) } returns UserDataMock.provideUserDataModel(name = name)
        val sut = UserRepositoryImpl(localDS, remoteDS)
        val result = sut.register(name, "lastName", "email", "password")

        Assert.assertEquals(name, result.name)
    }

    @Test
    fun `given valid data when registration succeeds then user is stored in local`() = runTest {
        val user = UserDataMock.provideUserDataModel(name = "Dirty Harry")

        val localDS = mockk<UserDatasource>(relaxed = true)
        val remoteDS = mockk<UserDatasource>(relaxed = true)
        coEvery { remoteDS.register(any(), any(), any(), any()) } returns user
        val sut = UserRepositoryImpl(localDS, remoteDS)
        sut.register("name", "lastName", "email", "password")

        coVerify { localDS.saveUser(user) }
    }
}