package cmm.apps.esmorga.datasource_remote.api.device

import cmm.apps.esmorga.data.device.datasource.DeviceDataSource
import cmm.apps.esmorga.datasource_remote.api.device.DeviceConstants.DEVICE_ID_HEADER_KEY
import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.component.KoinComponent

class DeviceInterceptor(
    private val deviceDataSource: DeviceDataSource
) : Interceptor, KoinComponent {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        var deviceId = deviceDataSource.getDeviceId()

        requestBuilder.addHeader(DEVICE_ID_HEADER_KEY, deviceId)

        return chain.proceed(requestBuilder.build())
    }
}