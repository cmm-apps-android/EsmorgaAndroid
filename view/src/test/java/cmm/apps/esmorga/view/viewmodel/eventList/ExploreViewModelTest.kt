package cmm.apps.esmorga.view.viewmodel.eventList

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import cmm.apps.esmorga.domain.event.GetEventsAndPollsUseCase
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.domain.result.Source
import cmm.apps.esmorga.view.dateformatting.DateFormatterImpl
import cmm.apps.esmorga.view.dateformatting.EsmorgaDateTimeFormatter
import cmm.apps.esmorga.view.explore.ExploreViewModel
import cmm.apps.esmorga.view.explore.model.ExploreEffect
import cmm.apps.esmorga.view.explore.model.ListCardUiModel
import cmm.apps.esmorga.view.viewmodel.mock.EventViewMock
import cmm.apps.esmorga.view.viewmodel.mock.PollViewMock
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
class ExploreViewModelTest {

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
    fun `given a successful usecase when load method is called usecase executed and UI state containing events and polls is emitted`() = runTest {
        val domainEventName = "DomainEvent"
        val domainPollName = "DomainPoll"

        val useCase = mockk<GetEventsAndPollsUseCase>(relaxed = true)
        coEvery { useCase() } returns EsmorgaResult.success(Pair(EventViewMock.provideEventList(listOf(domainEventName)), PollViewMock.providePollList(listOf(domainPollName))))

        val sut = ExploreViewModel(useCase)
        sut.loadEventsAndPolls()

        val uiState = sut.uiState.value
        Assert.assertEquals(domainEventName, uiState.eventList[0].cardTitle)
        Assert.assertEquals(domainPollName, uiState.pollList[0].cardTitle)
    }

    @Test
    fun `given a failing usecase when load method is called usecase executed and UI state containing error is emitted`() = runTest {
        val useCase = mockk<GetEventsAndPollsUseCase>(relaxed = true)
        coEvery { useCase() } returns EsmorgaResult.failure(EsmorgaException(message = "Test Exception", source = Source.REMOTE, code = 500))

        val sut = ExploreViewModel(useCase)
        sut.loadEventsAndPolls()

        val uiState = sut.uiState.value
        Assert.assertNotNull("Error should be filled", uiState.error)
    }

    @Test
    fun `given a no connection usecase when load method is called usecase executed and UI state containing data and no connection effect are emitted`() = runTest {
        val domainEventName = "DomainEvent"
        val domainPollName = "DomainPoll"

        val useCase = mockk<GetEventsAndPollsUseCase>(relaxed = true)
        coEvery { useCase() } returns EsmorgaResult.noConnectionError(
            Pair(
                EventViewMock.provideEventList(listOf(domainEventName)),
                PollViewMock.providePollList(listOf(domainPollName))
            )
        )

        val sut = ExploreViewModel(useCase)

        sut.effect.test {
            sut.loadEventsAndPolls()

            val uiState = sut.uiState.value
            Assert.assertEquals(domainEventName, uiState.eventList[0].cardTitle)
            Assert.assertEquals(domainPollName, uiState.pollList[0].cardTitle)

            val effect = awaitItem()
            Assert.assertTrue(effect is ExploreEffect.ShowNoNetworkPrompt)
        }
    }

    @Test
    fun `given events are loaded when event is clicked then navigation effect is emitted`() = runTest {
        val eventId = "EventId"
        val eventName = "EventName"

        val useCase = mockk<GetEventsAndPollsUseCase>()
        coEvery { useCase() } returns EsmorgaResult.success(Pair(listOf(EventViewMock.provideEvent(name = eventName, id = eventId)), PollViewMock.providePollList(listOf())))

        val viewModel = ExploreViewModel(useCase)
        viewModel.loadEventsAndPolls()

        val clickedUiEvent = ListCardUiModel(
            id = eventId,
            imageUrl = "Test Image",
            cardTitle = eventName,
            cardSubtitle1 = "Subtitle 1",
            cardSubtitle2 = "Subtitle 2"
        )

        viewModel.effect.test {
            viewModel.onEventClick(clickedUiEvent)

            val emittedEffect = awaitItem()
            Assert.assertTrue(emittedEffect is ExploreEffect.NavigateToEventDetail)

            val navigationEffect = emittedEffect as ExploreEffect.NavigateToEventDetail
            Assert.assertEquals(eventId, navigationEffect.event.id)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given polls are loaded when poll is clicked then navigation effect is emitted`() = runTest {
        val pollId = "PollId"
        val pollName = "PollName"

        val useCase = mockk<GetEventsAndPollsUseCase>()
        coEvery { useCase() } returns EsmorgaResult.success(Pair(listOf(), listOf(PollViewMock.providePoll(id = pollId, name = pollName))))

        val viewModel = ExploreViewModel(useCase)
        viewModel.loadEventsAndPolls()

        val clickedUiEvent = ListCardUiModel(
            id = pollId,
            imageUrl = "Test Image",
            cardTitle = pollName,
            cardSubtitle1 = "Subtitle 1"
        )

        viewModel.effect.test {
            viewModel.onPollClick(clickedUiEvent)

            val emittedEffect = awaitItem()
            Assert.assertTrue(emittedEffect is ExploreEffect.NavigateToPollDetail)

            val navigationEffect = emittedEffect as ExploreEffect.NavigateToPollDetail
            Assert.assertEquals(pollId, navigationEffect.poll.id)

            cancelAndIgnoreRemainingEvents()
        }
    }
}