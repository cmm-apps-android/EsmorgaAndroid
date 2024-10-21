package cmm.apps.esmorga.di

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import cmm.apps.esmorga.domain.event.model.EventType
import cmm.apps.esmorga.view.eventlist.model.EventListUiModel
import cmm.apps.esmorga.view.eventlist.model.EventUILocation
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
                EventListUiModel(
                    id = "123",
                    name = "Event Name",
                    date = "2025-03-08T10:05:30.915Z",
                    dateFormatted = "11 de Marzo de 2024",
                    description = "description",
                    type = EventType.SPORT,
                    location = EventUILocation("Location"),
                    userJoined = false
                )
            }
            androidContext(mockContext)
            modules(AppDIModules.modules)
            checkModules()
        }

    }

}