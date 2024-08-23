package cmm.apps.esmorga.view.viewmodel.eventdetails

import cmm.apps.esmorga.domain.event.GetEventDetailsUseCase
import cmm.apps.esmorga.domain.result.Success
import cmm.apps.esmorga.view.eventdetails.EventDetailsViewModel
import cmm.apps.esmorga.view.viewmodel.mock.EventViewMock
import cmm.apps.esmorga.view.viewmodel.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class EventDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `given a successful usecase when get event by id is called usecase executed and UI state containing event is emitted`() = runTest {
        val domainEventName = "DomainEvent"

        val useCase = mockk<GetEventDetailsUseCase>(relaxed = true)
        coEvery { useCase(any()) } returns Result.success(Success(EventViewMock.provideEvent(domainEventName)))

        val sut = EventDetailsViewModel(useCase, "eventId")

        val uiState = sut.uiState.value
        Assert.assertEquals(domainEventName, uiState.title)
    }
}