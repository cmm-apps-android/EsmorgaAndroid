package cmm.apps.esmorga.datasource_remote.user

import android.content.Context
import cmm.apps.esmorga.data.user.datasource.AuthDatasource
import cmm.apps.esmorga.datasource_remote.api.EsmorgaApi
import cmm.apps.esmorga.datasource_remote.api.EsmorgaAuthApi
import cmm.apps.esmorga.datasource_remote.mock.UserRemoteMock
import cmm.apps.esmorga.domain.result.EsmorgaException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class UserRemoteDatasourceImplTest {

    @Test
    fun `given valid credentials when login succeeds then user is returned`() = runTest {
        val remoteUserName = "Albus"

        val api = mockk<EsmorgaAuthApi>(relaxed = true)
        val authDatasource = mockk<AuthDatasource>(relaxed = true)
        val esmorgaApi = mockk<EsmorgaApi>(relaxed = true)

        coEvery { api.login(any()) } returns UserRemoteMock.provideUser(remoteUserName)

        val sut = UserRemoteDatasourceImpl(api, esmorgaApi, authDatasource)
        val result = sut.login("email", "password")

        Assert.assertEquals(remoteUserName, result.dataName)

        coVerify { authDatasource.saveTokens(any(), any(), any()) }
    }

    @Test(expected = Exception::class)
    fun `given invalid credentials when login fails then exception is thrown`() = runTest {
        val api = mockk<EsmorgaAuthApi>(relaxed = true)
        val authDatasource = mockk<AuthDatasource>(relaxed = true)
        val esmorgaApi = mockk<EsmorgaApi>(relaxed = true)

        coEvery { api.login(any()) } throws HttpException(Response.error<ResponseBody>(401, "Error".toResponseBody("application/json".toMediaTypeOrNull())))

        val sut = UserRemoteDatasourceImpl(api, esmorgaApi, authDatasource)
        sut.login("email", "password")
    }

    @Test
    fun `given valid data when registration succeeds then user is returned`() = runTest {
        val remoteUserName = "Barbus"

        val api = mockk<EsmorgaAuthApi>(relaxed = true)
        val authDatasource = mockk<AuthDatasource>(relaxed = true)
        val esmorgaApi = mockk<EsmorgaApi>(relaxed = true)
        coEvery { api.register(any()) } returns Unit

        val sut = UserRemoteDatasourceImpl(api, esmorgaApi, authDatasource)
        val result = sut.register(remoteUserName, "lastName", "email", "password")

        Assert.assertEquals(Unit, result)
    }

    @Test
    fun `given invalid data when registration fails then exception is thrown`() = runTest {
        val errorCode = 400

        val context = mockk<Context>(relaxed = true)
        val api = mockk<EsmorgaAuthApi>(relaxed = true)
        val authDatasource = mockk<AuthDatasource>(relaxed = true)
        val esmorgaApi = mockk<EsmorgaApi>(relaxed = true)

        coEvery { api.register(any()) } throws HttpException(Response.error<ResponseBody>(errorCode, "Error".toResponseBody("application/json".toMediaTypeOrNull())))
        coEvery { context.getSystemService(Context.CONNECTIVITY_SERVICE) }

        val sut = UserRemoteDatasourceImpl(api, esmorgaApi, authDatasource)

        val exception = try {
            sut.register("name", "lastName", "email", "password")
            null
        } catch (exception: RuntimeException) {
            exception
        }

        Assert.assertTrue(exception is EsmorgaException)
        Assert.assertEquals(errorCode, (exception as EsmorgaException).code)
    }

    @Test
    fun `given valid data when email verification succeeds then Unit is returned`() = runTest {
        val api = mockk<EsmorgaAuthApi>(relaxed = true)
        val authDatasource = mockk<AuthDatasource>(relaxed = true)
        val esmorgaApi = mockk<EsmorgaApi>(relaxed = true)
        coEvery { api.emailVerification(any()) } returns Unit

        val sut = UserRemoteDatasourceImpl(api, esmorgaApi, authDatasource)
        val result = sut.emailVerification("test@example.com")

        Assert.assertEquals(Unit, result)
    }

    @Test(expected = Exception::class)
    fun `given api call fails when emailVerification is invoked then Exception is thrown(`() = runTest {
        val api = mockk<EsmorgaAuthApi>(relaxed = true)
        val authDatasource = mockk<AuthDatasource>(relaxed = true)
        val esmorgaApi = mockk<EsmorgaApi>(relaxed = true)
        coEvery { api.emailVerification(any()) } throws HttpException(Response.error<ResponseBody>(400, "Error".toResponseBody("application/json".toMediaTypeOrNull())))

        val sut = UserRemoteDatasourceImpl(api, esmorgaApi, authDatasource)
        sut.emailVerification("test@example.com")
    }

    @Test
    fun `given valid data when recover password succeeds then Unit is returned`() = runTest {
        val api = mockk<EsmorgaAuthApi>(relaxed = true)
        val authDatasource = mockk<AuthDatasource>(relaxed = true)
        val esmorgaApi = mockk<EsmorgaApi>(relaxed = true)
        coEvery { api.recoverPassword(any()) } returns Unit

        val sut = UserRemoteDatasourceImpl(api, esmorgaApi, authDatasource)
        val result = sut.recoverPassword("test@example.com")

        Assert.assertEquals(Unit, result)
    }

    @Test(expected = Exception::class)
    fun `given api call fails when recoverPassword is invoked then Exception is thrown`() = runTest {
        val api = mockk<EsmorgaAuthApi>(relaxed = true)
        val authDatasource = mockk<AuthDatasource>(relaxed = true)
        val esmorgaApi = mockk<EsmorgaApi>(relaxed = true)
        coEvery { api.recoverPassword(any()) } throws HttpException(Response.error<ResponseBody>(400, "Error".toResponseBody("application/json".toMediaTypeOrNull())))

        val sut = UserRemoteDatasourceImpl(api, esmorgaApi, authDatasource)
        sut.recoverPassword("test@example.com")
    }

    @Test
    fun `given valid data, when resetPassword is succeed then Unit is returned`() = runTest {
        val api = mockk<EsmorgaAuthApi>(relaxed = true)
        val authDatasource = mockk<AuthDatasource>(relaxed = true)
        val esmorgaApi = mockk<EsmorgaApi>(relaxed = true)
        coEvery { api.resetPassword(any()) } returns Unit

        val sut = UserRemoteDatasourceImpl(api, esmorgaApi, authDatasource)
        val result = sut.resetPassword("347638", "password")

        Assert.assertEquals(Unit, result)
    }

    @Test(expected = Exception::class)
    fun `given api call fails when resetPassword is invoked then Exception is thrown `() = runTest {
        val api = mockk<EsmorgaAuthApi>(relaxed = true)
        val authDatasource = mockk<AuthDatasource>(relaxed = true)
        val esmorgaApi = mockk<EsmorgaApi>(relaxed = true)
        coEvery { api.resetPassword(any()) } throws HttpException(Response.error<ResponseBody>(400, "Error".toResponseBody("application/json".toMediaTypeOrNull())))

        val sut = UserRemoteDatasourceImpl(api, esmorgaApi, authDatasource)
        val result = sut.resetPassword("347638", "password")

        Assert.assertEquals(Unit, result)
    }

    @Test
    fun `given valid data, when changePassword is succeed then Unit is returned`() = runTest {
        val api = mockk<EsmorgaAuthApi>(relaxed = true)
        val authDatasource = mockk<AuthDatasource>(relaxed = true)
        val esmorgaApi = mockk<EsmorgaApi>(relaxed = true)

        coEvery { esmorgaApi.changePassword(any()) } returns Unit

        val sut = UserRemoteDatasourceImpl(api, esmorgaApi, authDatasource)
        val result = sut.changePassword("password1", "password2")

        Assert.assertEquals(Unit, result)
    }

    @Test(expected = Exception::class)
    fun `given api call fails when changePassword is invoked then Exception is thrown `() = runTest {
        val api = mockk<EsmorgaAuthApi>(relaxed = true)
        val authDatasource = mockk<AuthDatasource>(relaxed = true)
        val esmorgaApi = mockk<EsmorgaApi>(relaxed = true)

        coEvery { esmorgaApi.changePassword(any()) } throws HttpException(Response.error<ResponseBody>(400, "Error".toResponseBody("application/json".toMediaTypeOrNull())))

        val sut = UserRemoteDatasourceImpl(api, esmorgaApi, authDatasource)
        val result = sut.changePassword("password1", "password2")

        Assert.assertEquals(Unit, result)
    }
}