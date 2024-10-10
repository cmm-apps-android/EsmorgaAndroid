package cmm.apps.esmorga.view.viewmodel.eventdetails

import androidx.test.ext.junit.runners.AndroidJUnit4
import cmm.apps.esmorga.domain.event.GetEventDetailsUseCase
import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.domain.user.GetSavedUserUseCase
import cmm.apps.esmorga.domain.user.model.User
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.eventdetails.EventDetailsViewModel
import cmm.apps.esmorga.view.viewmodel.mock.EventViewMock
import cmm.apps.esmorga.view.viewmodel.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EventDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `given a successful usecase when get event by id is called usecase executed and UI state containing event is emitted`() = runTest {
        val domainEventName = "DomainEvent"

        val useCase = mockk<GetEventDetailsUseCase>(relaxed = true)
        coEvery { useCase(any()) } returns EsmorgaResult.success(EventViewMock.provideEvent(domainEventName))

        val userUseCase = mockk<GetSavedUserUseCase>(relaxed = true)
        coEvery { userUseCase() } returns EsmorgaResult.failure(Exception())

        val sut = EventDetailsViewModel(useCase, userUseCase, "eventId")

        val uiState = sut.uiState.value
        Assert.assertEquals(domainEventName, uiState.title)
        Assert.assertEquals(R.string.button_login_to_join, uiState.primaryButtonTitle)
    }

    @Test
    fun `given a successful usecase when get event by id is called usecase executed and UI state containing event has correct content`() = runTest {
        val domainEventName = "DomainEvent"

        val useCase = mockk<GetEventDetailsUseCase>(relaxed = true)
        coEvery { useCase(any()) } returns EsmorgaResult.success(EventViewMock.provideEvent(domainEventName, userJoined = true))

        val userUseCase = mockk<GetSavedUserUseCase>(relaxed = true)
        coEvery { userUseCase() } returns EsmorgaResult.success(User("", "", ""))

        val sut = EventDetailsViewModel(useCase, userUseCase, "eventId")

        val uiState = sut.uiState.value
        Assert.assertEquals(domainEventName, uiState.title)
        Assert.assertEquals(R.string.button_leave_event, uiState.primaryButtonTitle)
    }

    @Test
    fun `given a successful usecase when get event by id is called usecase executed and UI state containing event not joined has correct content`() = runTest {
        val domainEventName = "DomainEvent"

        val useCase = mockk<GetEventDetailsUseCase>(relaxed = true)
        coEvery { useCase(any()) } returns EsmorgaResult.success(EventViewMock.provideEvent(domainEventName, userJoined = false))

        val userUseCase = mockk<GetSavedUserUseCase>(relaxed = true)
        coEvery { userUseCase() } returns EsmorgaResult.success(User("", "", ""))

        val sut = EventDetailsViewModel(useCase, userUseCase, "eventId")

        val uiState = sut.uiState.value
        Assert.assertEquals(domainEventName, uiState.title)
        Assert.assertEquals(R.string.button_join_event, uiState.primaryButtonTitle)
    }
}