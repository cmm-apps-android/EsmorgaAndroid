package cmm.apps.esmorga.domain.poll

import cmm.apps.esmorga.domain.event.GetEventListUseCaseImpl
import cmm.apps.esmorga.domain.event.repository.EventRepository
import cmm.apps.esmorga.domain.mock.EventDomainMock
import cmm.apps.esmorga.domain.mock.PollDomainMock
import cmm.apps.esmorga.domain.poll.repository.PollRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class GetPollListUseCaseImplTest {

    @Test
    fun `given a successful repository when polls requested then polls returned`() = runTest {
        val repoPollName = "RepoPoll"

        val repo = mockk<PollRepository>(relaxed = true)
        coEvery { repo.getPolls() } returns PollDomainMock.providePollList(listOf(repoPollName))

        val sut = GetPollListUseCaseImpl(repo)
        val result = sut.invoke()

        Assert.assertEquals(repoPollName, result.data!![0].name)
    }
}