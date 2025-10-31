package cmm.apps.esmorga.data.event

import cmm.apps.esmorga.data.CacheHelper
import cmm.apps.esmorga.data.event.datasource.EventDatasource
import cmm.apps.esmorga.data.event.mapper.toEvent
import cmm.apps.esmorga.data.event.mapper.toEventAttendee
import cmm.apps.esmorga.data.mock.EventAttendeeDataMock
import cmm.apps.esmorga.data.mock.EventDataMock
import cmm.apps.esmorga.data.mock.UserDataMock
import cmm.apps.esmorga.data.user.datasource.UserDatasource
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.Source
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class EventRepositoryImplTest {

    private lateinit var userDS: UserDatasource
    private lateinit var localDS: EventDatasource
    private lateinit var remoteDS: EventDatasource

    @Before
    fun setUp() {
        userDS = mockk<UserDatasource>(relaxed = true)
        localDS = mockk<EventDatasource>(relaxed = true)
        remoteDS = mockk<EventDatasource>(relaxed = true)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `given empty local and no user when events requested then remote events are returned`() = runTest {
        val remoteName = "RemoteEvent"

        coEvery { userDS.getUser() } throws EsmorgaException(message = "Mock Exception", source = Source.LOCAL, code = ErrorCodes.NO_DATA)
        coEvery { localDS.getEvents() } returns emptyList()
        coEvery { remoteDS.getEvents() } returns EventDataMock.provideEventDataModelList(listOf(remoteName))

        val sut = EventRepositoryImpl(userDS, localDS, remoteDS, UnconfinedTestDispatcher())
        val result = sut.getEvents()

        Assert.assertEquals(remoteName, result[0].name)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `given empty local and user logged when events requested then remote events are returned`() = runTest {
        val remoteName = "RemoteEvent"

        coEvery { userDS.getUser() } returns UserDataMock.provideUserDataModel()
        coEvery { localDS.getEvents() } returns emptyList()
        coEvery { remoteDS.getEvents() } returns EventDataMock.provideEventDataModelList(listOf(remoteName))

        val sut = EventRepositoryImpl(userDS, localDS, remoteDS, UnconfinedTestDispatcher())
        val result = sut.getEvents()

        Assert.assertEquals(remoteName, result[0].name)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `given empty local and user logged and joined to event when events requested then remote event with joined value is returned`() = runTest {
        val remoteName = "RemoteEvent"
        val joinedEvent = EventDataMock.provideEventDataModel(remoteName)
        val notJoinedEvent = EventDataMock.provideEventDataModel(remoteName)

        coEvery { userDS.getUser() } returns UserDataMock.provideUserDataModel()
        coEvery { localDS.getEvents() } returns emptyList()
        coEvery { remoteDS.getEvents() } returns listOf(joinedEvent, notJoinedEvent)
        coEvery { remoteDS.getMyEvents() } returns listOf(joinedEvent)

        val sut = EventRepositoryImpl(userDS, localDS, remoteDS, UnconfinedTestDispatcher())
        val result = sut.getEvents()

        Assert.assertEquals(remoteName, result[0].name)
        Assert.assertTrue(result.find { it.id == joinedEvent.dataId }?.userJoined == true)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `given events locally cached when events requested then local events are returned`() = runTest {
        val localName = "LocalEvent"

        coEvery { localDS.getEvents() } returns EventDataMock.provideEventDataModelList(listOf(localName))
        coEvery { remoteDS.getEvents() } throws Exception()

        val sut = EventRepositoryImpl(userDS, localDS, remoteDS, UnconfinedTestDispatcher())
        val result = sut.getEvents()

        Assert.assertEquals(localName, result[0].name)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `given empty local when events requested then events are stored in cache`() = runTest {
        val events = EventDataMock.provideEventDataModelList(listOf("Event"))

        coEvery { localDS.getEvents() } returns emptyList()
        coEvery { remoteDS.getEvents() } returns events

        val sut = EventRepositoryImpl(userDS, localDS, remoteDS, UnconfinedTestDispatcher())
        sut.getEvents()

        coVerify { localDS.cacheEvents(events) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test(expected = EsmorgaException::class)
    fun `given no connection and expired local when events requested then local events are returned and a non blocking error is returned`() = runTest {
        val localName = "LocalEvent"
        val oldDate = System.currentTimeMillis() - (CacheHelper.DEFAULT_CACHE_TTL + 1000)

        coEvery { localDS.getEvents() } returns listOf(EventDataMock.provideEventDataModel(localName).copy(dataCreationTime = oldDate))
        coEvery { remoteDS.getEvents() } throws EsmorgaException(message = "No connection", code = ErrorCodes.NO_CONNECTION, source = Source.REMOTE)

        val sut = EventRepositoryImpl(userDS, localDS, remoteDS, UnconfinedTestDispatcher())
        val result = sut.getEvents()

        Assert.assertEquals(localName, result[0].name)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `given working remote when event attendees requested then remote attendees are returned`() = runTest {
        val eventName = "RemoteEvent"
        val remoteAttendeeName = "RemoteAttendee"

        coEvery { remoteDS.getEventAttendees(eventName) } returns EventAttendeeDataMock.provideEventAttendeeDataModelList(listOf(remoteAttendeeName))

        val sut = EventRepositoryImpl(userDS, localDS, remoteDS, UnconfinedTestDispatcher())
        val result = sut.getEventAttendees(eventName)

        Assert.assertEquals(remoteAttendeeName, result[0].name)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `given working remote and local when event attendees requested then attendees with paid mark are returned`() = runTest {
        val eventName = "RemoteEvent"
        val remoteGoodAttendeeName = "GoodAttendee"
        val remoteBadAttendeeName = "BadAttendee"

        coEvery { remoteDS.getEventAttendees(eventName) } returns EventAttendeeDataMock.provideEventAttendeeDataModelList(listOf(remoteGoodAttendeeName, remoteBadAttendeeName))
        coEvery { localDS.getEventAttendees(eventName) } returns listOf(EventAttendeeDataMock.provideEventAttendeeDataModel(remoteGoodAttendeeName, true))

        val sut = EventRepositoryImpl(userDS, localDS, remoteDS, UnconfinedTestDispatcher())
        val result = sut.getEventAttendees(eventName)

        Assert.assertEquals(remoteGoodAttendeeName, result[0].name)
        Assert.assertTrue(result[0].alreadyPaid)
        Assert.assertFalse(result[1].alreadyPaid)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `given events locally cached when join event is requested then local events are updated`() = runTest {
        val localEvents = listOf(EventDataMock.provideEventDataModel("localName"))
        val event = localEvents.first()

        coEvery { localDS.getEvents() } returns localEvents
        coEvery { remoteDS.joinEvent(any()) } returns Unit

        val sut = EventRepositoryImpl(userDS, localDS, remoteDS, UnconfinedTestDispatcher())
        sut.joinEvent(event.toEvent())

        coVerify { remoteDS.joinEvent(any()) }
        coVerify { localDS.joinEvent(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `given events locally cached when leave event is requested then local events are updated`() = runTest {
        val localEvents = listOf(EventDataMock.provideEventDataModel("localName", userJoined = true))
        val event = localEvents.first()

        coEvery { localDS.getEvents() } returns localEvents
        coEvery { remoteDS.leaveEvent(any()) } returns Unit

        val sut = EventRepositoryImpl(userDS, localDS, remoteDS, UnconfinedTestDispatcher())
        sut.leaveEvent(event.toEvent())

        coVerify { remoteDS.leaveEvent(any()) }
        coVerify { localDS.leaveEvent(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `given working local when event attendee updated then local attendee is stored`() = runTest {
        val attendeeName = "RemoteAttendee"

        coEvery { localDS.updateAttendee(any()) } returns Unit

        val sut = EventRepositoryImpl(userDS, localDS, remoteDS, UnconfinedTestDispatcher())
        sut.updateEventAttendee(EventAttendeeDataMock.provideEventAttendeeDataModel(attendeeName).toEventAttendee())

        coVerify { localDS.updateAttendee(any()) }
    }

}