package cmm.apps.esmorga.view.viewmodel.eventdetails

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import cmm.apps.esmorga.domain.event.GetEventDetailsUseCase
import cmm.apps.esmorga.domain.event.JoinEventUseCase
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.domain.result.Source
import cmm.apps.esmorga.domain.user.GetSavedUserUseCase
import cmm.apps.esmorga.domain.user.model.User
import cmm.apps.esmorga.view.R
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

@RunWith(AndroidJUnit4::class)
class EventDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var mockContext: Context

    @Before
    fun init() {
        mockContext = ApplicationProvider.getApplicationContext()
        startKoin {
            androidContext(mockContext)
        }
    }

    @After
    fun shutDown() {
        stopKoin()
    }

    @Test
    fun `given a successful usecase when get event by id is called usecase executed and UI state containing event is emitted`() = runTest {
        val domainEventName = "DomainEvent"

        val useCase = mockk<GetEventDetailsUseCase>(relaxed = true)
        coEvery { useCase(any()) } returns EsmorgaResult.success(EventViewMock.provideEvent(domainEventName))

        val userUseCase = mockk<GetSavedUserUseCase>(relaxed = true)
        coEvery { userUseCase() } returns EsmorgaResult.failure(Exception())

        val joinEventUseCase = mockk<JoinEventUseCase>(relaxed = true)

        val sut = EventDetailsViewModel(useCase, userUseCase, joinEventUseCase, "eventId")

        val uiState = sut.uiState.value
        Assert.assertEquals(domainEventName, uiState.title)
        Assert.assertEquals(mockContext.getString(R.string.button_login_to_join), uiState.primaryButtonTitle)
    }

    @Test
    fun `given a successful usecase when get event by id is called usecase executed and UI state containing event has correct content`() = runTest {
        val domainEventName = "DomainEvent"

        val useCase = mockk<GetEventDetailsUseCase>(relaxed = true)
        coEvery { useCase(any()) } returns EsmorgaResult.success(EventViewMock.provideEvent(domainEventName, userJoined = true))

        val userUseCase = mockk<GetSavedUserUseCase>(relaxed = true)
        coEvery { userUseCase() } returns EsmorgaResult.success(User("", "", ""))

        val joinEventUseCase = mockk<JoinEventUseCase>(relaxed = true)

        val sut = EventDetailsViewModel(useCase, userUseCase, joinEventUseCase, "eventId")

        val uiState = sut.uiState.value
        Assert.assertEquals(domainEventName, uiState.title)
        Assert.assertEquals(mockContext.getString(R.string.button_leave_event), uiState.primaryButtonTitle)
    }

    @Test
    fun `given a successful usecase when get event by id is called usecase executed and UI state containing event not joined has correct content`() = runTest {
        val domainEventName = "DomainEvent"

        val useCase = mockk<GetEventDetailsUseCase>(relaxed = true)
        coEvery { useCase(any()) } returns EsmorgaResult.success(EventViewMock.provideEvent(domainEventName, userJoined = false))

        val userUseCase = mockk<GetSavedUserUseCase>(relaxed = true)
        coEvery { userUseCase() } returns EsmorgaResult.success(User("", "", ""))

        val joinEventUseCase = mockk<JoinEventUseCase>(relaxed = true)

        val sut = EventDetailsViewModel(useCase, userUseCase, joinEventUseCase, "eventId")

        val uiState = sut.uiState.value
        Assert.assertEquals(domainEventName, uiState.title)
        Assert.assertEquals(mockContext.getString(R.string.button_join_event), uiState.primaryButtonTitle)
    }

    @Test
    fun `given a successful usecase when joint event is called then join event success snackbar is shown`() = runTest {
        val domainEventName = "DomainEvent"

        val useCase = mockk<GetEventDetailsUseCase>(relaxed = true)
        coEvery { useCase(any()) } returns EsmorgaResult.success(EventViewMock.provideEvent(domainEventName, userJoined = false))

        val userUseCase = mockk<GetSavedUserUseCase>(relaxed = true)
        coEvery { userUseCase() } returns EsmorgaResult.success(User("", "", ""))

        val joinEventUseCase = mockk<JoinEventUseCase>(relaxed = true)
        coEvery { joinEventUseCase("eventId") } returns EsmorgaResult.success(Unit)

        val sut = EventDetailsViewModel(useCase, userUseCase, joinEventUseCase, "eventId")
        sut.effect.test {
            sut.onPrimaryButtonClicked()

            val effect = awaitItem()
            Assert.assertTrue(effect is EventDetailsEffect.ShowJoinEventSuccessSnackbar)
        }
    }

    @Test
    fun `given a failure usecase when joint event is called then full screen error is shown`() = runTest {
        val domainEventName = "DomainEvent"

        val useCase = mockk<GetEventDetailsUseCase>(relaxed = true)
        coEvery { useCase(any()) } returns EsmorgaResult.success(EventViewMock.provideEvent(domainEventName, userJoined = false))

        val userUseCase = mockk<GetSavedUserUseCase>(relaxed = true)
        coEvery { userUseCase() } returns EsmorgaResult.success(User("", "", ""))

        val joinEventUseCase = mockk<JoinEventUseCase>(relaxed = true)
        coEvery { joinEventUseCase("eventId") } returns EsmorgaResult.failure(Exception())

        val sut = EventDetailsViewModel(useCase, userUseCase, joinEventUseCase, "eventId")
        sut.effect.test {
            sut.onPrimaryButtonClicked()

            val effect = awaitItem()
            Assert.assertTrue(effect is EventDetailsEffect.ShowFullScreenError)
        }
    }

    @Test
    fun `given no internet connection when joint event is called then no internet error screen is shown`() = runTest {
        val domainEventName = "DomainEvent"

        val useCase = mockk<GetEventDetailsUseCase>(relaxed = true)
        coEvery { useCase(any()) } returns EsmorgaResult.success(EventViewMock.provideEvent(domainEventName, userJoined = false))

        val userUseCase = mockk<GetSavedUserUseCase>(relaxed = true)
        coEvery { userUseCase() } returns EsmorgaResult.success(User("", "", ""))

        val joinEventUseCase = mockk<JoinEventUseCase>(relaxed = true)
        coEvery { joinEventUseCase("eventId") } returns EsmorgaResult.failure(EsmorgaException("No Connection", Source.REMOTE, ErrorCodes.NO_CONNECTION))

        val sut = EventDetailsViewModel(useCase, userUseCase, joinEventUseCase, "eventId")
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
        val domainEventName = "DomainEvent"

        val useCase = mockk<GetEventDetailsUseCase>(relaxed = true)
        coEvery { useCase(any()) } returns EsmorgaResult.success(EventViewMock.provideEvent(domainEventName, userJoined = false))

        val userUseCase = mockk<GetSavedUserUseCase>(relaxed = true)
        coEvery { userUseCase() } returns EsmorgaResult.success(User("", "", ""))

        val joinEventUseCase = mockk<JoinEventUseCase>(relaxed = true)
        coEvery { joinEventUseCase("") } returns EsmorgaResult.success(Unit)

        val sut = EventDetailsViewModel(useCase, userUseCase, joinEventUseCase, "eventId")
        sut.onPrimaryButtonClicked()

        val uiState = sut.uiState.value
        Assert.assertTrue(uiState.primaryButtonLoading)
    }
}