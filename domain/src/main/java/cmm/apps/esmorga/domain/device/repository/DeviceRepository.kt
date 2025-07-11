package cmm.apps.esmorga.domain.device.repository

import cmm.apps.esmorga.domain.buildConfig.EsmorgaBuildConfig

interface DeviceRepository {
    fun getDeviceId(): String
    fun getEnvironment(): EsmorgaBuildConfig.Environment
}