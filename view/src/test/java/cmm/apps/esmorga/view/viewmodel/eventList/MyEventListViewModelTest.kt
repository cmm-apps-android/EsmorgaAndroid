package cmm.apps.esmorga.view.viewmodel.eventList

import app.cash.turbine.test
import cmm.apps.esmorga.domain.event.GetMyEventListUseCase
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.domain.result.Source
import cmm.apps.esmorga.domain.user.GetUserIsAdminUsecase
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

        val isAdminUseCase = mockk<GetUserIsAdminUsecase>(relaxed = true)
        coEvery { isAdminUseCase() } returns false

        val sut = MyEventListViewModel(useCase, isAdminUseCase)
        sut.loadMyEvents()

        val uiState = sut.uiState.value
        Assert.assertEquals(domainEventName, uiState.eventList[0].cardTitle)
    }

    @Test
    fun `given a empty usecase when load method is called then UI state containing empty error is emitted`() = runTest {
        val useCase = mockk<GetMyEventListUseCase>(relaxed = true)
        coEvery { useCase() } returns EsmorgaResult.success(EventViewMock.provideEventList(listOf()))

        val isAdminUseCase = mockk<GetUserIsAdminUsecase>(relaxed = true)
        coEvery { isAdminUseCase() } returns false

        val sut = MyEventListViewModel(useCase, isAdminUseCase)
        sut.loadMyEvents()

        val uiState = sut.uiState.value
        Assert.assertEquals(MyEventListError.EMPTY_LIST, uiState.error)
    }

    @Test
    fun `given a not logged in error usecase when load method is called then UI state containing error is emitted`() = runTest {
        val useCase = mockk<GetMyEventListUseCase>(relaxed = true)
        coEvery { useCase() } returns EsmorgaResult.failure(EsmorgaException(message = "Mock error", source = Source.LOCAL, code = ErrorCodes.NOT_LOGGED_IN))

        val isAdminUseCase = mockk<GetUserIsAdminUsecase>(relaxed = true)
        coEvery { isAdminUseCase() } returns false

        val sut = MyEventListViewModel(useCase, isAdminUseCase)
        sut.loadMyEvents()

        val uiState = sut.uiState.value
        Assert.assertEquals(MyEventListError.NOT_LOGGED_IN, uiState.error)
    }

    @Test
    fun `given an usecase with unkwnown error when load method is called then UI state containing error is emitted`() = runTest {
        val useCase = mockk<GetMyEventListUseCase>(relaxed = true)
        coEvery { useCase() } returns EsmorgaResult.failure(EsmorgaException(message = "Mock error", source = Source.REMOTE, code = ErrorCodes.UNKNOWN_ERROR))

        val isAdminUseCase = mockk<GetUserIsAdminUsecase>(relaxed = true)
        coEvery { isAdminUseCase() } returns false

        val sut = MyEventListViewModel(useCase, isAdminUseCase)
        sut.loadMyEvents()

        val uiState = sut.uiState.value
        Assert.assertEquals(MyEventListError.UNKNOWN, uiState.error)
    }

    @Test
    fun `given an usecase with no internet error when load method is called then UI state containing cached events and error is emitted`() = runTest {
        val domainEventName = "DomainEvent"

        val useCase = mockk<GetMyEventListUseCase>(relaxed = true)
        coEvery { useCase() } returns EsmorgaResult.noConnectionError(EventViewMock.provideEventList(listOf(domainEventName)))

        val isAdminUseCase = mockk<GetUserIsAdminUsecase>(relaxed = true)
        coEvery { isAdminUseCase() } returns false

        val sut = MyEventListViewModel(useCase, isAdminUseCase)
        sut.effect.test {
            sut.loadMyEvents()

            val uiState = sut.uiState.value
            Assert.assertEquals(domainEventName, uiState.eventList[0].cardTitle)

            val effect = awaitItem()
            Assert.assertTrue(effect is MyEventListEffect.ShowNoNetworkPrompt)
        }
    }

    @Test
    fun `given an successful usecase when load method is called then UI state containing loading is emitted`() = runTest {
        val domainEventName = "DomainEvent"

        val useCase = mockk<GetMyEventListUseCase>(relaxed = true)
        coEvery { useCase() } returns EsmorgaResult.noConnectionError(EventViewMock.provideEventList(listOf(domainEventName)))

        val isAdminUseCase = mockk<GetUserIsAdminUsecase>(relaxed = true)
        coEvery { isAdminUseCase() } returns false

        val sut = MyEventListViewModel(useCase, isAdminUseCase)
        sut.uiState.test {
            awaitItem() //skip state already in flow before calling loadMyEvents again
            sut.loadMyEvents()
            Assert.assertTrue(awaitItem().loading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given user is admin when checkIfUserIsAdmin is called then UI state should have isAdmin true`() = runTest {
        val useCase = mockk<GetMyEventListUseCase>(relaxed = true)
        val isAdminUseCase = mockk<GetUserIsAdminUsecase>()
        coEvery { isAdminUseCase() } returns true

        val sut = MyEventListViewModel(useCase, isAdminUseCase)
        sut.checkIfUserIsAdmin()

        val uiState = sut.uiState.value
        Assert.assertTrue(uiState.isAdmin)
    }
}