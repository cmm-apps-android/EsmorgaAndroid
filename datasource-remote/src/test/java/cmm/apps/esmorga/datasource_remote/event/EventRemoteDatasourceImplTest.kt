package cmm.apps.esmorga.datasource_remote.event

import cmm.apps.esmorga.datasource_remote.api.EsmorgaApi
import cmm.apps.esmorga.datasource_remote.api.EsmorgaGuestApi
import cmm.apps.esmorga.datasource_remote.event.mapper.toEventDataModel
import cmm.apps.esmorga.datasource_remote.event.model.EventListWrapperRemoteModel
import cmm.apps.esmorga.datasource_remote.mock.EventRemoteMock
import cmm.apps.esmorga.datasource_remote.mock.EventRemoteMock.provideEvent
import cmm.apps.esmorga.domain.result.ErrorCodes
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

class EventRemoteDatasourceImplTest {

    @Test
    fun `given a working api when events requested then event list is successfully returned`() = runTest {
        val remoteEventName = "RemoteEvent"

        val api = mockk<EsmorgaApi>(relaxed = true)
        val guestApi = mockk<EsmorgaGuestApi>(relaxed = true)
        coEvery { guestApi.getEvents() } returns EventRemoteMock.provideEventListWrapper(listOf(remoteEventName))

        val sut = EventRemoteDatasourceImpl(api, guestApi)
        val result = sut.getEvents()

        Assert.assertEquals(remoteEventName, result[0].dataName)
    }

    @Test
    fun `given an api returning 500 when events requested then EsmorgaException is thrown`() = runTest {
        val errorCode = 500

        val api = mockk<EsmorgaApi>(relaxed = true)
        val guestApi = mockk<EsmorgaGuestApi>(relaxed = true)
        coEvery { guestApi.getEvents() } throws HttpException(Response.error<ResponseBody>(errorCode, "Error".toResponseBody("application/json".toMediaTypeOrNull())))

        val sut = EventRemoteDatasourceImpl(api, guestApi)

        val exception = try {
            sut.getEvents()
            null
        } catch (exception: RuntimeException) {
            exception
        }

        Assert.assertTrue(exception is EsmorgaException)
        Assert.assertEquals(errorCode, (exception as EsmorgaException).code)
    }

    @Test
    fun `given an api with an event with wrong type when events requested then Exception is thrown`() = runTest {
        val remoteEventName = "RemoteEvent"
        val wrongTypeEvent = EventRemoteMock.provideEvent(remoteEventName).copy(remoteType = "ERROR")

        val api = mockk<EsmorgaApi>(relaxed = true)
        val guestApi = mockk<EsmorgaGuestApi>(relaxed = true)
        coEvery { guestApi.getEvents() } returns EventListWrapperRemoteModel(1, listOf(wrongTypeEvent))

        val sut = EventRemoteDatasourceImpl(api, guestApi)

        val exception = try {
            sut.getEvents()
            null
        } catch (exception: RuntimeException) {
            exception
        }

        Assert.assertTrue(exception is EsmorgaException)
        Assert.assertEquals(ErrorCodes.PARSE_ERROR, (exception as EsmorgaException).code)
    }

    @Test
    fun `given an api with an event with wrong formatted date when events requested then Exception is thrown`() = runTest {
        val remoteEventName = "RemoteEvent"
        val wrongTypeEvent = EventRemoteMock.provideEvent(remoteEventName).copy(remoteDate = "ERROR")

        val api = mockk<EsmorgaApi>(relaxed = true)
        val guestApi = mockk<EsmorgaGuestApi>(relaxed = true)
        coEvery { guestApi.getEvents() } returns EventListWrapperRemoteModel(1, listOf(wrongTypeEvent))

        val sut = EventRemoteDatasourceImpl(api, guestApi)
        val exception = try {
            sut.getEvents()
            null
        } catch (exception: RuntimeException) {
            exception
        }

        Assert.assertTrue(exception is EsmorgaException)
        Assert.assertEquals(ErrorCodes.PARSE_ERROR, (exception as EsmorgaException).code)
    }

