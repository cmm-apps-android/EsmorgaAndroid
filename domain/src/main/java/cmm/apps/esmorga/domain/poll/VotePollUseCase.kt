package cmm.apps.esmorga.domain.poll

import cmm.apps.esmorga.domain.result.EsmorgaResult

interface VotePollUseCase {
    suspend operator fun invoke(): EsmorgaResult<Unit>
}

class VotePollUseCaseImpl() : VotePollUseCase {
    override suspend fun invoke(): EsmorgaResult<Unit> {
        //TODO
        return EsmorgaResult.success(Unit)
    }

}