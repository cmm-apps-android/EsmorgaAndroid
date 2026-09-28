package cmm.apps.esmorga.notifications

import android.content.Context
import cmm.apps.esmorga.BuildConfig
import com.onesignal.OneSignal
import com.onesignal.debug.LogLevel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object OneSignalManager {

    fun initialize(
        context: Context,
        appId: String,
        coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    ) {
        OneSignal.Debug.logLevel = if (BuildConfig.DEBUG) LogLevel.VERBOSE else LogLevel.NONE
        OneSignal.initWithContext(context, appId)
        OneSignal.User.addTags(createOneSignalDeviceTags(BuildConfig.FLAVOR, BuildConfig.VERSION_NAME))

        coroutineScope.launch {
            OneSignal.Notifications.requestPermission(false)
        }
    }
}
