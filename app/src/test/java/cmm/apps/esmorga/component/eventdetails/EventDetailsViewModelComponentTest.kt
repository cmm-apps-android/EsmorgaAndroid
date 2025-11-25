package cmm.apps.esmorga.component.eventdetails

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import cmm.apps.esmorga.component.mock.ApiMockerRule
import cmm.apps.esmorga.component.mock.EventMock
import cmm.apps.esmorga.component.mock.MockApplication
import cmm.apps.esmorga.component.mock.UserMock
import cmm.apps.esmorga.component.mock.mockResponseSuccess
import cmm.apps.esmorga.datasource_local.database.EsmorgaDatabase
import cmm.apps.esmorga.datasource_remote.api.EsmorgaEventAuthenticatedApi
import cmm.apps.esmorga.di.AppDIModules
import cmm.apps.esmorga.domain.event.JoinEventUseCase
import cmm.apps.esmorga.domain.event.LeaveEventUseCase
import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.domain.user.GetSavedUserUseCase
import cmm.apps.esmorga.view.eventdetails.EventDetailsViewModel
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
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
import org.koin.test.KoinTest
import org.koin.test.inject
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@Config(application = MockApplication::class)
class EventDetailsViewModelComponentTest : KoinTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val apiMocker = ApiMockerRule(EsmorgaEventAuthenticatedApi::class.java, testDispatcher)

    private lateinit var mockContext: Context
    private lateinit var mockDatabase: EsmorgaDatabase

    @Before
    fun init() {
        mockContext = ApplicationProvider.getApplicationContext()
        mockDatabase = Room
            .inMemoryDatabaseBuilder(mockContext, EsmorgaDatabase::class.java)
            .allowMainThreadQueries()
            .setTransactionExecutor(testDispatcher.asExecutor())
            .setQueryExecutor(testDispatcher.asExecutor())
            .build()
    }

    @After
    fun shutDown() {
        mockDatabase.close()
        stopKoin()
    }

    private fun startDI() {
        startKoin {
            androidContext(mockContext)
            modules(
                AppDIModules.modules,
                module {
                    single<EsmorgaDatabase> { mockDatabase }
                    single<EsmorgaEventAuthenticatedApi> { apiMocker.api }
                }
            )
        }
    }

    @Test
    fun `given a DB without an user when screen is shown then UI state with login button is returned`() = runTest {
        startDI()

        val getSavedUserUseCase: GetSavedUserUseCase by inject()
        val joinEventUseCase: JoinEventUseCase by inject()
        val leaveEventUseCase: LeaveEventUseCase by inject()
        val event: Event = EventMock.provideEventModel("TestEvent")

        val sut = EventDetailsViewModel(getSavedUserUseCase, joinEventUseCase, leaveEventUseCase, event)

        sut.onStart(mockk<LifecycleOwner>(relaxed = true))

        advanceUntilIdle()

        val uiState = sut.uiState.value
        Assert.assertTrue("Primary button not enabled", uiState.isPrimaryButtonEnabled)
        Assert.assertTrue("Sign in button not shown", uiState.primaryButtonTitle.contains("Sign in"))
    }

    @Test
    fun `given a DB with a stored user when screen is shown then UI state with join button is returned`() = runTest {
        mockDatabase.userDao().insertUser(UserMock.provideUserLocalModel())

        startDI()

        val getSavedUserUseCase: GetSavedUserUseCase by inject()
        val joinEventUseCase: JoinEventUseCase by inject()
        val leaveEventUseCase: LeaveEventUseCase by inject()
        val event: Event = EventMock.provideEventModel("TestEvent")

        val sut = EventDetailsViewModel(getSavedUserUseCase, joinEventUseCase, leaveEventUseCase, event)

        sut.onStart(mockk<LifecycleOwner>(relaxed = true))

        advanceUntilIdle()

        val uiState = sut.uiState.value
        Assert.assertTrue("Primary button not enabled", uiState.isPrimaryButtonEnabled)
        Assert.assertTrue("Join button not shown", uiState.primaryButtonTitle.contains("Join"))
    }

    @Test
    fun `given a DB with a stored user and an event that user has joined when screen is shown then UI state with leave button is returned`() = runTest {
        mockDatabase.userDao().insertUser(UserMock.provideUserLocalModel())

        startDI()

        val getSavedUserUseCase: GetSavedUserUseCase by inject()
        val joinEventUseCase: JoinEventUseCase by inject()
        val leaveEventUseCase: LeaveEventUseCase by inject()
        val event: Event = EventMock.provideEventModel(name = "TestEvent", userJoined = true)

        val sut = EventDetailsViewModel(getSavedUserUseCase, joinEventUseCase, leaveEventUseCase, event)

        sut.onStart(mockk<LifecycleOwner>(relaxed = true))

        advanceUntilIdle()

        val uiState = sut.uiState.value
        Assert.assertTrue("Primary button not enabled", uiState.isPrimaryButtonEnabled)
        Assert.assertTrue("Leave button not shown", uiState.primaryButtonTitle.contains("Leave"))
    }

    @Test
    fun `given a DB with a stored user and a working backed when user tries to join an event then state is updated accordingly`() = runTest {
        mockDatabase.userDao().insertUser(UserMock.provideUserLocalModel())
        apiMocker.enqueue { mockResponseSuccess() }

        startDI()

        val getSavedUserUseCase: GetSavedUserUseCase by inject()
        val joinEventUseCase: JoinEventUseCase by inject()
        val leaveEventUseCase: LeaveEventUseCase by inject()
        val event: Event = EventMock.provideEventModel(name = "TestEvent")

        val sut = EventDetailsViewModel(getSavedUserUseCase, joinEventUseCase, leaveEventUseCase, event)

        sut.onStart(mockk<LifecycleOwner>(relaxed = true))
        advanceUntilIdle()

        val uiStateInitial = sut.uiState.value
        Assert.assertTrue("Primary button not enabled", uiStateInitial.isPrimaryButtonEnabled)
        Assert.assertTrue("Join button not shown", uiStateInitial.primaryButtonTitle.contains("Join"))

        sut.onPrimaryButtonClicked()
        advanceUntilIdle()

        val uiStateAfterJoined = sut.uiState.value
        Assert.assertTrue("Primary button not enabled", uiStateAfterJoined.isPrimaryButtonEnabled)
        Assert.assertTrue("Leave button not shown", uiStateAfterJoined.primaryButtonTitle.contains("Leave"))
    }
}