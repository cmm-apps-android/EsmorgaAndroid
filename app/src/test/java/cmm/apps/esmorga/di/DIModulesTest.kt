package cmm.apps.esmorga.di

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import cmm.apps.esmorga.component.mock.EventDataMock
import cmm.apps.esmorga.component.mock.EventTypeModelMock
import cmm.apps.esmorga.data.event.mapper.toEvent
import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.view.createevent.model.CreateEventFormUiModel
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
            MockProvider.register { clazz ->
                when (clazz) {
                    Event::class -> EventDataMock.provideEventDataModel("Event Name").toEvent()
                    CreateEventFormUiModel::class -> EventTypeModelMock.provide("Test Event", "Description")
                    else -> null
                }
            }

            androidContext(mockContext)
            modules(AppDIModules.modules)
            checkModules()
        }
    }

}