package cmm.apps.esmorga.domain.event

import cmm.apps.esmorga.domain.event.model.CreateEventForm
import cmm.apps.esmorga.domain.event.model.EventLocation
import cmm.apps.esmorga.domain.event.model.EventType
import cmm.apps.esmorga.domain.event.repository.EventRepository
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.domain.result.Source
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class CreateEventUseCaseTest {

    @Test
    fun `given a successful repository when create event requested then return success`() = runTest {
        val repo = mockk<EventRepository>(relaxed = true)
        coEvery { repo.createEvent(any()) } returns Unit

        val sut = CreateEventUseCaseImpl(repo)
        val eventForm = CreateEventForm(
            name = "Test Event",
            description = "Test Description",
            type = EventType.PARTY,
            date = "2025-12-31T23:59:59Z",
            location = EventLocation(name = "Test Location", lat = 0.0, long = 0.0)
        )
        val result = sut.invoke(eventForm)

        Assert.assertEquals(EsmorgaResult.success(Unit), result)
    }

    @Test
    fun `given a failure repository when create event requested then return exception`() = runTest {
        val repo = mockk<EventRepository>(relaxed = true)
        coEvery { repo.createEvent(any()) } throws EsmorgaException("Unknown error", Source.REMOTE, ErrorCodes.UNKNOWN_ERROR)

        val sut = CreateEventUseCaseImpl(repo)
        val eventForm = CreateEventForm(
            name = "Test Event",
            description = "Test Description",
            type = EventType.PARTY,
            date = "2025-12-31T23:59:59Z",
            location = EventLocation(name = "Test Location", lat = 0.0, long = 0.0)
        )
        val result = sut.invoke(eventForm)

        Assert.assertTrue(result.error is EsmorgaException)
    }

    @Test
    fun `given a no connection error when create event requested then return no connection error`() = runTest {
        val repo = mockk<EventRepository>(relaxed = true)
        coEvery { repo.createEvent(any()) } throws EsmorgaException("No Connection", Source.REMOTE, ErrorCodes.NO_CONNECTION)

        val sut = CreateEventUseCaseImpl(repo)
        val eventForm = CreateEventForm(
            name = "Test Event",
            description = "Test Description",
            type = EventType.PARTY,
            date = "2025-12-31T23:59:59Z",
            location = EventLocation(name = "Test Location", lat = 0.0, long = 0.0)
        )
        val result = sut.invoke(eventForm)

        Assert.assertTrue(result.error is EsmorgaException)
        Assert.assertEquals(ErrorCodes.NO_CONNECTION, result.error?.code)
    }
}


