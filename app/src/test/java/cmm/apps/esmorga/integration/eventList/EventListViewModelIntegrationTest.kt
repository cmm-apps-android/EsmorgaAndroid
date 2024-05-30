package cmm.apps.esmorga.integration.eventList

import android.app.Application
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import cmm.apps.esmorga.data.di.DataDIModule.EVENT_REMOTE_DATASOURCE_INSTANCE_NAME
import cmm.apps.esmorga.data.event.datasource.EventDatasource
import cmm.apps.esmorga.datasource_local.database.EsmorgaDatabase
import cmm.apps.esmorga.di.AppDIModules
import cmm.apps.esmorga.domain.event.GetEventListUseCase
import cmm.apps.esmorga.integration.mock.EventDataMock
import cmm.apps.esmorga.view.eventList.EventListViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class EventListViewModelIntegrationTest : KoinTest {

    private lateinit var mockContext: Context
    private lateinit var mockDatabase: EsmorgaDatabase
    private lateinit var remoteDatasource: EventDatasource

    private val testDispatcher = UnconfinedTestDispatcher()

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

    @Test
    fun `given a successful API and an empty DB when screen is shown then UI state with events is returned`() = runTest {
        val remoteEventName = "RemoteEvent"
        remoteDatasource = mockk<EventDatasource>()
        coEvery { remoteDatasource.getEvents() } returns EventDataMock.provideEventDataModelList(listOf(remoteEventName))
        startKoin()

        val app = mockk<Application>(relaxed = true)
        val useCase: GetEventListUseCase by inject()

        val sut = EventListViewModel(app, useCase)

        sut.onStart(mockk<LifecycleOwner>(relaxed = true))

        val uiState = sut.uiState.value
        Assert.assertTrue(uiState.eventList[0].contains(remoteEventName))
    }

    private fun startKoin() {
        startKoin {
            androidContext(mockContext)
            modules(
                AppDIModules.modules,
                module {
                    single<EsmorgaDatabase> { mockDatabase }
                    factory<EventDatasource>(named(EVENT_REMOTE_DATASOURCE_INSTANCE_NAME)) { remoteDatasource }
                }
            )
        }
    }
}