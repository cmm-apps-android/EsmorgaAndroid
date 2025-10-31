package cmm.apps.esmorga.datasource_remote.api.device

import cmm.apps.esmorga.data.device.datasource.DeviceDataSource
import cmm.apps.esmorga.datasource_remote.api.device.DeviceConstants.DEVICE_ID_HEADER_KEY
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class DeviceInterceptor(
    private val deviceDataSource: DeviceDataSource
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = runBlocking {
        val requestBuilder = chain.request().newBuilder()
        val deviceId = deviceDataSource.getDeviceId()

        requestBuilder.addHeader(DEVICE_ID_HEADER_KEY, deviceId)

        chain.proceed(requestBuilder.build())
    }
}