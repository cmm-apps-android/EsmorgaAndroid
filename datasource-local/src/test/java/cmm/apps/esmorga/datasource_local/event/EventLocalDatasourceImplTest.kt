package cmm.apps.esmorga.datasource_local.event

import cmm.apps.esmorga.datasource_local.database.dao.EventDao
import cmm.apps.esmorga.datasource_local.event.mapper.toEventDataModelList
import cmm.apps.esmorga.datasource_local.event.model.EventLocalModel
import cmm.apps.esmorga.datasource_local.mock.EventLocalMock
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Test

class EventLocalDatasourceImplTest {

    private val fakeStorage = mutableListOf<String>()

    private fun provideFakeDao(): EventDao {
        val slot = slot<List<EventLocalModel>>()
        val dao = mockk<EventDao>()
        coEvery { dao.getEvents() } coAnswers {
            fakeStorage.map { name ->
                EventLocalMock.provideEvent(name)
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

    @After
    fun shutDown() {
        fakeStorage.clear()
    }

    @Test
    fun `given a working dao when events requested then events successfully returned`() = runTest {
        val localEventName = "LocalEvent"

        val dao = mockk<EventDao>(relaxed = true)
        coEvery { dao.getEvents() } returns EventLocalMock.provideEventList(listOf(localEventName))

        val sut = EventLocalDatasourceImpl(dao)
        val result = sut.getEvents()

        Assert.assertEquals(localEventName, result[0].dataName)
    }

    @Test
    fun `given an empty storage when events cached then events are stored successfully`() = runTest {
        val localEventName = "LocalEvent"

        val sut = EventLocalDatasourceImpl(provideFakeDao())
        sut.cacheEvents(EventLocalMock.provideEventList(listOf(localEventName)).toEventDataModelList())
        val result = sut.getEvents()

        Assert.assertEquals(1, result.size)
        Assert.assertEquals(localEventName, result[0].dataName)
    }

    @Test
    fun `given a storage with events when events cached then old events are removed and new events are stored successfully`() = runTest {
        val localEventName = "LocalEvent"
        fakeStorage.add("ShouldBeRemoved")

        val sut = EventLocalDatasourceImpl(provideFakeDao())
        sut.cacheEvents(EventLocalMock.provideEventList(listOf(localEventName)).toEventDataModelList())
        val result = sut.getEvents()

        Assert.assertEquals(1, result.size)
        Assert.assertEquals(localEventName, result[0].dataName)
    }

}