package cmm.apps.esmorga.data.user

import cmm.apps.esmorga.data.event.datasource.EventDatasource
import cmm.apps.esmorga.data.mock.EventDataMock
import cmm.apps.esmorga.data.mock.UserDataMock
import cmm.apps.esmorga.data.user.datasource.UserDatasource
import cmm.apps.esmorga.data.user.model.UserDataModel
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.Source
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import junit.framework.TestCase.fail
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class UserRepositoryImplTest {
    @Test
    fun `given local data when user requested then local user is returned`() = runTest {
        val name = "Harry"

        val localDS = mockk<UserDatasource>(relaxed = true)
        val remoteDS = mockk<UserDatasource>(relaxed = true)
        val localEventDS = mockk<EventDatasource>(relaxed = true)
        coEvery { localDS.getUser() } returns UserDataMock.provideUserDataModel(name = name)

        val sut = UserRepositoryImpl(localDS, remoteDS, localEventDS)
        val result = sut.getUser()

        Assert.assertEquals(name, result.name)
    }

    @Test
    fun `given valid credentials when login succeed then user is returned`() = runTest {
        val name = "Ron"
        val localDS = mockk<UserDatasource>(relaxed = true)
        val remoteDS = mockk<UserDatasource>(relaxed = true)
        val localEventDS = mockk<EventDatasource>(relaxed = true)
        coEvery { remoteDS.login(any(), any()) } returns UserDataMock.provideUserDataModel(name = name)
        val sut = UserRepositoryImpl(localDS, remoteDS, localEventDS)
        val result = sut.login("email", "password")

        Assert.assertEquals(name, result.name)
    }

    @Test
    fun `given invalid credentials when login fails then exception is thrown`() = runTest {
        val errorCode = 401
        val localDS = mockk<UserDatasource>(relaxed = true)
        val remoteDS = mockk<UserDatasource>(relaxed = true)
        val localEventDS = mockk<EventDatasource>(relaxed = true)
        coEvery { remoteDS.login(any(), any()) } throws EsmorgaException("error", Source.REMOTE, errorCode)
        val sut = UserRepositoryImpl(localDS, remoteDS, localEventDS)

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
        val localEventDS = mockk<EventDatasource>(relaxed = true)
        coEvery { remoteDS.login(any(), any()) } throws EsmorgaException("error", Source.REMOTE, 500)
        val sut = UserRepositoryImpl(localDS, remoteDS, localEventDS)

        sut.login("validEmail", "validPassword")
    }

    @Test
    fun `given valid data when registration succeeds then success is returned`() = runTest {
        val name = "Mezcal"
        val localDS = mockk<UserDatasource>(relaxed = true)
        val remoteDS = mockk<UserDatasource>(relaxed = true)
        val localEventDS = mockk<EventDatasource>(relaxed = true)
        coEvery { remoteDS.register(any(), any(), any(), any()) } returns Unit
        val sut = UserRepositoryImpl(localDS, remoteDS, localEventDS)
        val result = sut.register(name, "lastName", "email", "password")

        Assert.assertEquals(Unit, result)
    }

    @Test
    fun `given valid credentials when login succeed then events cache is deleted`() = runTest {
        val localEventName = "LocalEvent"
        val name = "Hermione"
        val eventsMock = EventDataMock.provideEventDataModelList(listOf(localEventName))

        val localDS = mockk<UserDatasource>(relaxed = true)
        val remoteDS = mockk<UserDatasource>(relaxed = true)
        val localEventDS = mockk<EventDatasource>(relaxed = true)
        coEvery { remoteDS.login(any(), any()) } returns UserDataMock.provideUserDataModel(name = name)
        coEvery { localEventDS.getEvents() } returns eventsMock
        val sut = UserRepositoryImpl(localDS, remoteDS, localEventDS)
        sut.login("email", "password")

        coVerify { localEventDS.deleteCacheEvents() }
    }

    @Test
    fun `given valid data when resend email succeeds then success is returned`() = runTest {
        val localDS = mockk<UserDatasource>(relaxed = true)
        val remoteDS = mockk<UserDatasource>(relaxed = true)
        val localEventDS = mockk<EventDatasource>(relaxed = true)
        coEvery { remoteDS.emailVerification(any()) } returns Unit
        val sut = UserRepositoryImpl(localDS, remoteDS, localEventDS)
        val result = sut.emailVerification("email")

        Assert.assertEquals(Unit, result)
    }

    @Test(expected = EsmorgaException::class)
    fun `given remote datasource throws error when emailVerification is called then EsmorgaException is thrown()`() = runTest {
        val localDS = mockk<UserDatasource>(relaxed = true)
        val remoteDS = mockk<UserDatasource>(relaxed = true)
        val localEventDS = mockk<EventDatasource>(relaxed = true)
        coEvery { remoteDS.emailVerification(any()) } throws EsmorgaException("error", Source.REMOTE, 500)
        val sut = UserRepositoryImpl(localDS, remoteDS, localEventDS)
        sut.emailVerification("email")
    }

    @Test
    fun `given a log out when user request then user data is deleted`() = runTest {
        val localDS = mockk<UserDatasource>(relaxed = true)
        val remoteDS = mockk<UserDatasource>(relaxed = true)
        val localEventDS = mockk<EventDatasource>(relaxed = true)
        coEvery { localDS.deleteUserSession() } returns Unit
        coEvery { localEventDS.deleteCacheEvents() } returns Unit

        val sut = UserRepositoryImpl(localDS, remoteDS, localEventDS)
        sut.logout()
        coVerify { localDS.deleteUserSession() }
        coVerify { remoteDS.deleteUserSession() }
        coVerify { localEventDS.deleteCacheEvents() }
    }

    @Test
    fun `given a log out when user request then error is expected`() = runTest {
        val localDS = mockk<UserDatasource>(relaxed = true)
        val remoteDS = mockk<UserDatasource>(relaxed = true)
        val localEventDS = mockk<EventDatasource>(relaxed = true)

        coEvery { localDS.deleteUserSession() } throws Exception("fallo deleteUser")
        coEvery { localEventDS.deleteCacheEvents() } returns Unit

        val sut = UserRepositoryImpl(localDS, remoteDS, localEventDS)

        try {
            sut.logout()
            fail("Se esperaba una excepción y no se lanzó")
        } catch (e: Exception) {
            assertTrue(e.message!!.contains("Error al cerrar sesión"))
        }

        coVerify { localDS.deleteUserSession() }
    }

    @Test
    fun `given a verification code when activateAccount is called then remoteDs is called`() = runTest {
        val localDS = mockk<UserDatasource>(relaxed = true)
        val remoteDS = mockk<UserDatasource>(relaxed = true)
        val localEventDS = mockk<EventDatasource>(relaxed = true)
        val fakeUserDataModel = UserDataModel(
            dataName = "Yago",
            dataLastName = "Perez",
            dataEmail = "yago@mail.com",
            dataRole = "USER"
        )

        coEvery { remoteDS.activateAccount(any()) } returns fakeUserDataModel

        val sut = UserRepositoryImpl(localDS, remoteDS, localEventDS)

        val code = "123456"
        sut.activateAccount(code)

        coVerify { localDS.saveUser(fakeUserDataModel) }
        coVerify { localEventDS.deleteCacheEvents() }
    }

    @Test(expected = EsmorgaException::class)
    fun `given a verification code when activateAccount fails then throws exception`() = runTest {
        val localDS = mockk<UserDatasource>(relaxed = true)
        val remoteDS = mockk<UserDatasource>(relaxed = true)
        val localEventDS = mockk<EventDatasource>(relaxed = true)

        coEvery { remoteDS.activateAccount(any()) } throws EsmorgaException("Error", Source.REMOTE, 400)

        val sut = UserRepositoryImpl(localDS, remoteDS, localEventDS)

        sut.activateAccount("invalid-code")
    }

    @Test
    fun `given valid data when recover password succeeds then success is returned`() = runTest {
        val localDS = mockk<UserDatasource>(relaxed = true)
        val remoteDS = mockk<UserDatasource>(relaxed = true)
        val localEventDS = mockk<EventDatasource>(relaxed = true)
        coEvery { remoteDS.recoverPassword(any()) } returns Unit
        val sut = UserRepositoryImpl(localDS, remoteDS, localEventDS)
        val result = sut.recoverPassword("test@example.com")

        Assert.assertEquals(Unit, result)
    }

    @Test(expected = EsmorgaException::class)
    fun `given remote datasource fails to recover password when recoverPassword is called then EsmorgaException is thrown`() = runTest {
        val localDS = mockk<UserDatasource>(relaxed = true)
        val remoteDS = mockk<UserDatasource>(relaxed = true)
        val localEventDS = mockk<EventDatasource>(relaxed = true)
        coEvery { remoteDS.recoverPassword(any()) } throws EsmorgaException("error", Source.REMOTE, 500)
        val sut = UserRepositoryImpl(localDS, remoteDS, localEventDS)
        sut.recoverPassword("test@example.com")

    }

    @Test
    fun `given valid data when reset password succeeds then success is returned`() = runTest {
        val remoteDs = mockk<UserDatasource>(relaxed = true)
        val localDS = mockk<UserDatasource>(relaxed = true)
        val localEventDS = mockk<EventDatasource>(relaxed = true)

        coEvery { remoteDs.resetPassword(any(), any()) } returns Unit

        val sut = UserRepositoryImpl(localDS, remoteDs, localEventDS)
        val result = sut.resetPassword("123456", "password")

        Assert.assertEquals(Unit, result)
    }

    @Test(expected = EsmorgaException::class)
    fun `given remote datasource failure, when resetPassword is called then EsmorgaException is thrown`() = runTest {
        val remoteDs = mockk<UserDatasource>(relaxed = true)
        val localDs = mockk<UserDatasource>(relaxed = true)
        val localEventDs = mockk<EventDatasource>(relaxed = true)

        coEvery { remoteDs.resetPassword(any(), any()) } throws EsmorgaException("error", Source.REMOTE, 400)

        val sut = UserRepositoryImpl(localDs, remoteDs, localEventDs)
        sut.resetPassword("123456", "password")
    }
}