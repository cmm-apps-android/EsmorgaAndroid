package cmm.apps.esmorga.domain.device

import cmm.apps.esmorga.domain.buildConfig.EsmorgaBuildConfig
import cmm.apps.esmorga.domain.device.repository.DeviceRepository
import cmm.apps.esmorga.domain.result.EsmorgaResult

interface ShowDeviceIdIfNeededUseCase {
    suspend operator fun invoke(): EsmorgaResult<String?>
}

class ShowDeviceIdIfNeededUseCaseImpl(
    private val repo: DeviceRepository
) : ShowDeviceIdIfNeededUseCase {
    override suspend fun invoke(): EsmorgaResult<String?> {
        val buildType = repo.getEnvironment()
        val shouldShow = buildType == EsmorgaBuildConfig.Environment.QA
        val deviceId = repo.getDeviceId()
        val result = if (shouldShow) deviceId else null
        return EsmorgaResult.success(result)
    }
}
