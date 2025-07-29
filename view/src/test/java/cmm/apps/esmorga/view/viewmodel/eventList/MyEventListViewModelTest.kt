package cmm.apps.esmorga.view.viewmodel.eventList

import app.cash.turbine.test
import cmm.apps.esmorga.domain.event.GetMyEventListUseCase
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.domain.result.Source
import cmm.apps.esmorga.domain.user.GetSavedUserUseCase
import cmm.apps.esmorga.view.eventlist.MyEventListViewModel
import cmm.apps.esmorga.view.eventlist.model.MyEventListEffect
import cmm.apps.esmorga.view.eventlist.model.MyEventListError
import cmm.apps.esmorga.view.viewmodel.mock.EventViewMock
import cmm.apps.esmorga.view.viewmodel.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class MyEventListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `given a successful usecase when load method is called then UI state containing events is emitted`() = runTest {
        val domainEventName = "DomainEvent"

        val useCase = mockk<GetMyEventListUseCase>(relaxed = true)
        coEvery { useCase() } returns EsmorgaResult.success(EventViewMock.provideEventList(listOf(domainEventName)))

        val getSavedUserUseCase = mockk<GetSavedUserUseCase>(relaxed = true)

        val sut = MyEventListViewModel(useCase, getSavedUserUseCase)
        sut.loadMyEvents()

        val uiState = sut.uiState.value
        Assert.assertEquals(domainEventName, uiState.eventList[0].cardTitle)
    }

    @Test
    fun `given a empty usecase when load method is called then UI state containing empty error is emitted`() = runTest {
        val useCase = mockk<GetMyEventListUseCase>(relaxed = true)
        coEvery { useCase() } returns EsmorgaResult.success(EventViewMock.provideEventList(listOf()))

        val getSavedUserUseCase = mockk<GetSavedUserUseCase>(relaxed = true)

        val sut = MyEventListViewModel(useCase, getSavedUserUseCase)
        sut.loadMyEvents()

        val uiState = sut.uiState.value
        Assert.assertEquals(MyEventListError.EMPTY_LIST, uiState.error)
    }

    @Test
    fun `given a not logged in error usecase when load method is called then UI state containing error is emitted`() = runTest {
        val useCase = mockk<GetMyEventListUseCase>(relaxed = true)
        coEvery { useCase() } returns EsmorgaResult.failure(
            EsmorgaException(message = "Mock error", source = Source.LOCAL, code = ErrorCodes.NOT_LOGGED_IN)
        )

        val getSavedUserUseCase = mockk<GetSavedUserUseCase>(relaxed = true)

        val sut = MyEventListViewModel(useCase, getSavedUserUseCase)
        sut.loadMyEvents()

        val uiState = sut.uiState.value
        Assert.assertEquals(MyEventListError.NOT_LOGGED_IN, uiState.error)
    }

    @Test
    fun `given an usecase with unknown error when load method is called then UI state containing error is emitted`() = runTest {
        val useCase = mockk<GetMyEventListUseCase>(relaxed = true)
        coEvery { useCase() } returns EsmorgaResult.failure(
            EsmorgaException(message = "Mock error", source = Source.REMOTE, code = ErrorCodes.UNKNOWN_ERROR)
        )

        val getSavedUserUseCase = mockk<GetSavedUserUseCase>(relaxed = true)

        val sut = MyEventListViewModel(useCase, getSavedUserUseCase)
        sut.loadMyEvents()

        val uiState = sut.uiState.value
        Assert.assertEquals(MyEventListError.UNKNOWN, uiState.error)
    }

    @Test
    fun `given an usecase with no internet error when load method is called then UI state containing cached events and error is emitted`() = runTest {
        val domainEventName = "DomainEvent"

        val useCase = mockk<GetMyEventListUseCase>(relaxed = true)
        coEvery { useCase() } returns EsmorgaResult.noConnectionError(EventViewMock.provideEventList(listOf(domainEventName)))

        val getSavedUserUseCase = mockk<GetSavedUserUseCase>(relaxed = true)

        val sut = MyEventListViewModel(useCase, getSavedUserUseCase)
        sut.effect.test {
            sut.loadMyEvents()

            val uiState = sut.uiState.value
            Assert.assertEquals(domainEventName, uiState.eventList[0].cardTitle)

            val effect = awaitItem()
            Assert.assertTrue(effect is MyEventListEffect.ShowNoNetworkPrompt)
        }
    }

    @Test
    fun `given a successful usecase when load method is called then UI state containing loading is emitted`() = runTest {
        val domainEventName = "DomainEvent"

        val useCase = mockk<GetMyEventListUseCase>(relaxed = true)
        coEvery { useCase() } returns EsmorgaResult.noConnectionError(EventViewMock.provideEventList(listOf(domainEventName)))

        val getSavedUserUseCase = mockk<GetSavedUserUseCase>(relaxed = true)

        val sut = MyEventListViewModel(useCase, getSavedUserUseCase)
        sut.uiState.test {
            awaitItem()
            sut.loadMyEvents()
            Assert.assertTrue(awaitItem().loading)
            cancelAndIgnoreRemainingEvents()
        }
    }
}