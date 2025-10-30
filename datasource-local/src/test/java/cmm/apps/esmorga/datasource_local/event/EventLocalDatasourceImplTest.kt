package cmm.apps.esmorga.datasource_local.event

import cmm.apps.esmorga.data.event.model.EventDataModel
import cmm.apps.esmorga.datasource_local.database.dao.EventAttendeeDao
import cmm.apps.esmorga.datasource_local.database.dao.EventDao
import cmm.apps.esmorga.datasource_local.event.mapper.toEventAttendeeDataModel
import cmm.apps.esmorga.datasource_local.event.mapper.toEventDataModel
import cmm.apps.esmorga.datasource_local.event.mapper.toEventDataModelList
import cmm.apps.esmorga.datasource_local.event.mapper.toEventLocalModel
import cmm.apps.esmorga.datasource_local.event.model.EventAttendeeLocalModel
import cmm.apps.esmorga.datasource_local.event.model.EventLocalModel
import cmm.apps.esmorga.datasource_local.mock.EventAttendeeLocalMock
import cmm.apps.esmorga.datasource_local.mock.EventLocalMock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Test

class EventLocalDatasourceImplTest {

    private val fakeEventStorage = mutableListOf<String>()
    private val fakeAttendeeStorage = mutableListOf<String>()

    private fun provideFakeEventDao(): EventDao {
        val eventListSlot = slot<List<EventLocalModel>>()
        val singleEventSlot = slot<String>()
        val dao = mockk<EventDao>()
        coEvery { dao.getEvents() } coAnswers {
            fakeEventStorage.map { name ->
                EventLocalMock.provideEvent(name)
            }
        }
        coEvery { dao.insertEvents(capture(eventListSlot)) } coAnswers {
            fakeEventStorage.addAll(eventListSlot.captured.map { event -> event.localName })
        }
        coEvery { dao.deleteAll() } coAnswers {
            fakeEventStorage.clear()
        }

        coEvery { dao.getEventById(capture(singleEventSlot)) } coAnswers {
            EventLocalMock.provideEvent(fakeEventStorage.find { it == singleEventSlot.captured }!!)
        }

        return dao
    }

    private fun provideFakeAttendeeDao(): EventAttendeeDao {
        val singleAttendeeSlot = slot<EventAttendeeLocalModel>()
        val dao = mockk<EventAttendeeDao>()
        coEvery { dao.getEventAttendees(any()) } coAnswers {
            fakeAttendeeStorage.map { name ->
                EventAttendeeLocalMock.provideAttendee(name = name)
            }
        }
        coEvery { dao.insertAttendee(capture(singleAttendeeSlot)) } coAnswers {
            fakeAttendeeStorage.add(singleAttendeeSlot.captured.localName)
        }

        return dao
    }

    @After
    fun shutDown() {
        fakeEventStorage.clear()
    }

    @Test
    fun `given a working dao when events requested then events successfully returned`() = runTest {
        val localEventName = "LocalEvent"

        val dao = mockk<EventDao>(relaxed = true)
        coEvery { dao.getEvents() } returns EventLocalMock.provideEventList(listOf(localEventName))

        val sut = EventLocalDatasourceImpl(dao, provideFakeAttendeeDao())
        val result = sut.getEvents()

        Assert.assertEquals(localEventName, result[0].dataName)
    }

    @Test
    fun `given an empty storage when events cached then events are stored successfully`() = runTest {
        val localEventName = "LocalEvent"

        val sut = EventLocalDatasourceImpl(provideFakeEventDao(), provideFakeAttendeeDao())
        sut.cacheEvents(EventLocalMock.provideEventList(listOf(localEventName)).toEventDataModelList())
        val result = sut.getEvents()

        Assert.assertEquals(1, result.size)
        Assert.assertEquals(localEventName, result[0].dataName)
    }

