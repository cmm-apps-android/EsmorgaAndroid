package cmm.apps.esmorga.domain.event

import cmm.apps.esmorga.domain.event.model.CreateEventForm
import cmm.apps.esmorga.domain.event.repository.EventRepository
import cmm.apps.esmorga.domain.result.EsmorgaResult

interface CreateEventUseCase {
    suspend operator fun invoke(eventForm: CreateEventForm): EsmorgaResult<Unit>
}

class CreateEventUseCaseImpl(private val repo: EventRepository) : CreateEventUseCase {
    override suspend fun invoke(eventForm: CreateEventForm): EsmorgaResult<Unit> {
        try {
            repo.createEvent(eventForm)
            return EsmorgaResult.success(Unit)
        } catch (e: Exception) {
            return EsmorgaResult.failure(e)
        }
    }
}

