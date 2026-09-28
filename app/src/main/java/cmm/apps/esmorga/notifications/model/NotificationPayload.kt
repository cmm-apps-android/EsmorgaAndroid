package cmm.apps.esmorga.notifications.model

import cmm.apps.esmorga.domain.notifications.model.NotificationPayload
import org.json.JSONObject

internal fun JSONObject?.toNotificationPayload(): NotificationPayload {
    return NotificationPayload(
        type = this?.optString("type"),
        eventId = this?.optString("eventId"),
        eventDate = this?.optString("eventDate")
    )
}