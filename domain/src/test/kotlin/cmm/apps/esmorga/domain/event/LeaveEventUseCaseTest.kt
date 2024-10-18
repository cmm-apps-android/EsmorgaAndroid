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

class LeaveEventUseCaseTest {

    @Test
    fun `given a successful repository when leave event requested then return success`() = runTest {
        val repo = mockk<EventRepository>(relaxed = true)
        coEvery { repo.leaveEvent(any()) } returns Unit

        val sut = LeaveEventUseCaseImpl(repo)
        val result = sut.invoke(EventDomainMock.provideEvent("Event Name"))

        Assert.assertEquals(EsmorgaResult.success(Unit), result)
    }

    @Test
    fun `given a failure repository when leave event requested then return exception`() = runTest {
        val repo = mockk<EventRepository>(relaxed = true)
        coEvery { repo.leaveEvent(any()) } throws EsmorgaException("Unknown error", Source.REMOTE, ErrorCodes.UNKNOWN_ERROR)

        val sut = LeaveEventUseCaseImpl(repo)
        val result = sut.invoke(EventDomainMock.provideEvent("Event Name"))

        Assert.assertTrue(result.error is EsmorgaException)
    }

    @Test
    fun `given a no network available repository when leave event requested then return no connection exception`() = runTest {
        val repo = mockk<EventRepository>(relaxed = true)
        coEvery { repo.leaveEvent(any()) } throws EsmorgaException("No Connection", Source.REMOTE, ErrorCodes.NO_CONNECTION)

        val sut = LeaveEventUseCaseImpl(repo)
        val result = sut.invoke(EventDomainMock.provideEvent("Event Name"))

        Assert.assertTrue(result.error is EsmorgaException)
        Assert.assertEquals(ErrorCodes.NO_CONNECTION, result.error?.code)
    }
}