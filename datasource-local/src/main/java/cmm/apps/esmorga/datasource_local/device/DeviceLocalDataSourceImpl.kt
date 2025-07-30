package cmm.apps.esmorga.datasource_local.device

import cmm.apps.esmorga.data.device.datasource.DeviceDataSource
import cmm.apps.esmorga.domain.buildConfig.EsmorgaBuildConfig

class DeviceLocalDataSourceImpl(private val buildConfigContract: EsmorgaBuildConfig) : DeviceDataSource {

    override fun getEnvironment(): EsmorgaBuildConfig.Environment {
        return buildConfigContract.getEnvironment()
    }
}