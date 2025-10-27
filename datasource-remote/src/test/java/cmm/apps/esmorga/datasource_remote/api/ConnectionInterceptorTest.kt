package cmm.apps.esmorga.datasource_remote.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import okhttp3.Interceptor
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.M])
@RunWith(AndroidJUnit4::class)
class ConnectionInterceptorTest {

    private lateinit var mockNetworkCapabilities: NetworkCapabilities

    @Before
    fun init() {
        mockkStatic(FirebaseCrashlytics::class)

        val mockContext: Context = mockk(relaxed = true)
        val mockConnectivityManager: ConnectivityManager = mockk(relaxed = true)
        every { mockContext.getSystemService(Context.CONNECTIVITY_SERVICE) } returns mockConnectivityManager
        mockNetworkCapabilities = mockk(relaxed = true)
        every { mockConnectivityManager.getNetworkCapabilities(any()) } returns mockNetworkCapabilities

        startKoin {
            modules(module {
                single<Context> { mockContext }
            })
        }
    }

    @After
    fun shutDown() {
        stopKoin()
        clearMocks(mockNetworkCapabilities)
        unmockkAll()
    }

    @Test
    fun `given a working connection interceptor when there is no network available then crashlytics logs no connection`() = runTest {
        every { mockNetworkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns false
        val crashlyticsMock = mockk<FirebaseCrashlytics>(relaxed = true)
        every { FirebaseCrashlytics.getInstance() } returns crashlyticsMock

        val sut = ConnectionInterceptor
        sut.intercept(mockk<Interceptor.Chain>(relaxed = true))

        verify {
            crashlyticsMock.log("No connectivity during backend call")
            crashlyticsMock.recordException(any(), any())
        }
    }

    @Test
    fun `given a working connection interceptor when there is network available then crashlytics logs nothing`() = runTest {
        every { mockNetworkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns true
        val crashlyticsMock = mockk<FirebaseCrashlytics>(relaxed = true)
        every { FirebaseCrashlytics.getInstance() } returns crashlyticsMock

        val sut = ConnectionInterceptor
        sut.intercept(mockk<Interceptor.Chain>(relaxed = true))

        verify(exactly = 0) { crashlyticsMock.log(any()) }
        verify(exactly = 0) { crashlyticsMock.recordException(any(), any()) }
    }

}