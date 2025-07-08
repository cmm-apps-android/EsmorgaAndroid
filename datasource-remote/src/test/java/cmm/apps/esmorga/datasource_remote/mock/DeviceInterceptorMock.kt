package cmm.apps.esmorga.datasource_remote.mock

import android.content.SharedPreferences
import cmm.apps.esmorga.datasource_remote.api.device.DeviceInterceptor
import io.mockk.mockk

object DeviceInterceptorMock {
    fun getDeviceInterceptor() : DeviceInterceptor {
        val mockSharedPreferences: SharedPreferences = mockk(relaxed = true)
        return DeviceInterceptor(mockSharedPreferences)
    }
}