    @Test
    fun `given a storage with events when events cached then old events are removed and new events are stored successfully`() = runTest {
        val localEventName = "LocalEvent"
        fakeEventStorage.add("ShouldBeRemoved")

        val sut = EventLocalDatasourceImpl(provideFakeEventDao(),provideFakeAttendeeDao())
        sut.cacheEvents(EventLocalMock.provideEventList(listOf(localEventName)).toEventDataModelList())
        val result = sut.getEvents()

        Assert.assertEquals(1, result.size)
        Assert.assertEquals(localEventName, result[0].dataName)
    }

    @Test
    fun `given a storage with events when single event is requested then is returned successfully`() = runTest {
        val localEventName = "LocalEvent"
        fakeEventStorage.add(localEventName)

        val sut = EventLocalDatasourceImpl(provideFakeEventDao(), provideFakeAttendeeDao())
        val result = sut.getEventById(localEventName)

        Assert.assertEquals(localEventName, result.dataName)
    }

    @Test
    fun `given a storage with events when delete cached events is requested then the list is empty`() = runTest {
        val localEventName = "LocalEvent"
        lateinit var result: List<EventDataModel>

        val sut = EventLocalDatasourceImpl(provideFakeEventDao(), provideFakeAttendeeDao())
        sut.cacheEvents(EventLocalMock.provideEventList(listOf(localEventName)).toEventDataModelList())
        result = sut.getEvents()

        Assert.assertEquals(localEventName, result[0].dataName)
        sut.deleteCacheEvents()
        result = sut.getEvents()
        Assert.assertEquals(emptyList<EventDataModel>(), result)
    }

    @Test
    fun `given a storage with events when join event is called then old event are updated with new value`() = runTest {
        val localEventName = "LocalEvent"
        val localEvent = EventLocalMock.provideEvent(localEventName)
        val localEvents = listOf(localEvent)
        val dao = mockk<EventDao>(relaxed = true)
        coEvery { dao.getEvents() } returns localEvents

        val sut = EventLocalDatasourceImpl(dao, provideFakeAttendeeDao())
        sut.joinEvent(localEvent.toEventDataModel())

        coVerify { dao.updateEvent(localEvent.copy(localUserJoined = true, localCurrentAttendeeCount = 1)) }
    }

    @Test
    fun `given a storage with events when events leave event is called then old events are updated with new value`() = runTest {
        val localEventName = "LocalEvent"
        val localEvent = EventLocalMock.provideEvent(localEventName, true)
        val localEvents = listOf(localEvent)
        val dao = mockk<EventDao>(relaxed = true)
        coEvery { dao.getEvents() } returns localEvents

        val sut = EventLocalDatasourceImpl(dao, provideFakeAttendeeDao())
        sut.leaveEvent(localEvent.toEventDataModel())

        coVerify { dao.updateEvent(localEvent.copy(localUserJoined = false, localCurrentAttendeeCount = 0)) }
    }

    @Test
    fun `given a working dao when attendees requested then attendees successfully returned`() = runTest {
        val localAttendeeName = "LocalAttendee"
        val eventId = "event"

        val dao = mockk<EventAttendeeDao>(relaxed = true)
        coEvery { dao.getEventAttendees(eventId) } returns EventAttendeeLocalMock.provideAttendeeList(listOf(localAttendeeName))

        val sut = EventLocalDatasourceImpl(provideFakeEventDao(), dao)
        val result = sut.getEventAttendees(eventId)

        Assert.assertEquals(localAttendeeName, result[0].dataName)
    }

    @Test
    fun `given an empty storage when attendee updated then data is stored successfully`() = runTest {
        val localAttendeeName = "LocalAttendee"
        val eventId = "event"

        val sut = EventLocalDatasourceImpl(provideFakeEventDao(), provideFakeAttendeeDao())
        sut.updateAttendee(EventAttendeeLocalMock.provideAttendee(eventId = eventId, name = localAttendeeName).toEventAttendeeDataModel())
        val result = sut.getEventAttendees(eventId)

        Assert.assertEquals(1, result.size)
        Assert.assertEquals(localAttendeeName, result[0].dataName)
    }

}