    @Test
    fun `given a working api when my events requested then event list is successfully returned`() = runTest {
        val remoteEventName = "RemoteEvent"

        val api = mockk<EsmorgaApi>(relaxed = true)
        val guestApi = mockk<EsmorgaGuestApi>(relaxed = true)
        coEvery { api.getMyEvents() } returns EventRemoteMock.provideEventListWrapper(listOf(remoteEventName))

        val sut = EventRemoteDatasourceImpl(api, guestApi)
        val result = sut.getMyEvents()

        Assert.assertEquals(remoteEventName, result[0].dataName)
    }

    @Test
    fun `given an api returning 500 when my events requested then EsmorgaException is thrown`() = runTest {
        val errorCode = 500

        val api = mockk<EsmorgaApi>(relaxed = true)
        val guestApi = mockk<EsmorgaGuestApi>(relaxed = true)

        coEvery { api.getMyEvents() } throws HttpException(Response.error<ResponseBody>(errorCode, "Error".toResponseBody("application/json".toMediaTypeOrNull())))
        val sut = EventRemoteDatasourceImpl(api, guestApi)

        val exception = try {
            sut.getMyEvents()
            null
        } catch (exception: RuntimeException) {
            exception
        }

        Assert.assertTrue(exception is EsmorgaException)
        Assert.assertEquals(errorCode, (exception as EsmorgaException).code)
    }

    @Test
    fun `given a working api when join event requested successfully then return Unit`() = runTest {
        val api = mockk<EsmorgaApi>(relaxed = true)
        val guestApi = mockk<EsmorgaGuestApi>(relaxed = true)
        coEvery { api.joinEvent(any()) } returns Unit

        val sut = EventRemoteDatasourceImpl(api, guestApi)
        val result = sut.joinEvent(provideEvent("Remote Event").toEventDataModel())

        coVerify { api.joinEvent(any()) }
        Assert.assertEquals(Unit, result)
    }

    @Test
    fun `given an api returning a 400 when join event requested then Exception is thrown`() = runTest {
        val errorCode = 400
        val api = mockk<EsmorgaApi>(relaxed = true)
        val guestApi = mockk<EsmorgaGuestApi>(relaxed = true)
        coEvery { api.joinEvent(any()) } throws HttpException(Response.error<ResponseBody>(errorCode, "Error".toResponseBody("application/json".toMediaTypeOrNull())))

        val sut = EventRemoteDatasourceImpl(api, guestApi)
        val exception = try {
            sut.joinEvent(provideEvent("Remote Event").toEventDataModel())
        } catch (exception: RuntimeException) {
            exception
        }

        Assert.assertTrue(exception is EsmorgaException)
        Assert.assertEquals(errorCode, (exception as EsmorgaException).code)
    }

    @Test
    fun `given a working api when leave event requested successfully then return Unit`() = runTest {
        val api = mockk<EsmorgaApi>(relaxed = true)
        val guestApi = mockk<EsmorgaGuestApi>(relaxed = true)
        coEvery { api.leaveEvent(any()) } returns Unit

        val sut = EventRemoteDatasourceImpl(api, guestApi)
        val result = sut.leaveEvent(provideEvent("Remote Event").toEventDataModel())

        coVerify { api.leaveEvent(any()) }
        Assert.assertEquals(Unit, result)
    }

    @Test
    fun `given an api returning a 400 when leave event requested then Exception is thrown`() = runTest {
        val errorCode = 400
        val api = mockk<EsmorgaApi>(relaxed = true)
        val guestApi = mockk<EsmorgaGuestApi>(relaxed = true)
        coEvery { api.leaveEvent(any()) } throws HttpException(Response.error<ResponseBody>(errorCode, "Error".toResponseBody("application/json".toMediaTypeOrNull())))

        val sut = EventRemoteDatasourceImpl(api, guestApi)
        val exception = try {
            sut.leaveEvent(provideEvent("Remote Event").toEventDataModel())
        } catch (exception: RuntimeException) {
            exception
        }

        Assert.assertTrue(exception is EsmorgaException)
        Assert.assertEquals(errorCode, (exception as EsmorgaException).code)
    }
}