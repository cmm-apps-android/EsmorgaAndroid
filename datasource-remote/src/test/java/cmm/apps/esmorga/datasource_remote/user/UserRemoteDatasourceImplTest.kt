package cmm.apps.esmorga.datasource_remote.user

import android.content.Context
import cmm.apps.esmorga.datasource_remote.api.EsmorgaAuthApi
import cmm.apps.esmorga.datasource_remote.mock.UserRemoteMock
import cmm.apps.esmorga.domain.result.EsmorgaException
import io.mockk.coEvery
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

        val context = mockk<Context>(relaxed = true)
        val api = mockk<EsmorgaAuthApi>(relaxed = true)
        coEvery { api.login(any()) } returns UserRemoteMock.provideUser(remoteUserName)

        val sut = UserRemoteDatasourceImpl(api, context)
        val result = sut.login("email", "password")

        Assert.assertEquals(remoteUserName, result.dataName)
    }

    @Test(expected = Exception::class)
    fun `given invalid credentials when login fails then exception is thrown`() = runTest {
        val context = mockk<Context>(relaxed = true)
        val api = mockk<EsmorgaAuthApi>(relaxed = true)
        coEvery { api.login(any()) } throws HttpException(Response.error<ResponseBody>(401, "Error".toResponseBody("application/json".toMediaTypeOrNull())))

        val sut = UserRemoteDatasourceImpl(api, context)
        sut.login("email", "password")
    }

    @Test
    fun `given valid data when registration succeeds then user is returned`() = runTest {
        val remoteUserName = "Barbus"

        val context = mockk<Context>(relaxed = true)
        val api = mockk<EsmorgaAuthApi>(relaxed = true)
        coEvery { api.register(any()) } returns Unit

        val sut = UserRemoteDatasourceImpl(api, context)
        val result = sut.register(remoteUserName, "lastName", "email", "password")

        Assert.assertEquals(Unit, result)
    }

    @Test
    fun `given invalid data when registration fails then exception is thrown`() = runTest {
        val errorCode = 400

        val context = mockk<Context>(relaxed = true)
        val api = mockk<EsmorgaAuthApi>(relaxed = true)
        coEvery { api.register(any()) } throws HttpException(Response.error<ResponseBody>(errorCode, "Error".toResponseBody("application/json".toMediaTypeOrNull())))
        coEvery { context.getSystemService(Context.CONNECTIVITY_SERVICE) }

        val sut = UserRemoteDatasourceImpl(api, context)

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
        val context = mockk<Context>(relaxed = true)
        val api = mockk<EsmorgaAuthApi>(relaxed = true)
        coEvery { api.emailVerification(any()) } returns Unit

        val sut = UserRemoteDatasourceImpl(api, context)
        val result = sut.emailVerification("test@example.com")

        Assert.assertEquals(Unit, result)
    }

    @Test(expected = Exception::class)
    fun `given api call fails when emailVerification is invoked then Exception is thrown(`() = runTest {
        val context = mockk<Context>(relaxed = true)
        val api = mockk<EsmorgaAuthApi>(relaxed = true)
        coEvery { api.emailVerification(any()) } throws HttpException(Response.error<ResponseBody>(400, "Error".toResponseBody("application/json".toMediaTypeOrNull())))

        val sut = UserRemoteDatasourceImpl(api, context)
        sut.emailVerification("test@example.com")
    }

    @Test
    fun `given valid data when recover password succeeds then Unit is returned`() = runTest {
        val context = mockk<Context>(relaxed = true)
        val api = mockk<EsmorgaAuthApi>(relaxed = true)
        coEvery { api.recoverPassword(any()) } returns Unit

        val sut = UserRemoteDatasourceImpl(api, context)
        val result = sut.recoverPassword("test@example.com")

        Assert.assertEquals(Unit, result)
    }

    @Test(expected = Exception::class)
    fun `given api call fails when recoverPassword is invoked then Exception is thrown`() = runTest {
        val context = mockk<Context>(relaxed = true)
        val api = mockk<EsmorgaAuthApi>(relaxed = true)
        coEvery { api.recoverPassword(any()) } throws HttpException(Response.error<ResponseBody>(400, "Error".toResponseBody("application/json".toMediaTypeOrNull())))

        val sut = UserRemoteDatasourceImpl(api, context)
        sut.recoverPassword("test@example.com")
    }
}