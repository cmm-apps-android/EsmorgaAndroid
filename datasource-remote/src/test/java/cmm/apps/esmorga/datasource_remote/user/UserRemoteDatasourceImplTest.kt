package cmm.apps.esmorga.datasource_remote.user

import cmm.apps.esmorga.datasource_remote.api.EsmorgaApi
import cmm.apps.esmorga.datasource_remote.mock.UserRemoteMock
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
    fun `given valid credentials when login succeed then user is returned`() = runTest {
        val remoteUserName = "Albus"

        val api = mockk<EsmorgaApi>(relaxed = true)
        coEvery { api.login(any()) } returns UserRemoteMock.provideUser(remoteUserName)

        val sut = UserRemoteDatasourceImpl(api)
        val result = sut.login("email", "password")

        Assert.assertTrue(result.isSuccess)
        Assert.assertEquals(remoteUserName, result.getOrThrow().dataName)
    }

    @Test(expected = Exception::class)
    fun `given invalid credentials when login fails then exception is thrown`() = runTest {
        val api = mockk<EsmorgaApi>(relaxed = true)
        coEvery { api.login(any()) } throws HttpException(Response.error<ResponseBody>(401, "Error".toResponseBody("application/json".toMediaTypeOrNull())))

        val sut = UserRemoteDatasourceImpl(api)
        sut.login("email", "password")
    }

}