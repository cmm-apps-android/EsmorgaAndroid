package cmm.apps.esmorga.domain.event

import cmm.apps.esmorga.domain.event.model.EventAttendee
import cmm.apps.esmorga.domain.event.repository.EventRepository
import cmm.apps.esmorga.domain.result.EsmorgaResult

interface UpdateEventAttendeeUseCase {
    suspend operator fun invoke(attendee: EventAttendee): EsmorgaResult<Unit>
}

class UpdateEventAttendeeUseCaseImpl(private val repo: EventRepository) : UpdateEventAttendeeUseCase {
    override suspend fun invoke(attendee: EventAttendee): EsmorgaResult<Unit> {
        try {
            repo.updateEventAttendee(attendee)
            return EsmorgaResult.success(Unit)
        } catch (e: Exception) {
            return EsmorgaResult.failure(e)
        }
    }
}
