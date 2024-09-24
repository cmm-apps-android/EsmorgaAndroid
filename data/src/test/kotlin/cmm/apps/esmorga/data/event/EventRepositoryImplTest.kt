package cmm.apps.esmorga.data.event

import cmm.apps.esmorga.data.CacheHelper
import cmm.apps.esmorga.data.event.datasource.EventDatasource
import cmm.apps.esmorga.data.mock.EventDataMock
import cmm.apps.esmorga.data.mock.UserDataMock
import cmm.apps.esmorga.data.user.datasource.UserDatasource
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.Source
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class EventRepositoryImplTest {

    private lateinit var userDS: UserDatasource
    private lateinit var localDS: EventDatasource
    private lateinit var remoteDS: EventDatasource

    @Before
    fun setUp(){
        userDS = mockk<UserDatasource>(relaxed = true)
        localDS = mockk<EventDatasource>(relaxed = true)
        remoteDS = mockk<EventDatasource>(relaxed = true)
    }

    @Test
    fun `given empty local and no user when events requested then remote events are returned`() = runTest {
        val remoteName = "RemoteEvent"

        coEvery { userDS.getUser() } throws EsmorgaException(message = "Mock Exception", source = Source.LOCAL, code = ErrorCodes.NO_DATA)
        coEvery { localDS.getEvents() } returns emptyList()
        coEvery { remoteDS.getEvents() } returns EventDataMock.provideEventDataModelList(listOf(remoteName))

        val sut = EventRepositoryImpl(userDS, localDS, remoteDS)
        val result = sut.getEvents()

        Assert.assertEquals(remoteName, result.data[0].name)
    }

    @Test
    fun `given empty local and user logged when events requested then remote events are returned`() = runTest {
        val remoteName = "RemoteEvent"

        coEvery { userDS.getUser() } returns UserDataMock.provideUserDataModel()
        coEvery { localDS.getEvents() } returns emptyList()
        coEvery { remoteDS.getEvents() } returns EventDataMock.provideEventDataModelList(listOf(remoteName))

        val sut = EventRepositoryImpl(userDS, localDS, remoteDS)
        val result = sut.getEvents()

        Assert.assertEquals(remoteName, result.data[0].name)
    }

    @Test
    fun `given empty local and user logged and joined to event when events requested then remote event with joined value is returned`() = runTest {
        val remoteName = "RemoteEvent"
        val joinedEvent = EventDataMock.provideEventDataModel(remoteName)
        val notJoinedEvent = EventDataMock.provideEventDataModel(remoteName)

        coEvery { userDS.getUser() } returns UserDataMock.provideUserDataModel()
        coEvery { localDS.getEvents() } returns emptyList()
        coEvery { remoteDS.getEvents() } returns listOf(joinedEvent, notJoinedEvent)
        coEvery { remoteDS.getMyEvents() } returns listOf(joinedEvent)

        val sut = EventRepositoryImpl(userDS, localDS, remoteDS)
        val result = sut.getEvents()

        Assert.assertEquals(remoteName, result.data[0].name)
        Assert.assertTrue(result.data.find { it.id == joinedEvent.dataId }?.userJoined == true)
    }

    @Test
    fun `given events locally cached when events requested then local events are returned`() = runTest {
        val localName = "LocalEvent"

        coEvery { localDS.getEvents() } returns EventDataMock.provideEventDataModelList(listOf(localName))
        coEvery { remoteDS.getEvents() } throws Exception()

        val sut = EventRepositoryImpl(userDS, localDS, remoteDS)
        val result = sut.getEvents()

        Assert.assertEquals(localName, result.data[0].name)
    }

    @Test
    fun `given empty local when events requested then events are stored in cache`() = runTest {
        val events = EventDataMock.provideEventDataModelList(listOf("Event"))

        coEvery { localDS.getEvents() } returns emptyList()
        coEvery { remoteDS.getEvents() } returns events

        val sut = EventRepositoryImpl(userDS, localDS, remoteDS)
        sut.getEvents()

        coVerify { localDS.cacheEvents(events) }
    }

    @Test
    fun `given no connection and expired local when events requested then local events are returned and a non blocking error is returned`() = runTest {
        val localName = "LocalEvent"
        val oldDate = System.currentTimeMillis() - (CacheHelper.DEFAULT_CACHE_TTL + 1000)

        coEvery { localDS.getEvents() } returns listOf(EventDataMock.provideEventDataModel(localName).copy(dataCreationTime = oldDate))
        coEvery { remoteDS.getEvents() } throws EsmorgaException(message = "No connection", code = ErrorCodes.NO_CONNECTION, source = Source.REMOTE)

        val sut = EventRepositoryImpl(userDS, localDS, remoteDS)
        val result = sut.getEvents()

        Assert.assertEquals(localName, result.data[0].name)
        Assert.assertEquals(ErrorCodes.NO_CONNECTION, result.nonBlockingError)
    }

}