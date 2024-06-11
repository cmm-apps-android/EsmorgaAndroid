package cmm.apps.esmorga.view.viewmodel.eventList

import android.app.Application
import androidx.lifecycle.LifecycleOwner
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
    fun `given a successful usecase when screen sent to foreground then usecase executed and UI state containing events is emitted`() = runTest {
        val domainEventName = "DomainEvent"

        val app = mockk<Application>(relaxed = true)
        val useCase = mockk<GetEventListUseCase>(relaxed = true)
        coEvery { useCase() } returns Result.success(Success(EventViewMock.provideEventList(listOf(domainEventName))))

        val sut = EventListViewModel(app, useCase)
        sut.onStart(mockk<LifecycleOwner>(relaxed = true))

        val uiState = sut.uiState.value
        Assert.assertTrue(uiState.eventList[0].cardTitle.contains(domainEventName))
    }
}