package cmm.apps.esmorga.data.event

import cmm.apps.esmorga.data.CacheHelper
import cmm.apps.esmorga.data.event.datasource.EventDatasource
import cmm.apps.esmorga.data.mock.EventDataMock
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.Source
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class EventRepositoryImplTest {

    @Test
    fun `given empty local when events requested then remote events are returned`() = runTest {
        val remoteName = "RemoteEvent"

        val localDS = mockk<EventDatasource>(relaxed = true)
        coEvery { localDS.getEvents() } returns emptyList()

        val remoteDS = mockk<EventDatasource>()
        coEvery { remoteDS.getEvents() } returns EventDataMock.provideEventDataModelList(listOf(remoteName))

        val sut = EventRepositoryImpl(localDS, remoteDS)
        val result = sut.getEvents()

        Assert.assertEquals(remoteName, result.data[0].name)
    }

    @Test
    fun `given events locally cached when events requested then local events are returned`() = runTest {
        val localName = "LocalEvent"

        val localDS = mockk<EventDatasource>(relaxed = true)
        coEvery { localDS.getEvents() } returns EventDataMock.provideEventDataModelList(listOf(localName))

        val remoteDS = mockk<EventDatasource>()
        coEvery { remoteDS.getEvents() } throws Exception()

        val sut = EventRepositoryImpl(localDS, remoteDS)
        val result = sut.getEvents()

        Assert.assertEquals(localName, result.data[0].name)
    }

    @Test
    fun `given empty local when events requested then events are stored in cache`() = runTest {
        val events = EventDataMock.provideEventDataModelList(listOf("Event"))

        val localDS = mockk<EventDatasource>(relaxed = true)
        coEvery { localDS.getEvents() } returns emptyList()

        val remoteDS = mockk<EventDatasource>()
        coEvery { remoteDS.getEvents() } returns events

        val sut = EventRepositoryImpl(localDS, remoteDS)
        sut.getEvents()

        coVerify { localDS.cacheEvents(events) }
    }

    @Test
    fun `given no connection and expired local when events requested then local events are returned and a non blocking error is returned`() = runTest {
        val localName = "LocalEvent"
        val oldDate = System.currentTimeMillis() - (CacheHelper.DEFAULT_CACHE_TTL + 1000)

        val localDS = mockk<EventDatasource>(relaxed = true)
        coEvery { localDS.getEvents() } returns listOf(EventDataMock.provideEventDataModel(localName).copy(dataCreationTime = oldDate))

        val remoteDS = mockk<EventDatasource>()
        coEvery { remoteDS.getEvents() } throws EsmorgaException(message = "No connection", code = ErrorCodes.NO_CONNECTION, source = Source.REMOTE)

        val sut = EventRepositoryImpl(localDS, remoteDS)
        val result = sut.getEvents()

        Assert.assertEquals(localName, result.data[0].name)
        Assert.assertEquals(ErrorCodes.NO_CONNECTION, result.nonBlockingError)
    }

}