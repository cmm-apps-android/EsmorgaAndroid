package cmm.apps.esmorga.datasource_remote

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import cmm.apps.esmorga.datasource_remote.api.EsmorgaAuthApi
import cmm.apps.esmorga.datasource_remote.api.EsmorgaGuestApi
import cmm.apps.esmorga.datasource_remote.api.NetworkApiHelper
import cmm.apps.esmorga.datasource_remote.mock.DeviceInterceptorMock.getDeviceInterceptor
import cmm.apps.esmorga.datasource_remote.mock.EsmorgaAuthenticationMock.getAuthInterceptor
import cmm.apps.esmorga.datasource_remote.mock.EsmorgaAuthenticationMock.getEsmorgaAuthenticatorMock
import cmm.apps.esmorga.datasource_remote.mock.MockServer
import cmm.apps.esmorga.datasource_remote.mock.json.ServerFiles
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.M])
@RunWith(AndroidJUnit4::class)
class EsmorgaApiTest {

    private lateinit var mockServer: MockServer
    private lateinit var mockContext: Context
    private lateinit var mockConnectivityManager: ConnectivityManager
    private lateinit var mockNetwork: Network
    private lateinit var mockNetworkCapabilities: NetworkCapabilities
    private var isNetworkAvailable = true

    @Before
    fun init() {
        mockServer = MockServer()
        mockContext = mockk(relaxed = true)
        mockConnectivityManager = mockk(relaxed = true)
        mockNetwork = mockk(relaxed = true)
        mockNetworkCapabilities = mockk(relaxed = true)

        mockkStatic(FirebaseCrashlytics::class)
        coEvery { mockContext.getSystemService(Context.CONNECTIVITY_SERVICE) } returns mockConnectivityManager

        coEvery { mockConnectivityManager.activeNetwork } returns mockNetwork
        coEvery { mockConnectivityManager.getNetworkCapabilities(mockNetwork) } returns mockNetworkCapabilities
        coEvery { mockNetworkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns isNetworkAvailable

        startKoin {
            modules(module {
                single<Context> { mockContext }
            })
        }
    }

    @After
    fun shutDown() {
        mockServer.shutdown()
        stopKoin()
        unmockkStatic(FirebaseCrashlytics::class)
    }

    @Test
    fun `given a successful mock server when events are requested then a correct eventWrapper is returned`() = runTest {
        coEvery { mockNetworkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns true
        val crashlyticsMock = mockk<FirebaseCrashlytics>(relaxed = true)

        coEvery { FirebaseCrashlytics.getInstance() } returns crashlyticsMock

        mockServer.enqueueFile(200, ServerFiles.GET_EVENTS)

        val sut = NetworkApiHelper().provideApi(mockServer.start(), EsmorgaGuestApi::class.java, getEsmorgaAuthenticatorMock(), getAuthInterceptor(), getDeviceInterceptor())

        val eventWrapper = sut.getEvents()

        Assert.assertEquals(2, eventWrapper.remoteEventList.size)
        Assert.assertTrue(eventWrapper.remoteEventList[0].remoteName.contains("MobgenFest"))
    }

    @Test
    fun `given a successful mock server when login is requested then a correct user is returned`() = runTest {
        coEvery { mockNetworkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns true
        val crashlyticsMock = mockk<FirebaseCrashlytics>(relaxed = true)

        coEvery { FirebaseCrashlytics.getInstance() } returns crashlyticsMock

        mockServer.enqueueFile(200, ServerFiles.LOGIN)

        val sut = NetworkApiHelper().provideApi(mockServer.start(), EsmorgaAuthApi::class.java, getEsmorgaAuthenticatorMock(), getAuthInterceptor(), getDeviceInterceptor())

        val user = sut.login(body = mapOf("email" to "email", "password" to "password"))

        Assert.assertEquals("Albus", user.remoteProfile.remoteName)
    }

    @Test
    fun `given a login api call when there is no network available then crashlytics logs no connection`() = runTest {
        coEvery { mockNetworkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns false
        val crashlyticsMock = mockk<FirebaseCrashlytics>(relaxed = true)

        coEvery { FirebaseCrashlytics.getInstance() } returns crashlyticsMock
        mockServer.enqueueFile(200, ServerFiles.LOGIN)

        val sut = NetworkApiHelper().provideApi(mockServer.start(), EsmorgaAuthApi::class.java, getEsmorgaAuthenticatorMock(), getAuthInterceptor(), getDeviceInterceptor())

        sut.login(body = mapOf("email" to "email", "password" to "password"))
        verify {
            crashlyticsMock.log("No connectivity during backend call")
            crashlyticsMock.recordException(any(), any())
        }

    }

}