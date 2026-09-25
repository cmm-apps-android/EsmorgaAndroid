package cmm.apps.esmorga

import android.app.Application
import android.os.StrictMode
import androidx.lifecycle.LifecycleObserver
import cmm.apps.esmorga.di.AppDIModules
import cmm.apps.esmorga.notifications.OneSignalManager
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class EsmorgaApp : Application(), LifecycleObserver {

    override fun onCreate() {
        super.onCreate()

        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build()
        )

        startKoin {
            allowOverride(false)
            androidLogger()
            androidContext(this@EsmorgaApp)
            modules(AppDIModules.modules)
        }

        OneSignalManager.initialize(
            application = this,
            appId = "0115e97b-297b-4eec-bd1a-130fe1e6a960"
        )
    }

}