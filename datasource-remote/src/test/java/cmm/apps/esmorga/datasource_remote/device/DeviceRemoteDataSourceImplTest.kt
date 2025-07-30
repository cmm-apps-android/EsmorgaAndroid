package cmm.apps.esmorga.datasource_remote.device

import android.content.SharedPreferences
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import org.junit.Assert
import org.junit.Test

class DeviceRemoteDataSourceImplTest {
    private var fakeDeviceId = "device-id"

    @Test
    fun `given a device id storage when device id is requested then device id is successfully returned`() {
        val sut = DeviceRemoteDataSourceImpl(provideFakeSharedPreferences())
        val result = sut.getDeviceId()

        Assert.assertEquals(fakeDeviceId, result)
    }

    private fun provideFakeSharedPreferences(): SharedPreferences {
        val sharedPreferences = mockk<SharedPreferences>(relaxed = true)
        val fakeSharedDeviceIdSlot = slot<String>()
        coEvery { sharedPreferences.getString("id_device", "") } coAnswers {
            fakeDeviceId
        }
        coEvery { sharedPreferences.edit().putString("id_device", capture(fakeSharedDeviceIdSlot)).apply() } coAnswers {
            fakeDeviceId = fakeSharedDeviceIdSlot.captured
        }
        return sharedPreferences
    }
}