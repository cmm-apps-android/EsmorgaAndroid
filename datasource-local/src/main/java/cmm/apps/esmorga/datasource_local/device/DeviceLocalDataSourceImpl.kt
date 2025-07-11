package cmm.apps.esmorga.datasource_local.device

import android.content.SharedPreferences
import cmm.apps.esmorga.data.device.datasource.DeviceDataSource
import cmm.apps.esmorga.domain.buildConfig.EsmorgaBuildConfig
import java.util.UUID

class DeviceLocalDataSourceImpl(private val sharedPreferences: SharedPreferences, private val buildConfigContract: EsmorgaBuildConfig) : DeviceDataSource {

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

    override fun getEnvironment(): EsmorgaBuildConfig.Environment {
        return buildConfigContract.getEnvironment()
    }
}