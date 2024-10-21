package cmm.apps.esmorga.di

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import cmm.apps.esmorga.component.mock.EventDataMock
import cmm.apps.esmorga.data.event.mapper.toEvent
import cmm.apps.esmorga.view.eventlist.mapper.EventListUiMapper.toEventUi
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.koinApplication
import org.koin.test.check.checkModules
import org.koin.test.mock.MockProvider

@RunWith(AndroidJUnit4::class)
class DIModulesTest {

    private lateinit var mockContext: Context

    @Before
    fun init() {
        mockContext = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun verifyKoinApp() {
        koinApplication {
            MockProvider.register {
                EventDataMock.provideEventDataModel("Event Name").toEvent().toEventUi()
            }
            androidContext(mockContext)
            modules(AppDIModules.modules)
            checkModules()
        }

    }

}