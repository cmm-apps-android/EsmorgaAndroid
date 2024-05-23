package cmm.apps.esmorga.datasource_local.event

import cmm.apps.esmorga.data.event.model.EventDataModel
import cmm.apps.esmorga.datasource_local.database.dao.EventDao
import cmm.apps.esmorga.datasource_local.event.mock.EventLocalMock
import cmm.apps.esmorga.datasource_local.event.model.EventLocalModel
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import java.time.ZonedDateTime

class EventLocalDatasourceImplTest {

    @Test
    fun `given a working dao when events requested then events successfully returned`() = runTest {
        val localEventName = "LocalEvent"

        val dao = mockk<EventDao>(relaxed = true)
        coEvery { dao.getEvents() } returns EventLocalMock.provideEventList(listOf(localEventName))

        val sut = EventLocalDatasourceImpl(dao)
        val result = sut.getEvents()

        Assert.assertEquals(result[0].dataName, localEventName)
    }

    @Test
    fun `given a working dao when events cached then events are stored successfully`() = runTest {
        val localEventName = "LocalEvent"
        val storage = mutableListOf<String>()
        val slot = slot<List<EventLocalModel>>()

        val dao = mockk<EventDao>()
        coEvery { dao.getEvents() } coAnswers {
            storage.map { name ->
                EventLocalModel(0, name, ZonedDateTime.now(), System.currentTimeMillis())
            }
        }
        coEvery { dao.insertEvent(capture(slot)) } coAnswers {
            storage.addAll(slot.captured.map { event -> event.localName })
        }
        coEvery { dao.deleteAll() } coAnswers {
            storage.clear()
        }

        val sut = EventLocalDatasourceImpl(dao)
        sut.cacheEvents(listOf(EventDataModel(localEventName, ZonedDateTime.now())))
        val result = sut.getEvents()

        Assert.assertEquals(result[0].dataName, localEventName)
    }
}