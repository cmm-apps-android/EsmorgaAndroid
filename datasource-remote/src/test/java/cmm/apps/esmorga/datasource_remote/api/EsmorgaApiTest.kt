package cmm.apps.esmorga.datasource_remote.api

import android.content.Context
import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import cmm.apps.esmorga.datasource_remote.mock.MockServer
import cmm.apps.esmorga.datasource_remote.mock.json.ServerFiles
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.M])
@RunWith(AndroidJUnit4::class)
class EsmorgaApiTest {

    private lateinit var mockServer: MockServer

    @Before
    fun init() {
        mockServer = MockServer()
    }

    @After
    fun shutDown() {
        mockServer.shutdown()
    }

    @Test
    fun `given a successful mock server when events are requested then a correct eventWrapper is returned`() = runTest {
        mockServer.enqueueFile(200, ServerFiles.GET_EVENTS)

        val sut = NetworkApiHelper().provideApi(
            mockServer.start(), EsmorgaGuestApi::class.java, null, null, null, null
        )

        val eventWrapper = sut.getEvents()

        Assert.assertEquals(2, eventWrapper.remoteEventList.size)
        Assert.assertTrue(eventWrapper.remoteEventList[0].remoteName.contains("MobgenFest"))
    }

    @Test
    fun `given a successful mock server when attendees are requested then a correct eventAttendeeWrapper is returned`() = runTest {
        mockServer.enqueueFile(200, ServerFiles.GET_EVENT_ATTENDEES)

        val sut = NetworkApiHelper().provideApi(
            mockServer.start(), EsmorgaApi::class.java, null, null, null, null
        )

        val attendeesWrapper = sut.getEventAttendees("test")

        Assert.assertEquals(3, attendeesWrapper.remoteEventAttendeeList.size)
        Assert.assertTrue(attendeesWrapper.remoteEventAttendeeList[0].contains("Mobgen Test"))
    }

    @Test
    fun `given a successful mock server when login is requested then a correct user is returned`() = runTest {
        mockServer.enqueueFile(200, ServerFiles.LOGIN)

        val sut = NetworkApiHelper().provideApi(
            mockServer.start(), EsmorgaAuthApi::class.java, null, null, null, null
        )

        val user = sut.login(body = mapOf("email" to "email", "password" to "password"))

        Assert.assertEquals("Albus", user.remoteProfile.remoteName)
    }

}