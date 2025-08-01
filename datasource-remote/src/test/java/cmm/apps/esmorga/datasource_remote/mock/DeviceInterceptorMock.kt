package cmm.apps.esmorga.datasource_remote.mock

import cmm.apps.esmorga.data.device.datasource.DeviceDataSource
import cmm.apps.esmorga.datasource_remote.api.device.DeviceInterceptor
import io.mockk.mockk

object DeviceInterceptorMock {
    fun getDeviceInterceptor(): DeviceInterceptor {
        val mockDeviceDataSource = mockk<DeviceDataSource>(relaxed = true)
        return DeviceInterceptor(mockDeviceDataSource)
    }
}