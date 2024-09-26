package cmm.apps.esmorga.datasource_remote

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import cmm.apps.esmorga.datasource_remote.api.EsmorgaApi
import cmm.apps.esmorga.datasource_remote.api.NetworkApiHelper
import cmm.apps.esmorga.datasource_remote.mock.EsmorgaAuthenticationMock.getAuthInterceptor
import cmm.apps.esmorga.datasource_remote.mock.EsmorgaAuthenticationMock.getEsmorgaAuthenticatorMock
import cmm.apps.esmorga.datasource_remote.mock.MockServer
import cmm.apps.esmorga.datasource_remote.mock.json.ServerFiles
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
class EsmorgaApiTest {

    private lateinit var mockServer: MockServer

    @Before
    fun init() {
        mockServer = MockServer()

        startKoin {
            modules(module {
                single<Context> { mockk() }
            })
        }
    }

    @After
    fun shutDown() {
        mockServer.shutdown()
        stopKoin()
    }

    @Test
    fun `given a successful mock server when events are requested then a correct eventWrapper is returned`() = runTest {
        mockServer.enqueueFile(200, ServerFiles.GET_EVENTS)

        val sut = NetworkApiHelper().provideApi(mockServer.start(), EsmorgaApi::class.java, getEsmorgaAuthenticatorMock(), getAuthInterceptor())

        val eventWrapper = sut.getEvents()

        Assert.assertEquals(2, eventWrapper.remoteEventList.size)
        Assert.assertTrue(eventWrapper.remoteEventList[0].remoteName.contains("MobgenFest"))
    }

    @Test
    fun `given a successful mock server when login is requested then a correct user is returned`() = runTest {
        mockServer.enqueueFile(200, ServerFiles.LOGIN)

        val sut = NetworkApiHelper().provideApi(mockServer.start(), EsmorgaApi::class.java, getEsmorgaAuthenticatorMock(), getAuthInterceptor())

        val user = sut.login(body = mapOf("email" to "email", "password" to "password"))

        Assert.assertEquals("Albus", user.remoteProfile.remoteName)
    }

}