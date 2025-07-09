package cmm.apps.esmorga.datasource_local.device

import android.content.SharedPreferences
import cmm.apps.datasource_local.BuildConfig
import cmm.apps.esmorga.data.device.datasource.DeviceDataSource
import java.util.UUID

class DeviceLocalDataSourceImpl(private val sharedPreferences: SharedPreferences) : DeviceDataSource {

    companion object {
        private const val DEVICE_ID_KEY = "id_device"
    }

    private fun saveDeviceId(deviceId: String) {
        sharedPreferences.edit().putString(DEVICE_ID_KEY, deviceId).apply()
    }

    override fun getDeviceId(): String {
        var uuid = sharedPreferences.getString(DEVICE_ID_KEY, "").orEmpty()
        if (uuid.isEmpty()) {
            uuid = UUID.randomUUID().toString()
            saveDeviceId(uuid)
        }
        return uuid
    }

    override fun getBuildType(): String {
        return BuildConfig.FLAVOR
    }
}