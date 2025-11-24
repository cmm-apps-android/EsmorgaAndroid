package cmm.apps.esmorga.di

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import cmm.apps.esmorga.component.mock.CreateEventFormModelMock
import cmm.apps.esmorga.component.mock.EventMock
import cmm.apps.esmorga.component.mock.PollMock
import cmm.apps.esmorga.domain.event.model.CreateEventForm
import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.domain.poll.model.Poll
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
                    Event::class -> EventMock.provideEventModel("Event Name")
                    Poll::class -> PollMock.providePollModel()
                    CreateEventForm::class -> CreateEventFormModelMock.provide("Test Event", "Description")
                    else -> null
                }
            }

            androidContext(mockContext)
            modules(AppDIModules.modules)
            checkModules()
        }
    }

}