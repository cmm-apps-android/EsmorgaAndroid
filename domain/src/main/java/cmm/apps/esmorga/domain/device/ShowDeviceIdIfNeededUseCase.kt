package cmm.apps.esmorga.domain.device

import cmm.apps.esmorga.domain.device.repository.DeviceRepository
import cmm.apps.esmorga.domain.result.EsmorgaResult

interface ShowDeviceIdIfNeededUseCase {
    suspend operator fun invoke(): EsmorgaResult<Boolean>
}

class ShowDeviceIdIfNeededUseCaseImpl(
    private val repo: DeviceRepository
) : ShowDeviceIdIfNeededUseCase {
    override suspend fun invoke(): EsmorgaResult<Boolean> {
        val buildType = repo.getBuildType()
        val shouldShow = buildType.equals("qa", ignoreCase = true)
        return EsmorgaResult.success(shouldShow)
    }
}
