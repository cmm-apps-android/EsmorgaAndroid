package cmm.apps.esmorga.di

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import cmm.apps.esmorga.component.mock.MockApplication
import cmm.apps.esmorga.data.event.datasource.EventDatasource
import cmm.apps.esmorga.datasource_local.database.EsmorgaDatabase
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.koinApplication
import org.koin.test.check.checkKoinModules
import org.koin.test.check.checkModules
import org.koin.test.verify.verify
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class DIModulesTest {

    private lateinit var mockContext: Context

    @Before
    fun init() {
        mockContext = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun verifyKoinApp() {
        koinApplication {
            androidContext(mockContext)
            modules(AppDIModules.modules)
            checkModules()
        }
    }

}