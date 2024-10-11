package cmm.apps.esmorga.domain.event

import cmm.apps.esmorga.domain.event.repository.EventRepository
import cmm.apps.esmorga.domain.result.EsmorgaResult

interface JoinEventUseCase {
    suspend operator fun invoke(eventId: String): EsmorgaResult<Unit>
}

class JoinEventUseCaseImpl(private val repo: EventRepository) : JoinEventUseCase {
    override suspend fun invoke(eventId: String): EsmorgaResult<Unit> {
        try {
            repo.joinEvent(eventId)
            return EsmorgaResult.success(Unit)
        } catch (e: Exception) {
            return EsmorgaResult.failure(e)
        }
    }

}