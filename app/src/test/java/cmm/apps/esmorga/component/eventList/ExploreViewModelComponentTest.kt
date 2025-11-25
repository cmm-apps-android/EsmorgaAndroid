package cmm.apps.esmorga.component.eventList

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import cmm.apps.esmorga.component.mock.ApiMockerRule
import cmm.apps.esmorga.component.mock.MockApplication
import cmm.apps.esmorga.component.mock.mockResponseSuccess
import cmm.apps.esmorga.datasource_local.database.EsmorgaDatabase
import cmm.apps.esmorga.datasource_remote.api.EsmorgaEventOpenApi
import cmm.apps.esmorga.di.AppDIModules
import cmm.apps.esmorga.domain.event.GetEventsAndPollsUseCase
import cmm.apps.esmorga.view.explore.ExploreViewModel
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
class ExploreViewModelComponentTest : KoinTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val apiMocker = ApiMockerRule(EsmorgaEventOpenApi::class.java, testDispatcher)

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
                    single<EsmorgaEventOpenApi> { apiMocker.api }
                }
            )
        }
    }

    @Test
    fun `given a successful API and an empty DB when screen is shown then UI state with events is returned`() = runTest {
        val remoteEventName = "Mocked Event from events.json"
        apiMocker.enqueue { mockResponseSuccess("/events.json") }
        startDI()

        val useCase: GetEventsAndPollsUseCase by inject()
        val sut = ExploreViewModel(useCase)

        sut.loadEventsAndPolls()

        advanceUntilIdle()

        val uiState = sut.uiState.value
        Assert.assertTrue(uiState.eventList[0].cardTitle.contains(remoteEventName))
    }
}