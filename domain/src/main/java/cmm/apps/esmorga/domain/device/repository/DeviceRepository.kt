package cmm.apps.esmorga.domain.device.repository

interface DeviceRepository {
    fun saveDeviceId(deviceId: String)
    fun getDeviceId(): String
}