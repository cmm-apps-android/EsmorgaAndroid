package cmm.apps.esmorga.datasource_remote.api.device

import android.content.SharedPreferences
import cmm.apps.esmorga.datasource_remote.api.device.DeviceConstants.DEVICE_ID_HEADER_KEY
import cmm.apps.esmorga.datasource_remote.api.device.DeviceConstants.DEVICE_ID_KEY
import okhttp3.Interceptor
import okhttp3.Response

class DeviceInterceptor(
    private val sharedPreferences: SharedPreferences
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        var deviceId = sharedPreferences.getString(DEVICE_ID_KEY, "")

        requestBuilder.addHeader(DEVICE_ID_HEADER_KEY, deviceId.orEmpty())

        return chain.proceed(requestBuilder.build())
    }
}