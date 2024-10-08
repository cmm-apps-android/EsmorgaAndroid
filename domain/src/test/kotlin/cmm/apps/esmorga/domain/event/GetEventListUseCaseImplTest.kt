package cmm.apps.esmorga.domain.event

import cmm.apps.esmorga.domain.event.repository.EventRepository
import cmm.apps.esmorga.domain.mock.EventDomainMock
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class GetEventListUseCaseImplTest {

    @Test
    fun `given a successful repository when events requested then events returned`() = runTest {
        val repoEventName = "RepoEvent"

        val repo = mockk<EventRepository>(relaxed = true)
        coEvery { repo.getEvents() } returns EventDomainMock.provideEventList(listOf(repoEventName))

        val sut = GetEventListUseCaseImpl(repo)
        val result = sut.invoke()

        Assert.assertEquals(repoEventName, result.data!![0].name)
    }
}