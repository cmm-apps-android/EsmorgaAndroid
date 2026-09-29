package cmm.apps.esmorga.notifications

import cmm.apps.esmorga.view.dateformatting.DateFormatterImpl
import cmm.apps.esmorga.view.dateformatting.EsmorgaDateTimeFormatter
import com.onesignal.notifications.INotificationReceivedEvent
import com.onesignal.notifications.INotificationServiceExtension
import java.time.Instant

class OneSignalNotificationServiceExtension(
    private val dateTimeFormatter: EsmorgaDateTimeFormatter = DateFormatterImpl()
) : INotificationServiceExtension {

    override fun onNotificationReceived(event: INotificationReceivedEvent) {
        val eventDateIso = event.notification.additionalData?.optString("eventDate")
        val originalBody = event.notification.body

        if (!eventDateIso.isNullOrBlank()) {
            val formattedBody = formatNotificationBody(
                originalBody = originalBody,
                eventDateIso = eventDateIso
            )
            if (formattedBody != originalBody) {
                event.notification.setExtender { builder ->
                    builder.setContentText(formattedBody)
                }
            }
        }
    }

    /**
     * Formats the notification body with event date.
     * Format: "formatted_date · original_body" (e.g., "4 oct, 16:15 · Buenos Hábitos")
     *
     * If the timestamp is missing or invalid, returns the original body unchanged.
     *
     * @param originalBody the original notification body text
     * @param eventDateIso ISO 8601 date string (e.g., "2026-10-04T14:15:00.000Z")
     * @param formatter optional formatter override (defaults to [dateTimeFormatter])
     * @return formatted body with date, or original body if date parsing fails
     */
    fun formatNotificationBody(
        originalBody: String?,
        eventDateIso: String?,
        formatter: EsmorgaDateTimeFormatter = dateTimeFormatter
    ): String {
        if (eventDateIso.isNullOrBlank()) {
            return originalBody ?: ""
        }

        return try {
            val instant = Instant.parse(eventDateIso)
            val formattedDate = formatter.formatNotificationDate(instant.toEpochMilli())
            "$formattedDate · ${originalBody ?: ""}"
        } catch (_: Exception) {
            originalBody ?: ""
        }
    }
}
