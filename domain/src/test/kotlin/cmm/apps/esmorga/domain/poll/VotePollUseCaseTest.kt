package cmm.apps.esmorga.domain.poll

import cmm.apps.esmorga.domain.event.JoinEventUseCaseImpl
import cmm.apps.esmorga.domain.event.repository.EventRepository
import cmm.apps.esmorga.domain.mock.EventDomainMock
import cmm.apps.esmorga.domain.mock.PollDomainMock
import cmm.apps.esmorga.domain.poll.repository.PollRepository
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.domain.result.Source
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class VotePollUseCaseTest {

    @Test
    fun `given a successful repository when voting poll then return success`() = runTest {
        val mockPoll = PollDomainMock.providePoll("Poll")
        val repo = mockk<PollRepository>(relaxed = true)
        coEvery { repo.votePoll(any(), any()) } returns mockPoll

        val sut = VotePollUseCaseImpl(repo)
        val result = sut.invoke("PollId", listOf("optionId"))

        Assert.assertEquals(EsmorgaResult.success(mockPoll), result)
    }

    @Test
    fun `given a failure repository when join event requested then return exception`() = runTest {
        val repo = mockk<PollRepository>(relaxed = true)
        coEvery { repo.votePoll(any(), any()) } throws EsmorgaException("Unknown error", Source.REMOTE, ErrorCodes.UNKNOWN_ERROR)

        val sut = VotePollUseCaseImpl(repo)
        val result = sut.invoke("PollId", listOf("optionId"))

        Assert.assertTrue(result.error is EsmorgaException)
    }
}