package cmm.apps.esmorga.notifications

import org.junit.Assert.assertFalse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OneSignalManagerTest {

    @Test
    fun `Given a null subscription id When validating registration Then it should be invalid`() {
        assertFalse(isRegisteredOneSignalSubscriptionId(null))
    }

    @Test
    fun `Given an empty subscription id When validating registration Then it should be invalid`() {
        assertFalse(isRegisteredOneSignalSubscriptionId(""))
    }

    @Test
    fun `Given a local placeholder subscription id When validating registration Then it should be invalid`() {
        assertFalse(isRegisteredOneSignalSubscriptionId("local-12345"))
    }

    @Test
    fun `Given a server assigned subscription id When validating registration Then it should be valid`() {
        assertTrue(isRegisteredOneSignalSubscriptionId("7a5906b5-9e5a-4c3d-b248-1234567890ab"))
    }

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
}



