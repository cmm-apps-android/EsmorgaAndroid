package cmm.apps.esmorga.notifications

import org.junit.Assert.assertEquals
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
}
