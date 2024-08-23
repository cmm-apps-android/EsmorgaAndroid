package cmm.apps.esmorga.view.viewmodel.eventList

import cmm.apps.esmorga.domain.event.GetEventListUseCase
import cmm.apps.esmorga.domain.result.Success
import cmm.apps.esmorga.view.eventlist.EventListViewModel
import cmm.apps.esmorga.view.viewmodel.mock.EventViewMock
import cmm.apps.esmorga.view.viewmodel.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class EventListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `given a successful usecase when load method is called usecase executed and UI state containing events is emitted`() = runTest {
        val domainEventName = "DomainEvent"

        val useCase = mockk<GetEventListUseCase>(relaxed = true)
        coEvery { useCase() } returns Result.success(Success(EventViewMock.provideEventList(listOf(domainEventName))))

        val sut = EventListViewModel(useCase)
        sut.loadEvents()

        val uiState = sut.uiState.value
        Assert.assertEquals(domainEventName, uiState.eventList[0].cardTitle)
    }
}