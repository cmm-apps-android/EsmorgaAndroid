package cmm.apps.esmorga.datasource_remote.event

import cmm.apps.esmorga.datasource_remote.api.EsmorgaApi
import cmm.apps.esmorga.datasource_remote.api.NetworkApiHelper
import cmm.apps.esmorga.datasource_remote.mock.MockServer
import cmm.apps.esmorga.datasource_remote.mock.json.ServerFiles
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
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

        val sut = NetworkApiHelper().provideApi(mockServer.start(), EsmorgaApi::class.java)

        val eventWrapper = sut.getEvents()

        Assert.assertEquals(2, eventWrapper.remoteEventList.size)
        Assert.assertTrue(eventWrapper.remoteEventList[0].remoteName.contains("MobgenFest"))
    }

}