package cmm.apps.esmorga.datasource_local.device

import android.content.SharedPreferences
import androidx.core.content.edit
import cmm.apps.esmorga.data.device.datasource.DeviceDataSource
import cmm.apps.esmorga.domain.buildConfig.EsmorgaBuildConfig
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class DeviceLocalDataSourceImpl(
    private val sharedPreferences: SharedPreferences,
    private val buildConfigContract: EsmorgaBuildConfig,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : DeviceDataSource {

    companion object {
        private const val DEVICE_ID_KEY = "id_device"
    }

    private fun saveDeviceId(deviceId: String) {
        sharedPreferences.edit { putString(DEVICE_ID_KEY, deviceId) }
    }

    override suspend fun getDeviceId(): String = withContext(coroutineDispatcher) {
        sharedPreferences.getString(DEVICE_ID_KEY, null) ?: UUID.randomUUID().toString().also {
            saveDeviceId(it)
        }
    }

    override suspend fun getEnvironment(): EsmorgaBuildConfig.Environment {
        return buildConfigContract.getEnvironment()
    }
}