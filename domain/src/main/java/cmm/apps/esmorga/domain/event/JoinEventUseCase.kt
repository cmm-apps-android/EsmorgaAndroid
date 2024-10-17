package cmm.apps.esmorga.domain.event

import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.domain.event.repository.EventRepository
import cmm.apps.esmorga.domain.result.EsmorgaResult

interface JoinEventUseCase {
    suspend operator fun invoke(event: Event): EsmorgaResult<Unit>
}

class JoinEventUseCaseImpl(private val repo: EventRepository) : JoinEventUseCase {
    override suspend fun invoke(event: Event): EsmorgaResult<Unit> {
        try {
            repo.joinEvent(event)
            return EsmorgaResult.success(Unit)
        } catch (e: Exception) {
            return EsmorgaResult.failure(e)
        }
    }

}