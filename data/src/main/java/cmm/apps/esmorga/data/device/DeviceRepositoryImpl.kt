package cmm.apps.esmorga.data.device

import cmm.apps.esmorga.data.device.datasource.DeviceDataSource
import cmm.apps.esmorga.domain.buildConfig.EsmorgaBuildConfig
import cmm.apps.esmorga.domain.device.repository.DeviceRepository

class DeviceRepositoryImpl(private val localDataSource: DeviceDataSource) : DeviceRepository {

    override fun getDeviceId(): String = localDataSource.getDeviceId()

    override fun getEnvironment(): EsmorgaBuildConfig.Environment = localDataSource.getEnvironment()
}