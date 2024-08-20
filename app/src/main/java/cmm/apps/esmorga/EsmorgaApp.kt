package cmm.apps.esmorga

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import cmm.apps.esmorga.di.AppDIModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class EsmorgaApp : Application(), LifecycleObserver {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            allowOverride(false)
            androidLogger()
            androidContext(this@EsmorgaApp)
            modules(AppDIModules.modules)
        }
    }

}