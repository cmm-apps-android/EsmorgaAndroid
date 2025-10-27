package cmm.apps.esmorga.view.viewmodel.eventList

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import cmm.apps.esmorga.domain.event.GetEventListUseCase
import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.view.dateformatting.DateFormatterImpl
import cmm.apps.esmorga.view.dateformatting.EsmorgaDateTimeFormatter
import cmm.apps.esmorga.view.eventlist.EventListViewModel
import cmm.apps.esmorga.view.eventlist.model.EventListEffect
import cmm.apps.esmorga.view.eventlist.model.EventListUiModel
import cmm.apps.esmorga.view.viewmodel.mock.EventViewMock
import cmm.apps.esmorga.view.viewmodel.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
class EventListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var mockContext: Context

    @Before
    fun init() {
        mockContext = ApplicationProvider.getApplicationContext()
        startKoin {
            androidContext(mockContext)
            modules(module {
                single<EsmorgaDateTimeFormatter> { DateFormatterImpl() }
            })
        }
    }

    @After
    fun shutDown() {
        stopKoin()
    }

    @Test
    fun `given a successful usecase when load method is called usecase executed and UI state containing events is emitted`() = runTest {
        val domainEventName = "DomainEvent"

        val useCase = mockk<GetEventListUseCase>(relaxed = true)
        coEvery { useCase() } returns EsmorgaResult.success(EventViewMock.provideEventList(listOf(domainEventName)))

        val sut = EventListViewModel(useCase)
        sut.loadEvents()

        val uiState = sut.uiState.value
        Assert.assertEquals(domainEventName, uiState.eventList[0].cardTitle)
    }

    @Test
    fun `given events are loaded when event is clicked then navigation effect is emitted`() = runTest {
        val eventId = "EventId"
        val eventName = "EventName"

        val useCase = mockk<GetEventListUseCase>()
        coEvery { useCase() } returns EsmorgaResult.success(listOf(EventViewMock.provideEvent(name = eventName, id = eventId)))

        val viewModel = EventListViewModel(useCase)
        viewModel.loadEvents()

        val clickedUiEvent = EventListUiModel(
            id = eventId,
            imageUrl = "Test Image",
            cardTitle = eventName,
            cardSubtitle1 = "Subtitle 1",
            cardSubtitle2 = "Subtitle 2"
        )

        viewModel.effect.test {
            viewModel.onEventClick(clickedUiEvent)

            val emittedEffect = awaitItem()
            Assert.assertTrue(emittedEffect is EventListEffect.NavigateToEventDetail)

            val navigationEffect = emittedEffect as EventListEffect.NavigateToEventDetail
            Assert.assertEquals(eventId, navigationEffect.event.id)

            cancelAndIgnoreRemainingEvents()
        }
    }
}