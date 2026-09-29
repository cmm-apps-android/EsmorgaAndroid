package cmm.apps.esmorga.notifications

import androidx.core.app.NotificationCompat
import cmm.apps.esmorga.view.dateformatting.EsmorgaDateTimeFormatter
import com.onesignal.notifications.IDisplayableMutableNotification
import com.onesignal.notifications.INotificationReceivedEvent
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Test

class OneSignalNotificationServiceExtensionTest {

    private val dateTimeFormatter = mockk<EsmorgaDateTimeFormatter>()
    private val serviceExtension = OneSignalNotificationServiceExtension(dateTimeFormatter)

    @Test
    fun `given notification with valid eventDate when received then setExtender is invoked with formatted content text`() {
        val event = mockk<INotificationReceivedEvent>()
        val notification = mockk<IDisplayableMutableNotification>(relaxed = true)
        val additionalData = mockk<JSONObject>()

        every { event.notification } returns notification
        every { notification.additionalData } returns additionalData
        every { additionalData.optString("eventDate") } returns "2026-10-04T14:15:00.000Z"
        every { notification.body } returns "Buenos Hábitos"
        every { dateTimeFormatter.formatNotificationDate(any()) } returns "4 oct, 16:15"

        val extenderSlot = slot<NotificationCompat.Extender>()
        every { notification.setExtender(capture(extenderSlot)) } just Runs

        serviceExtension.onNotificationReceived(event)

        verify { notification.setExtender(any()) }

        val builder = mockk<NotificationCompat.Builder>(relaxed = true)
        extenderSlot.captured.extend(builder)

        verify { builder.setContentText("4 oct, 16:15 · Buenos Hábitos") }
    }

    @Test
    fun `given notification without eventDate when received then setExtender is not invoked and original body retained`() {
        val event = mockk<INotificationReceivedEvent>()
        val notification = mockk<IDisplayableMutableNotification>(relaxed = true)
        val additionalData = mockk<JSONObject>()

        every { event.notification } returns notification
        every { notification.additionalData } returns additionalData
        every { additionalData.optString("eventDate") } returns ""
        every { notification.body } returns "Buenos Hábitos"

        serviceExtension.onNotificationReceived(event)

        verify(exactly = 0) { notification.setExtender(any()) }
    }

    @Test
    fun `given notification with invalid eventDate when received then setExtender is not invoked and original body retained`() {
        val event = mockk<INotificationReceivedEvent>()
        val notification = mockk<IDisplayableMutableNotification>(relaxed = true)
        val additionalData = mockk<JSONObject>()

        every { event.notification } returns notification
        every { notification.additionalData } returns additionalData
        every { additionalData.optString("eventDate") } returns "invalid-timestamp"
        every { notification.body } returns "Buenos Hábitos"

        serviceExtension.onNotificationReceived(event)

        verify(exactly = 0) { notification.setExtender(any()) }
    }

    @Test
    fun `given valid ISO date when formatting notification body then returns formatted date prepended to body`() {
        val isoDate = "2026-10-04T14:15:00.000Z"
        val originalBody = "Buenos Hábitos"
        val formattedDate = "4 oct, 16:15"

        every { dateTimeFormatter.formatNotificationDate(any()) } returns formattedDate

        val result = serviceExtension.formatNotificationBody(
            originalBody = originalBody,
            eventDateIso = isoDate
        )

        assertEquals("4 oct, 16:15 · Buenos Hábitos", result)
    }

    @Test
    fun `given null eventDate when formatting notification body then returns original body`() {
        val originalBody = "Buenos Hábitos"

        val result = serviceExtension.formatNotificationBody(
            originalBody = originalBody,
            eventDateIso = null
        )

        assertEquals("Buenos Hábitos", result)
    }

    @Test
    fun `given blank eventDate when formatting notification body then returns original body`() {
        val originalBody = "Buenos Hábitos"

        val result = serviceExtension.formatNotificationBody(
            originalBody = originalBody,
            eventDateIso = "   "
        )

        assertEquals("Buenos Hábitos", result)
    }

    @Test
    fun `given invalid eventDate when formatting notification body then returns original body`() {
        val originalBody = "Buenos Hábitos"

        val result = serviceExtension.formatNotificationBody(
            originalBody = originalBody,
            eventDateIso = "invalid-date-string"
        )

        assertEquals("Buenos Hábitos", result)
    }

    @Test
    fun `given valid eventDate and null original body when formatting then returns formatted date with empty body`() {
        val isoDate = "2026-10-04T14:15:00.000Z"
        val formattedDate = "4 oct, 16:15"

        every { dateTimeFormatter.formatNotificationDate(any()) } returns formattedDate

        val result = serviceExtension.formatNotificationBody(
            originalBody = null,
            eventDateIso = isoDate
        )

        assertEquals("4 oct, 16:15 · ", result)
    }
}
