package cmm.apps.esmorga.datasource_remote.poll

import cmm.apps.esmorga.datasource_remote.api.EsmorgaEventApi
import cmm.apps.esmorga.datasource_remote.dateformatting.EsmorgaRemoteDateFormatter
import cmm.apps.esmorga.datasource_remote.event.EventRemoteDatasourceImpl
import cmm.apps.esmorga.datasource_remote.mock.PollRemoteMock
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

class PollRemoteDatasourceImplTest {

    val dateFormatter = mockk<EsmorgaRemoteDateFormatter>(relaxed = true)

    @Test
    fun `given a working api when polls requested then poll list is successfully returned`() = runTest {
        val remotePollName = "RemotePoll"

        val api = mockk<EsmorgaEventApi>(relaxed = true)
        coEvery { api.getPolls() } returns PollRemoteMock.providePollListWrapper(listOf(remotePollName))

        val sut = PollRemoteDatasourceImpl(api, dateFormatter)
        val result = sut.getPolls()

        Assert.assertEquals(remotePollName, result[0].dataName)
    }

    @Test
    fun `given an api returning 500 when polls requested then EsmorgaException is thrown`() = runTest {
        val errorCode = 500

        val api = mockk<EsmorgaEventApi>(relaxed = true)
        coEvery { api.getPolls() } throws HttpException(Response.error<ResponseBody>(errorCode, "Error".toResponseBody("application/json".toMediaTypeOrNull())))

        val sut = PollRemoteDatasourceImpl(api, dateFormatter)

        val exception = try {
            sut.getPolls()
            null
        } catch (exception: RuntimeException) {
            exception
        }

        Assert.assertTrue(exception is EsmorgaException)
        Assert.assertEquals(errorCode, (exception as EsmorgaException).code)
    }

}