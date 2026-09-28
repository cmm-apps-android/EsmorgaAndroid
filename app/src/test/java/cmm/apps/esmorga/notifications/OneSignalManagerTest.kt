package cmm.apps.esmorga.notifications

import cmm.apps.esmorga.notifications.model.toNotificationPayload
import io.mockk.every
import io.mockk.mockk
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class OneSignalManagerTest {

    @Test
    fun `Given a prod flavor When resolving the environment tag Then it should be prod`() {
        assertEquals("prod", resolveOneSignalEnvironmentTag("prod"))
    }

    @Test
    fun `Given a qa flavor When resolving the environment tag Then it should be qa`() {
        assertEquals("qa", resolveOneSignalEnvironmentTag("qa"))
    }

    @Test
    fun `Given a flavor and version When creating device tags Then it should include environment platform and version`() {
        assertEquals(
            mapOf(
                "environment" to "qa",
                "platform" to "android",
                "version" to "1.2.0"
            ),
            createOneSignalDeviceTags(flavor = "qa", versionName = "1.2.0")
        )
    }

    @Test
    fun `Given a valid notification json object When parsing payload Then it should populate fields correctly`() {
        val jsonObject = mockk<JSONObject>()
        every { jsonObject.optString("type") } returns "event-created"

        val payload = jsonObject.toNotificationPayload()

        assertEquals("event-created", payload.type)
    }

    @Test
    fun `Given a null json object When parsing payload Then fields should be null`() {
        val jsonObject: JSONObject? = null
        val payload = jsonObject.toNotificationPayload()

        assertNull(payload.type)
    }
}
