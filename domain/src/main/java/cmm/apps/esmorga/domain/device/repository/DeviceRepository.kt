package cmm.apps.esmorga.domain.device.repository

interface DeviceRepository {
    fun getDeviceId(): String
    fun getBuildType(): String
}