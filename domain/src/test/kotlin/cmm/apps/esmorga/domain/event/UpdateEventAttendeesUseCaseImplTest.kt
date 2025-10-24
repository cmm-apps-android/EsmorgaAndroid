package cmm.apps.esmorga.domain.event

import cmm.apps.esmorga.domain.event.repository.EventRepository
import cmm.apps.esmorga.domain.mock.EventAttendeeDomainMock
import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.domain.result.Success
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class UpdateEventAttendeesUseCaseImplTest {

    @Test
    fun `given a successful repository when event attendees are updated then success returned`() = runTest {
        val repo = mockk<EventRepository>(relaxed = true)

        val sut = UpdateEventAttendeesUseCaseImpl(repo)
        val result = sut.invoke(EventAttendeeDomainMock.provideAttendee("RepoAttendee"))

        Assert.assertEquals(EsmorgaResult.success(Unit), result)
    }

    @Test
    fun `given a failing repository when event attendees are updated then error returned`() = runTest {
        val exception = Exception()

        val repo = mockk<EventRepository>(relaxed = true)
        coEvery { repo.updateEventAttendee(any()) } throws exception

        val sut = UpdateEventAttendeesUseCaseImpl(repo)
        val result = sut.invoke(EventAttendeeDomainMock.provideAttendee("RepoAttendee"))

        Assert.assertNull(result.data)
        Assert.assertNotNull(result.error)
    }
}