package cmm.apps.esmorga.datasource_local.event

import cmm.apps.esmorga.data.event.model.EventDataModel
import cmm.apps.esmorga.datasource_local.database.dao.EventDao
import cmm.apps.esmorga.datasource_local.event.mock.EventLocalMock
import cmm.apps.esmorga.datasource_local.event.model.EventLocalModel
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Test
import java.time.ZonedDateTime

class EventLocalDatasourceImplTest {

    private val fakeStorage = mutableListOf<String>()

    @After
    fun shutDown(){
        fakeStorage.clear()
    }

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
    fun `given an empty storage when events cached then events are stored successfully`() = runTest {
        val localEventName = "LocalEvent"

        val sut = EventLocalDatasourceImpl(provideFakeDao())
        sut.cacheEvents(listOf(EventDataModel(localEventName, ZonedDateTime.now())))
        val result = sut.getEvents()

        Assert.assertEquals(result.size, 1)
        Assert.assertEquals(result[0].dataName, localEventName)
    }

    @Test
    fun `given a storage with events when events cached then old events are removed and new events are stored successfully`() = runTest {
        val localEventName = "LocalEvent"
        fakeStorage.add("ShouldBeRemoved")

        val sut = EventLocalDatasourceImpl(provideFakeDao())
        sut.cacheEvents(listOf(EventDataModel(localEventName, ZonedDateTime.now())))
        val result = sut.getEvents()

        Assert.assertEquals(result.size, 1)
        Assert.assertEquals(result[0].dataName, localEventName)
    }

    private fun provideFakeDao(): EventDao {
        val slot = slot<List<EventLocalModel>>()
        val dao = mockk<EventDao>()
        coEvery { dao.getEvents() } coAnswers {
            fakeStorage.map { name ->
                EventLocalModel(0, name, ZonedDateTime.now(), System.currentTimeMillis())
            }
        }
        coEvery { dao.insertEvent(capture(slot)) } coAnswers {
            fakeStorage.addAll(slot.captured.map { event -> event.localName })
        }
        coEvery { dao.deleteAll() } coAnswers {
            fakeStorage.clear()
        }

        return dao
    }
}