package cmm.apps.esmorga.datasource_local.device

import android.content.SharedPreferences
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class DeviceLocalDataSourceImplTest {

    private var fakeDeviceId = "24601"

    private fun provideFakeSharedPreferences(): SharedPreferences {
        val sharedPreferences = mockk<SharedPreferences>(relaxed = true)
        val fakeSharedDeviceIdSlot = slot<String>()
        coEvery { sharedPreferences.getString("id_device", null) } coAnswers {
            fakeDeviceId
        }
        coEvery { sharedPreferences.edit().putString("id_device", capture(fakeSharedDeviceIdSlot)) } coAnswers {
            fakeDeviceId = fakeSharedDeviceIdSlot.captured
            sharedPreferences.edit()
        }
        return sharedPreferences
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `given a device id storage when device id is requested then device id is successfully returned`() = runTest {
        val sut = DeviceLocalDataSourceImpl(provideFakeSharedPreferences(), UnconfinedTestDispatcher())
        val result = sut.getDeviceId()

        Assert.assertEquals(fakeDeviceId, result)
    }

}