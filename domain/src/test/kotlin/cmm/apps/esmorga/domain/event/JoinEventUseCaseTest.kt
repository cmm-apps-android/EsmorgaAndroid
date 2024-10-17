package cmm.apps.esmorga.domain.event

import cmm.apps.esmorga.domain.event.repository.EventRepository
import cmm.apps.esmorga.domain.mock.EventDomainMock
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.domain.result.Source
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class JoinEventUseCaseTest {

    @Test
    fun `given a successful repository when join event requested then return success`() = runTest {
        val repo = mockk<EventRepository>(relaxed = true)
        val event = EventDomainMock.provideEvent("Event Name")
        coEvery { repo.joinEvent(any()) } returns Unit

        val sut = JoinEventUseCaseImpl(repo)
        val result = sut.invoke(event)

        Assert.assertEquals(EsmorgaResult.success(Unit), result)
    }

    @Test
    fun `given a failure repository when join event requested then return exception`() = runTest {
        val repo = mockk<EventRepository>(relaxed = true)
        val event = EventDomainMock.provideEvent("Event Name")
        coEvery { repo.joinEvent(any()) } throws EsmorgaException("Unknown error", Source.REMOTE, ErrorCodes.UNKNOWN_ERROR)

        val sut = JoinEventUseCaseImpl(repo)
        val result = sut.invoke(event)

        Assert.assertTrue(result.error is EsmorgaException)
    }
}