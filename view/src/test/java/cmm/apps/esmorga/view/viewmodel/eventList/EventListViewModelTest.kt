package cmm.apps.esmorga.view.viewmodel.eventList

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import cmm.apps.esmorga.domain.event.GetEventListUseCase
import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.domain.event.model.EventLocation
import cmm.apps.esmorga.domain.event.model.EventType
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
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

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
    fun `given events are loaded when onEventClick is called with existing event then description is URL encoded in the emitted effect`() = runTest {
        val eventId = "testId123"
        val originalDescription = "Description with spaces and special characters: áéíóú & ñ & ?"
        val expectedEncodedDescription = URLEncoder.encode(originalDescription, StandardCharsets.UTF_8.toString())

        val mockDomainEvent = Event(
            id = eventId,
            name = "Test Event Name",
            date = 0,
            description = originalDescription,
            type = EventType.PARTY,
            imageUrl = "Test Image",
            location = EventLocation(
                name = "Test Location",
                lat = 0.0,
                long = 0.0
            ),
            userJoined = false
        )

        val useCase = mockk<GetEventListUseCase>()
        coEvery { useCase() } returns EsmorgaResult.success(listOf(mockDomainEvent))

        val viewModel = EventListViewModel(useCase)
        viewModel.loadEvents()

        val clickedUiEvent = EventListUiModel(
            id = eventId,
            imageUrl = "Test Image",
            cardTitle = "Test Event Name",
            cardSubtitle1 = "Subtitle 1",
            cardSubtitle2 = "Subtitle 2"
        )

        viewModel.effect.test {
            viewModel.onEventClick(clickedUiEvent)

            val emittedEffect = awaitItem()
            Assert.assertTrue(emittedEffect is EventListEffect.NavigateToEventDetail)

            val navigationEffect = emittedEffect as EventListEffect.NavigateToEventDetail
            Assert.assertEquals(
                expectedEncodedDescription,
                navigationEffect.event.description
            )
            Assert.assertEquals(eventId, navigationEffect.event.id)

            cancelAndIgnoreRemainingEvents()
        }
    }
}