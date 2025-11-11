package cmm.apps.esmorga.data.poll

import cmm.apps.esmorga.data.CacheHelper
import cmm.apps.esmorga.data.event.EventRepositoryImpl
import cmm.apps.esmorga.data.event.datasource.EventDatasource
import cmm.apps.esmorga.data.event.mapper.toEvent
import cmm.apps.esmorga.data.event.mapper.toEventAttendee
import cmm.apps.esmorga.data.mock.EventAttendeeDataMock
import cmm.apps.esmorga.data.mock.EventDataMock
import cmm.apps.esmorga.data.mock.PollDataMock
import cmm.apps.esmorga.data.mock.UserDataMock
import cmm.apps.esmorga.data.poll.datasource.PollDatasource
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

class PollRepositoryImplTest {

    private lateinit var remoteDS: PollDatasource

    @Before
    fun setUp() {
        remoteDS = mockk<PollDatasource>(relaxed = true)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `given empty local when polls requested then remote polls are returned`() = runTest {
        val remoteName = "RemotePoll"

        coEvery { remoteDS.getPolls() } returns PollDataMock.providePollDataModelList(listOf(remoteName))

        val sut = PollRepositoryImpl(remoteDS, UnconfinedTestDispatcher())
        val result = sut.getPolls()

        Assert.assertEquals(remoteName, result[0].name)
    }

//    @OptIn(ExperimentalCoroutinesApi::class)
//    @Test(expected = EsmorgaException::class)
//    fun `given no connection when polls requested then local polls are returned and a non blocking error is returned`() = runTest {
//        val localName = "LocalEvent"
//        val oldDate = System.currentTimeMillis() - (CacheHelper.DEFAULT_CACHE_TTL + 1000)
//
//        coEvery { localDS.getEvents() } returns listOf(EventDataMock.provideEventDataModel(localName).copy(dataCreationTime = oldDate))
//        coEvery { remoteDS.getEvents() } throws EsmorgaException(message = "No connection", code = ErrorCodes.NO_CONNECTION, source = Source.REMOTE)
//
//        val sut = EventRepositoryImpl(userDS, localDS, remoteDS, UnconfinedTestDispatcher())
//        val result = sut.getEvents()
//
//        Assert.assertEquals(localName, result[0].name)
//    }

}