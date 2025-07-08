package cmm.apps.esmorga.domain.device

import cmm.apps.esmorga.domain.device.repository.DeviceRepository
import cmm.apps.esmorga.domain.result.EsmorgaResult

interface GetDeviceIdUseCase {
    suspend operator fun invoke(): EsmorgaResult<String>
}

class GetDeviceIdUseCaseImpl(
    private val repo: DeviceRepository
) : GetDeviceIdUseCase {
    override suspend fun invoke(): EsmorgaResult<String> {
        val result = repo.getDeviceId()
        return EsmorgaResult.success(result)
    }
}