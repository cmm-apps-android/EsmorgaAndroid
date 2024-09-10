package cmm.apps.esmorga.di

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.koinApplication
import org.koin.test.check.checkModules

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
            androidContext(mockContext)
            modules(AppDIModules.modules)
            checkModules()
        }
    }

}