package cmm.apps.esmorga.view.viewmodel.eventdetails

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import cmm.apps.esmorga.domain.event.JoinEventUseCase
import cmm.apps.esmorga.domain.event.LeaveEventUseCase
import cmm.apps.esmorga.domain.event.model.EventLocation
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.domain.result.Source
import cmm.apps.esmorga.domain.user.GetSavedUserUseCase
import cmm.apps.esmorga.domain.user.model.RoleType
import cmm.apps.esmorga.domain.user.model.User
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.dateformatting.DateFormatterImpl
import cmm.apps.esmorga.view.dateformatting.EsmorgaDateTimeFormatter
import cmm.apps.esmorga.view.eventdetails.EventDetailsViewModel
import cmm.apps.esmorga.view.eventdetails.model.EventDetailsEffect
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
class EventDetailsViewModelTest {

    companion object {
        private const val ONE_HOUR_IN_MILLIS = 1000 * 60 * 60
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var mockContext: Context
    private lateinit var sut: EventDetailsViewModel

    private val getSavedUserUseCase = mockk<GetSavedUserUseCase>(relaxed = true).also { useCase ->
        coEvery { useCase() } returns EsmorgaResult.success(User("", "", "", RoleType.USER))
    }

    private val joinEventUseCase = mockk<JoinEventUseCase>(relaxed = true)

    private val leaveEventUseCase = mockk<LeaveEventUseCase>(relaxed = true)

    @Before
    fun init() {
        mockContext = ApplicationProvider.getApplicationContext()
        startKoin {
            androidContext(mockContext)
            modules(module {
                single<EsmorgaDateTimeFormatter> { DateFormatterImpl() }
            })
        }
        sut = EventDetailsViewModel(getSavedUserUseCase, joinEventUseCase, leaveEventUseCase, EventViewMock.provideEvent("Event Name"))
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

        sut = EventDetailsViewModel(userUseCase, joinEventUseCase, leaveEventUseCase, EventViewMock.provideEvent(eventName))

        val uiState = sut.uiState.value
        Assert.assertEquals(eventName, uiState.title)
        Assert.assertEquals(mockContext.getString(R.string.button_login_to_join), uiState.primaryButtonTitle)
    }

    @Test
    fun `given a successful usecase when get event by id is called usecase executed and UI state containing event has correct content`() = runTest {
        val eventName = "Event Name"
        sut = EventDetailsViewModel(getSavedUserUseCase, joinEventUseCase, leaveEventUseCase, EventViewMock.provideEvent(eventName, userJoined = true))

        val uiState = sut.uiState.value
        Assert.assertEquals(eventName, uiState.title)
        Assert.assertEquals(mockContext.getString(R.string.button_leave_event), uiState.primaryButtonTitle)
    }

    @Test
    fun `given a successful usecase when get event by id is called usecase executed and UI state containing event not joined has correct content`() = runTest {
        val eventName = "EventName"
        sut = EventDetailsViewModel(getSavedUserUseCase, joinEventUseCase, leaveEventUseCase, EventViewMock.provideEvent(eventName))
        val uiState = sut.uiState.value
        Assert.assertEquals(eventName, uiState.title)
        Assert.assertEquals(mockContext.getString(R.string.button_join_event), uiState.primaryButtonTitle)
    }

    @Test
    fun `given a successful usecase when joint event is called then join event success snackbar is shown`() = runTest {
        val event = EventViewMock.provideEvent("Event Name")
        val joinEventUseCase = mockk<JoinEventUseCase>(relaxed = true)
        coEvery { joinEventUseCase(event) } returns EsmorgaResult.success(Unit)

        sut = EventDetailsViewModel(getSavedUserUseCase, joinEventUseCase, leaveEventUseCase, event)
        sut.effect.test {
            sut.onPrimaryButtonClicked()

            val effect = awaitItem()
            Assert.assertTrue(effect is EventDetailsEffect.ShowJoinEventSuccess)
        }
    }

    @Test
    fun `given a failure usecase when joint event is called then full screen error is shown`() = runTest {
        val event = EventViewMock.provideEvent("Event Name")

        val joinEventUseCase = mockk<JoinEventUseCase>(relaxed = true)
        coEvery { joinEventUseCase(event) } returns EsmorgaResult.failure(Exception())

        sut = EventDetailsViewModel(getSavedUserUseCase, joinEventUseCase, leaveEventUseCase, event)
        sut.effect.test {
            sut.onPrimaryButtonClicked()

            val effect = awaitItem()
            Assert.assertTrue(effect is EventDetailsEffect.ShowFullScreenError)
        }
    }

    @Test
    fun `given no internet connection when joint event is called then no internet error screen is shown`() = runTest {
        val event = EventViewMock.provideEvent("Event Name")
        val joinEventUseCase = mockk<JoinEventUseCase>(relaxed = true)
        coEvery { joinEventUseCase(event) } returns EsmorgaResult.failure(EsmorgaException("No Connection", Source.REMOTE, ErrorCodes.NO_CONNECTION))

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

        sut = EventDetailsViewModel(getSavedUserUseCase, joinEventUseCase, leaveEventUseCase, EventViewMock.provideEvent("Event Name"))
        sut.onPrimaryButtonClicked()

        val uiState = sut.uiState.value
        Assert.assertTrue(uiState.primaryButtonLoading)
    }

    @Test
    fun `given a failure usecase when leave event is called then full screen error is shown`() = runTest {
        val event = EventViewMock.provideEvent(name = "Event Name", userJoined = true)

        val leaveEventUseCase = mockk<LeaveEventUseCase>(relaxed = true)
        coEvery { leaveEventUseCase(event) } returns EsmorgaResult.failure(Exception())

        sut = EventDetailsViewModel(getSavedUserUseCase, joinEventUseCase, leaveEventUseCase, event)
        sut.effect.test {
            sut.onPrimaryButtonClicked()

            val effect = awaitItem()
            Assert.assertTrue(effect is EventDetailsEffect.ShowFullScreenError)
        }
    }

    @Test
    fun `given a successful usecase when leave event is called then leave event success snackbar is shown`() = runTest {
        val event = EventViewMock.provideEvent("Event Name", true)

        val leaveEventUseCase = mockk<LeaveEventUseCase>(relaxed = true)
        coEvery { leaveEventUseCase(event) } returns EsmorgaResult.success(Unit)

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

        sut = EventDetailsViewModel(getSavedUserUseCase, joinEventUseCase, leaveEventUseCase, EventViewMock.provideEvent("Event Name"))
        sut.onPrimaryButtonClicked()

        val uiState = sut.uiState.value
        Assert.assertTrue(uiState.primaryButtonLoading)
    }

    @Test
    fun `given event details screen when navigate button is clicked then navigate to map`() = runTest {
        val event = EventViewMock.provideEvent("Event Name").copy(location = EventLocation("Location", lat = 43.0930493, long = -7.348734))
        coEvery { joinEventUseCase(event) } returns EsmorgaResult.failure(EsmorgaException("No Connection", Source.REMOTE, ErrorCodes.NO_CONNECTION))

        sut = EventDetailsViewModel(getSavedUserUseCase, joinEventUseCase, leaveEventUseCase, event)
        sut.effect.test {
            sut.onNavigateClick()

            val effect = awaitItem()
            Assert.assertTrue(effect is EventDetailsEffect.NavigateToLocation)
            val location = (effect as EventDetailsEffect.NavigateToLocation)
            Assert.assertEquals(event.location.lat, location.lat)
            Assert.assertEquals(event.location.long, location.lng)
            Assert.assertEquals(event.location.name, location.locationName)
        }
    }

    @Test
    fun `given event with capacity when getEventDetails is called then capacity info is shown in UI state`() = runTest {
        val event = EventViewMock.provideEvent(
            name = "CapacityTest",
            currentAttendeeCount = 5,
            maxCapacity = 10,
        )

        sut = EventDetailsViewModel(getSavedUserUseCase, joinEventUseCase, leaveEventUseCase, event)
        val uiState = sut.uiState.value

        Assert.assertEquals(5, uiState.currentAttendeeCount)
        Assert.assertEquals(10, uiState.maxCapacity)
    }


    @Test
    fun `given user not joined when joins event then attendee count increases and success effect emitted`() = runTest {
        val event = EventViewMock.provideEvent(
            name = "JoinEventFlow",
            currentAttendeeCount = 4,
            maxCapacity = 10,
            userJoined = false
        )
        val joinEventUseCase = mockk<JoinEventUseCase>(relaxed = true)
        coEvery { joinEventUseCase(event) } returns EsmorgaResult.success(Unit)

        sut = EventDetailsViewModel(getSavedUserUseCase, joinEventUseCase, leaveEventUseCase, event)

        sut.effect.test {
            sut.onPrimaryButtonClicked()

            val effect = awaitItem()
            Assert.assertTrue(effect is EventDetailsEffect.ShowJoinEventSuccess)

            val uiState = sut.uiState.value
            Assert.assertTrue(uiState.primaryButtonTitle.contains("Leave", ignoreCase = true))
            Assert.assertEquals(5, uiState.currentAttendeeCount)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given user joined when leaves event then attendee count decreases and success effect emitted`() = runTest {
        val event = EventViewMock.provideEvent(
            name = "LeaveEventFlow",
            currentAttendeeCount = 5,
            maxCapacity = 10,
            userJoined = true
        )

        val leaveEventUseCase = mockk<LeaveEventUseCase>(relaxed = true)
        coEvery { leaveEventUseCase(event) } returns EsmorgaResult.success(Unit)

        sut = EventDetailsViewModel(getSavedUserUseCase, joinEventUseCase, leaveEventUseCase, event)

        sut.effect.test {
            sut.onPrimaryButtonClicked()

            val effect = awaitItem()
            Assert.assertTrue(effect is EventDetailsEffect.ShowLeaveEventSuccess)

            val uiState = sut.uiState.value
            Assert.assertTrue(uiState.primaryButtonTitle.contains("Join", ignoreCase = true))
            Assert.assertEquals(4, uiState.currentAttendeeCount)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `end to end flow join and leave event`() = runTest {
        val event = EventViewMock.provideEvent(
            name = "FullFlowEvent",
            currentAttendeeCount = 2,
            maxCapacity = 10,
            userJoined = false
        )
        coEvery { joinEventUseCase(event) } returns EsmorgaResult.success(Unit)
        coEvery { leaveEventUseCase(event) } returns EsmorgaResult.success(Unit)

        sut = EventDetailsViewModel(getSavedUserUseCase, joinEventUseCase, leaveEventUseCase, event)

        sut.effect.test {
            sut.onPrimaryButtonClicked()
            Assert.assertTrue(awaitItem() is EventDetailsEffect.ShowJoinEventSuccess)
            Assert.assertEquals(3, sut.uiState.value.currentAttendeeCount)

            sut.onPrimaryButtonClicked()
            Assert.assertTrue(awaitItem() is EventDetailsEffect.ShowLeaveEventSuccess)
            Assert.assertEquals(2, sut.uiState.value.currentAttendeeCount)

            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `given user tries to join a full event when join button is clicked then a full event error message is shown`() = runTest {
        val event = EventViewMock.provideEvent("Event Name")
        val joinEventUseCase = mockk<JoinEventUseCase>(relaxed = true)

        coEvery { joinEventUseCase(event) } returns EsmorgaResult.failure(
            EsmorgaException(
                message = "Event full",
                source = Source.REMOTE,
                code = ErrorCodes.EVENT_FULL
            )
        )

        sut = EventDetailsViewModel(getSavedUserUseCase, joinEventUseCase, leaveEventUseCase, event)

        sut.effect.test {
            sut.onPrimaryButtonClicked()

            val effect = awaitItem()
            Assert.assertTrue(effect is EventDetailsEffect.ShowFullEventError)

            val uiState = sut.uiState.value
            Assert.assertFalse(uiState.primaryButtonLoading)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given an upcoming event when user opens event details then join deadline is visible and not expired`() = runTest {
        val futureDeadline = System.currentTimeMillis() + ONE_HOUR_IN_MILLIS
        val event = EventViewMock.provideEvent("DeadlineTest", joinDeadline = futureDeadline)

        sut = EventDetailsViewModel(getSavedUserUseCase, joinEventUseCase, leaveEventUseCase, event)
        val uiState = sut.uiState.value

        Assert.assertEquals(futureDeadline, uiState.joinDeadline)
        Assert.assertFalse(uiState.isJoinDeadlinePassed)
    }


    @Test
    fun `given an event with a future join deadline when user views the event then join button is enabled`() = runTest {
        val futureDeadline = System.currentTimeMillis() + ONE_HOUR_IN_MILLIS
        val event = EventViewMock.provideEvent("ButtonEnabledTest", joinDeadline = futureDeadline)

        sut = EventDetailsViewModel(getSavedUserUseCase, joinEventUseCase, leaveEventUseCase, event)

        val uiState = sut.uiState.value
        Assert.assertTrue(uiState.isJoinButtonEnabled)
    }

    @Test
    fun `given an event with a past join deadline when user views the event then join button is disabled and deadline is passed`() = runTest {
        val pastDeadline = System.currentTimeMillis() - ONE_HOUR_IN_MILLIS
        val event = EventViewMock.provideEvent("ButtonDisabledTest", joinDeadline = pastDeadline)

        sut = EventDetailsViewModel(getSavedUserUseCase, joinEventUseCase, leaveEventUseCase, event)

        val uiState = sut.uiState.value
        Assert.assertFalse(uiState.isJoinButtonEnabled)
        Assert.assertTrue(uiState.isJoinDeadlinePassed)
    }

    @Test
    fun `given an event with a past join deadline when user views the event then event title is still visible`() = runTest {
        val pastDeadline = System.currentTimeMillis() - ONE_HOUR_IN_MILLIS
        val event = EventViewMock.provideEvent("VisibleAfterDeadlineTest", joinDeadline = pastDeadline)

        sut = EventDetailsViewModel(getSavedUserUseCase, joinEventUseCase, leaveEventUseCase, event)

        val uiState = sut.uiState.value
        Assert.assertEquals("VisibleAfterDeadlineTest", uiState.title)
    }
}