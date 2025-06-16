package cmm.apps.esmorga.common.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.provider.Settings
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


class ConnectivityUtilsTest {

    private lateinit var context: Context
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var network: Network
    private lateinit var networkCapabilities: NetworkCapabilities

    @Before
    fun setup() {
        context = mockk(relaxed = true)
        connectivityManager = mockk(relaxed = true)
        network = mockk(relaxed = true)
        networkCapabilities = mockk(relaxed = true)

        mockkStatic(Settings.Global::class, FirebaseCrashlytics::class)

        every { context.getSystemService(Context.CONNECTIVITY_SERVICE) } returns connectivityManager
        every {
            Settings.Global.getInt(context.contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0)
        } returns 0

        println("ConnectivityManager en setup: ${context.getSystemService(Context.CONNECTIVITY_SERVICE)}")
    }

    @After
    fun teardown() {
        unmockkStatic(Settings.Global::class, FirebaseCrashlytics::class)
    }

    @Test
    fun `given network is available, when isNetworkAvailable is called, then returns true`() {
        every { connectivityManager.activeNetwork } returns network
        every { connectivityManager.getNetworkCapabilities(network) } returns networkCapabilities
        every { networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns true

        val result = cmm.apps.esmorga.datasource_remote.firebase.ConnectivityUtils.isNetworkAvailable(context)

        assertTrue(result)
    }

    @Test
    fun `given network is not available, when isNetworkAvailable is called, then returns false`() {
        every { connectivityManager.activeNetwork } returns null

        val result = cmm.apps.esmorga.datasource_remote.firebase.ConnectivityUtils.isNetworkAvailable(context)

        assertFalse(result)
    }

    @Test
    fun `given airplane mode is on, when isAirplaneModeOn is called, then returns true`() {
        every {
            Settings.Global.getInt(context.contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0)
        } returns 1

        val result = cmm.apps.esmorga.datasource_remote.firebase.ConnectivityUtils.isAirplaneModeOn(context)

        assertTrue(result)
    }

    @Test
    fun `given airplane mode is off, when isAirplaneModeOn is called, then returns false`() {
        val result = cmm.apps.esmorga.datasource_remote.firebase.ConnectivityUtils.isAirplaneModeOn(context)

        assertFalse(result)
    }

    @Test
    fun `given no connectivity, when reportNoConnectivityIfNeeded is called, then logs and records exception is sent`() {
        val crashlyticsMock = mockk<FirebaseCrashlytics>(relaxed = true)
        every { FirebaseCrashlytics.getInstance() } returns crashlyticsMock

        cmm.apps.esmorga.datasource_remote.firebase.ConnectivityUtils.reportNoConnectivityIfNeeded(context)

        verify {
            crashlyticsMock.log("No connectivity during backend call")
            crashlyticsMock.recordException(any(), any())
        }
    }
}