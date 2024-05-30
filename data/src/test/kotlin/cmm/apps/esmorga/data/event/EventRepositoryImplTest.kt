package cmm.apps.esmorga.data.event

import cmm.apps.esmorga.data.event.datasource.EventDatasource
import cmm.apps.esmorga.data.mock.EventDataMock
import io.mockk.coEvery
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

}