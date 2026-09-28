package cmm.apps.esmorga.domain.notifications.model

data class NotificationPayload(
    val type: String? = null,
    val eventId: String? = null,
    val eventDate: String? = null
)