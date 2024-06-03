package cmm.apps.esmorga.data.event

import cmm.apps.esmorga.data.event.datasource.EventDatasource
import cmm.apps.esmorga.data.mock.EventDataMock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
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

        assertEquals(remoteName, result[0].name)
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

}