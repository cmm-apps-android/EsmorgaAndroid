package cmm.apps.esmorga.data.device

import cmm.apps.esmorga.data.device.datasource.DeviceDataSource
import cmm.apps.esmorga.domain.device.repository.DeviceRepository

class DeviceRepositoryImpl(private val localDataSource: DeviceDataSource) : DeviceRepository {
    override fun saveDeviceId(deviceId: String) {
        localDataSource.saveDeviceId(deviceId)
    }

    override fun getDeviceId(): String = localDataSource.getDeviceId()
}