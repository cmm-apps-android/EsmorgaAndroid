package cmm.apps.esmorga.domain.event

import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.domain.event.repository.EventRepository
import cmm.apps.esmorga.domain.result.EsmorgaResult

interface LeaveEventUseCase {
    suspend operator fun invoke(event: Event): EsmorgaResult<Unit>
}

class LeaveEventUseCaseImpl(private val repo: EventRepository) : LeaveEventUseCase {
    override suspend fun invoke(event: Event): EsmorgaResult<Unit> {
        try {
            repo.leaveEvent(event)
            return EsmorgaResult.success(Unit)
        } catch (e: Exception) {
            return EsmorgaResult.failure(e)
        }
    }
}