package cmm.apps.esmorga.domain.event

import cmm.apps.esmorga.domain.event.repository.EventRepository
import cmm.apps.esmorga.domain.mock.EventDomainMock
import cmm.apps.esmorga.domain.mock.PollDomainMock
import cmm.apps.esmorga.domain.mock.UserDomainMock
import cmm.apps.esmorga.domain.poll.repository.PollRepository
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.Source
import cmm.apps.esmorga.domain.user.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class GetEventsAndPollsUseCaseImplTest {

    val repoEventName = "RepoEvent"
    val repoPollName = "RepoPoll"

    val eventRepo = mockk<EventRepository>(relaxed = true).also{
        coEvery { it.getEvents() } returns EventDomainMock.provideEventList(listOf(repoEventName))
    }

    val pollRepo = mockk<PollRepository>(relaxed = true).also{
        coEvery { it.getPolls() } returns PollDomainMock.providePollList(listOf(repoPollName))
    }

    val userRepo = mockk<UserRepository>(relaxed = true).also{
        coEvery { it.getUser() } returns UserDomainMock.provideUser()
    }

    @Test
    fun `given successful repositories when events and polls requested then events and polls returned`() = runTest {
        val sut = GetEventListUseCaseImpl(eventRepo, pollRepo, userRepo)
        val result = sut.invoke()

        Assert.assertEquals(repoEventName, result.data!!.first[0].name)
        Assert.assertEquals(repoPollName, result.data.second[0].name)
    }

    @Test
    fun `given no connection and successful repositories when events and polls requested then non blocking error with local data returned`() = runTest {
        val eventRepo = mockk<EventRepository>(relaxed = true)
        coEvery { eventRepo.getEvents(forceLocal = false) } throws EsmorgaException(message = "No connection error", source = Source.REMOTE, code = ErrorCodes.NO_CONNECTION)
        coEvery { eventRepo.getEvents(forceLocal = true) } returns EventDomainMock.provideEventList(listOf(repoEventName))

        val pollRepo = mockk<PollRepository>(relaxed = true)
        coEvery { pollRepo.getPolls(forceLocal = false) } throws EsmorgaException(message = "No connection error", source = Source.REMOTE, code = ErrorCodes.NO_CONNECTION)
        coEvery { pollRepo.getPolls(forceLocal = true) } returns PollDomainMock.providePollList(listOf(repoPollName))

        val sut = GetEventListUseCaseImpl(eventRepo, pollRepo, userRepo)
        val result = sut.invoke()

        Assert.assertEquals(repoEventName, result.data!!.first[0].name)
        Assert.assertEquals(repoPollName, result.data.second[0].name)
        Assert.assertTrue("NO_CONNECTION should be returned", result.error?.code == ErrorCodes.NO_CONNECTION)
    }

    @Test
    fun `given successful event repository and broken poll repository when events and polls requested then error is returned`() = runTest {
        val pollRepo = mockk<PollRepository>(relaxed = true)
        coEvery { pollRepo.getPolls() } throws EsmorgaException(message = "No connection error", source = Source.REMOTE, code = ErrorCodes.UNKNOWN_ERROR)

        val sut = GetEventListUseCaseImpl(eventRepo, pollRepo, userRepo)
        val result = sut.invoke()

        Assert.assertNotNull("Exception should be returned",result.error)
    }

    @Test
    fun `given broken event repository and successful poll repository when events and polls requested then error is returned`() = runTest {
        val eventRepo = mockk<EventRepository>(relaxed = true)
        coEvery { eventRepo.getEvents() } throws EsmorgaException(message = "No connection error", source = Source.REMOTE, code = ErrorCodes.UNKNOWN_ERROR)

        val sut = GetEventListUseCaseImpl(eventRepo, pollRepo, userRepo)
        val result = sut.invoke()

        Assert.assertNotNull("Exception should be returned",result.error)
    }
}