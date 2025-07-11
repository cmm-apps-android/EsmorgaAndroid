package cmm.apps.esmorga.datasource_local.device

import android.content.SharedPreferences
import cmm.apps.esmorga.domain.buildConfig.EsmorgaBuildConfig
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import org.junit.Assert
import org.junit.Test

class DeviceLocalDataSourceImplTest {

    private var fakeDeviceId = "24601"
    private val buildConfig: EsmorgaBuildConfig = mockk(relaxed = true)

    @Test
    fun `given a device id storage when device id is requested then device id is successfully returned`() {
        val sut = DeviceLocalDataSourceImpl(provideFakeSharedPreferences(), buildConfig)
        val result = sut.getDeviceId()

        Assert.assertEquals(fakeDeviceId, result)

    }

    @Test
    fun `given device datasource in qa environment when get the build type then returns qa`() {
        val sharedPreferences = mockk<SharedPreferences>(relaxed = true)
        val sut = DeviceLocalDataSourceImpl(sharedPreferences, buildConfig)

        coEvery { buildConfig.getEnvironment() } returns EsmorgaBuildConfig.Environment.QA

        val result = sut.getEnvironment()

        Assert.assertEquals(buildConfig.getEnvironment(), result)
    }

    @Test
    fun `given device datasource in prod environment when get the build type then returns prod`() {
        val sharedPreferences = mockk<SharedPreferences>(relaxed = true)
        val sut = DeviceLocalDataSourceImpl(sharedPreferences, buildConfig)

        coEvery { buildConfig.getEnvironment() } returns EsmorgaBuildConfig.Environment.PROD

        val result = sut.getEnvironment()

        Assert.assertEquals(buildConfig.getEnvironment(), result)
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