package cmm.apps.esmorga.view.viewmodel.eventdetails

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import cmm.apps.esmorga.domain.event.JoinEventUseCase
import cmm.apps.esmorga.domain.event.LeaveEventUseCase
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.domain.result.Source
import cmm.apps.esmorga.domain.user.GetSavedUserUseCase
import cmm.apps.esmorga.domain.user.model.User
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.eventdetails.EventDetailsViewModel
import cmm.apps.esmorga.view.eventdetails.model.EventDetailsEffect
import cmm.apps.esmorga.view.eventlist.mapper.EventListUiMapper.toEvent
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

@RunWith(AndroidJUnit4::class)
class EventDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var mockContext: Context
    private lateinit var sut: EventDetailsViewModel

    private val event = EventViewMock.provideEvent("DomainEvent")

    private val getSavedUserUseCase = mockk<GetSavedUserUseCase>(relaxed = true).also { useCase ->
        coEvery { useCase() } returns EsmorgaResult.success(User("", "", ""))
    }

    private val joinEventUseCase = mockk<JoinEventUseCase>(relaxed = true)

    private val leaveEventUseCase = mockk<LeaveEventUseCase>(relaxed = true)

    @Before
    fun init() {
        mockContext = ApplicationProvider.getApplicationContext()
        startKoin {
            androidContext(mockContext)
        }
        sut = EventDetailsViewModel(getSavedUserUseCase, joinEventUseCase, leaveEventUseCase, EventViewMock.provideEventUiModel("Event Name"))
    }

    @After
    fun shutDown() {
        stopKoin()
    }

    @Test
    fun `given a successful usecase when get event by id is called usecase executed and UI state containing event is emitted`() = runTest {
        val eventName = "Event Name"

        val userUseCase = mockk<GetSavedUserUseCase>(relaxed = true)
        coEvery { userUseCase() } returns EsmorgaResult.failure(Exception())

        sut = EventDetailsViewModel(userUseCase, joinEventUseCase, leaveEventUseCase, EventViewMock.provideEventUiModel(eventName))

        val uiState = sut.uiState.value
        Assert.assertEquals(eventName, uiState.title)
        Assert.assertEquals(mockContext.getString(R.string.button_login_to_join), uiState.primaryButtonTitle)
    }

    @Test
    fun `given a successful usecase when get event by id is called usecase executed and UI state containing event has correct content`() = runTest {
        val eventName = "Event Name"
        sut = EventDetailsViewModel(getSavedUserUseCase, joinEventUseCase, leaveEventUseCase, EventViewMock.provideEventUiModel(eventName, userJoined = true))

        val uiState = sut.uiState.value
        Assert.assertEquals(eventName, uiState.title)
        Assert.assertEquals(mockContext.getString(R.string.button_leave_event), uiState.primaryButtonTitle)
    }

    @Test
    fun `given a successful usecase when get event by id is called usecase executed and UI state containing event not joined has correct content`() = runTest {
        val eventName = "EventName"
        sut = EventDetailsViewModel(getSavedUserUseCase, joinEventUseCase, leaveEventUseCase, EventViewMock.provideEventUiModel(eventName))
        val uiState = sut.uiState.value
        Assert.assertEquals(eventName, uiState.title)
        Assert.assertEquals(mockContext.getString(R.string.button_join_event), uiState.primaryButtonTitle)
    }

    @Test
    fun `given a successful usecase when joint event is called then join event success snackbar is shown`() = runTest {
        val event = EventViewMock.provideEventUiModel("Event Name")
        val joinEventUseCase = mockk<JoinEventUseCase>(relaxed = true)
        coEvery { joinEventUseCase(event.toEvent()) } returns EsmorgaResult.success(Unit)

        sut = EventDetailsViewModel(getSavedUserUseCase, joinEventUseCase, leaveEventUseCase, event)
        sut.effect.test {
            sut.onPrimaryButtonClicked()

            val effect = awaitItem()
            Assert.assertTrue(effect is EventDetailsEffect.ShowJoinEventSuccess)
        }
    }

    @Test
    fun `given a failure usecase when joint event is called then full screen error is shown`() = runTest {
        val event = EventViewMock.provideEventUiModel("Event Name")

        val joinEventUseCase = mockk<JoinEventUseCase>(relaxed = true)
        coEvery { joinEventUseCase(event.toEvent()) } returns EsmorgaResult.failure(Exception())

        sut = EventDetailsViewModel(getSavedUserUseCase, joinEventUseCase, leaveEventUseCase, event)
        sut.effect.test {
            sut.onPrimaryButtonClicked()

            val effect = awaitItem()
            Assert.assertTrue(effect is EventDetailsEffect.ShowFullScreenError)
        }
    }

    @Test
    fun `given no internet connection when joint event is called then no internet error screen is shown`() = runTest {
        val event = EventViewMock.provideEventUiModel("Event Name")
        val joinEventUseCase = mockk<JoinEventUseCase>(relaxed = true)
        coEvery { joinEventUseCase(event.toEvent()) } returns EsmorgaResult.failure(EsmorgaException("No Connection", Source.REMOTE, ErrorCodes.NO_CONNECTION))

        sut = EventDetailsViewModel(getSavedUserUseCase, joinEventUseCase, leaveEventUseCase, event)
        sut.effect.test {
            sut.onPrimaryButtonClicked()

            val effect = awaitItem()
            Assert.assertTrue(effect is EventDetailsEffect.ShowNoNetworkError)
            val noNetworkArguments = (effect as EventDetailsEffect.ShowNoNetworkError).esmorgaNoNetworkArguments
            Assert.assertEquals(R.raw.no_connection_anim, noNetworkArguments.animation)
            Assert.assertEquals(mockContext.getString(R.string.screen_no_connection_title), noNetworkArguments.title)
            Assert.assertEquals(mockContext.getString(R.string.screen_no_connection_body), noNetworkArguments.subtitle)
            Assert.assertEquals(mockContext.getString(R.string.button_ok), noNetworkArguments.buttonText)
        }
    }

    @Test
    fun `given a successful usecase when joint event is called then loading button state is shown`() = runTest {
        val event = EventViewMock.provideEvent("DomainEvent")
        val joinEventUseCase = mockk<JoinEventUseCase>(relaxed = true)
        coEvery { joinEventUseCase(event) } returns EsmorgaResult.success(Unit)

        sut = EventDetailsViewModel(getSavedUserUseCase, joinEventUseCase, leaveEventUseCase, EventViewMock.provideEventUiModel("Event Name"))
        sut.onPrimaryButtonClicked()

        val uiState = sut.uiState.value
        Assert.assertTrue(uiState.primaryButtonLoading)
    }

    @Test
    fun `given a failure usecase when leave event is called then full screen error is shown`() = runTest {
        val event = EventViewMock.provideEventUiModel(name = "Event Name", userJoined = true)

        val leaveEventUseCase = mockk<LeaveEventUseCase>(relaxed = true)
        coEvery { leaveEventUseCase(event.toEvent()) } returns EsmorgaResult.failure(Exception())

        sut = EventDetailsViewModel(getSavedUserUseCase, joinEventUseCase, leaveEventUseCase, event)
        sut.effect.test {
            sut.onPrimaryButtonClicked()

            val effect = awaitItem()
            Assert.assertTrue(effect is EventDetailsEffect.ShowFullScreenError)
        }
    }

    @Test
    fun `given a successful usecase when leave event is called then leave event success snackbar is shown`() = runTest {
        val event = EventViewMock.provideEventUiModel("Event Name", true)

        val leaveEventUseCase = mockk<LeaveEventUseCase>(relaxed = true)
        coEvery { leaveEventUseCase(event.toEvent()) } returns EsmorgaResult.success(Unit)

        sut = EventDetailsViewModel(getSavedUserUseCase, joinEventUseCase, leaveEventUseCase, event)
        sut.effect.test {
            sut.onPrimaryButtonClicked()

            val effect = awaitItem()
            Assert.assertTrue(effect is EventDetailsEffect.ShowLeaveEventSuccess)
        }
    }

    @Test
    fun `given a successful usecase when leave event is called then loading button state is shown`() = runTest {
        val domainEventName = "DomainEvent"
        val event = EventViewMock.provideEvent(domainEventName, userJoined = true)

        val leaveEventUseCase = mockk<LeaveEventUseCase>(relaxed = true)
        coEvery { leaveEventUseCase(event) } returns EsmorgaResult.success(Unit)

        sut = EventDetailsViewModel(getSavedUserUseCase, joinEventUseCase, leaveEventUseCase, EventViewMock.provideEventUiModel("Event Name"))
        sut.onPrimaryButtonClicked()

        val uiState = sut.uiState.value
        Assert.assertTrue(uiState.primaryButtonLoading)
    }
}

