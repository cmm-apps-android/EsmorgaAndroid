package cmm.apps.esmorga.domain.notifications.repository

import cmm.apps.esmorga.domain.notifications.model.NotificationPayload
import kotlinx.coroutines.flow.SharedFlow

interface NotificationRepository {
    val notificationClickFlow: SharedFlow<NotificationPayload>
}