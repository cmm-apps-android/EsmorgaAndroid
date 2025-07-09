package cmm.apps.esmorga.datasource_local.device

import android.content.SharedPreferences
import cmm.apps.datasource_local.BuildConfig
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import org.junit.Assert
import org.junit.Test

class DeviceLocalDataSourceImplTest {

    private var fakeDeviceId = "24601"

    @Test
    fun `given a device id storage when device id is requested then device id is successfully returned`() {
        val sut = DeviceLocalDataSourceImpl(provideFakeSharedPreferences())
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

    @Test
    fun `given DeviceLocalDataSourceImpl when getBuildType is called then returns BuildConfig FLAVOR`() {
        val sharedPreferences = mockk<SharedPreferences>(relaxed = true)
        val sut = DeviceLocalDataSourceImpl(sharedPreferences)

        val result = sut.getBuildType()

        Assert.assertEquals(BuildConfig.FLAVOR, result)
    }
}