package cmm.apps.esmorga.notifications

import android.content.Context
import cmm.apps.esmorga.BuildConfig
import cmm.apps.esmorga.domain.notifications.model.NotificationPayload
import cmm.apps.esmorga.domain.notifications.repository.NotificationRepository
import cmm.apps.esmorga.notifications.model.toNotificationPayload
import com.onesignal.OneSignal
import com.onesignal.debug.LogLevel
import com.onesignal.notifications.INotificationClickEvent
import com.onesignal.notifications.INotificationClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

object OneSignalManager : NotificationRepository {

    private val _notificationClickFlow = MutableSharedFlow<NotificationPayload>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val notificationClickFlow: SharedFlow<NotificationPayload> = _notificationClickFlow.asSharedFlow()

    fun initialize(
        context: Context,
        appId: String,
        coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    ) {
        OneSignal.Debug.logLevel = if (BuildConfig.DEBUG) LogLevel.VERBOSE else LogLevel.NONE
        OneSignal.initWithContext(context, appId)
        OneSignal.User.addTags(createOneSignalDeviceTags(BuildConfig.FLAVOR, BuildConfig.VERSION_NAME))

        OneSignal.Notifications.addClickListener(object : INotificationClickListener {
            override fun onClick(event: INotificationClickEvent) {
                val additionalData = event.notification.additionalData
                _notificationClickFlow.tryEmit(additionalData.toNotificationPayload())
            }
        })

        coroutineScope.launch {
            OneSignal.Notifications.requestPermission(false)
        }
    }
}
