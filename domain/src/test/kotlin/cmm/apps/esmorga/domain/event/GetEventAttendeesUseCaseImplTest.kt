package cmm.apps.esmorga.domain.event

import cmm.apps.esmorga.domain.event.repository.EventRepository
import cmm.apps.esmorga.domain.mock.EventAttendeeDomainMock
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class GetEventAttendeesUseCaseImplTest {

    @Test
    fun `given a successful repository when event attendees requested then attendees returned`() = runTest {
        val eventName = "EventName"
        val repoAttendeeName = "RepoAttendee"

        val repo = mockk<EventRepository>(relaxed = true)
        coEvery { repo.getEventAttendees(eventName) } returns EventAttendeeDomainMock.provideEventAttendeeList(listOf(repoAttendeeName))

        val sut = GetEventAttendeesUseCaseImpl(repo)
        val result = sut.invoke(eventName)

        Assert.assertEquals(repoAttendeeName, result.data!![0].name)
    }
}