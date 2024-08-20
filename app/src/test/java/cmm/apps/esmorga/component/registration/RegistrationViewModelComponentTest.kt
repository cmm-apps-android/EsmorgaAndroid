package cmm.apps.esmorga.component.registration

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import cmm.apps.esmorga.component.mock.UserDataMock
import cmm.apps.esmorga.data.di.DataDIModule.REMOTE_DATASOURCE_INSTANCE_NAME
import cmm.apps.esmorga.data.user.datasource.UserDatasource
import cmm.apps.esmorga.datasource_local.database.EsmorgaDatabase
import cmm.apps.esmorga.di.AppDIModules
import cmm.apps.esmorga.domain.user.PerformRegistrationUserCase
import cmm.apps.esmorga.view.registration.RegistrationViewModel
import cmm.apps.esmorga.view.registration.model.RegistrationEffect
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
class RegistrationViewModelComponentTest : KoinTest {

    private lateinit var mockContext: Context
    private lateinit var mockDatabase: EsmorgaDatabase
    private lateinit var remoteDatasource: UserDatasource

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

    private fun startDI() {
        startKoin {
            androidContext(mockContext)
            modules(
                AppDIModules.modules,
                module {
                    single<EsmorgaDatabase> { mockDatabase }
                    factory<UserDatasource>(named(REMOTE_DATASOURCE_INSTANCE_NAME)) { remoteDatasource }
                }
            )
        }
    }

    @Test
    fun `given a successful API when user clicks on register then UI navigates to event list`() = runTest {
        remoteDatasource = mockk<UserDatasource>()
        coEvery { remoteDatasource.register(any(), any(), any(), any()) } returns UserDataMock.provideUserDataModel()
        startDI()

        val app = mockk<Application>(relaxed = true)
        val useCase: PerformRegistrationUserCase by inject()

        val sut = RegistrationViewModel(app, useCase)

        sut.effect.test {
            sut.onRegisterClicked("User", "test", "test@test.com", "Test@123", "Test@123")

            val effect = awaitItem()
            Assert.assertTrue(effect is RegistrationEffect.NavigateToEventList)
        }
    }
}