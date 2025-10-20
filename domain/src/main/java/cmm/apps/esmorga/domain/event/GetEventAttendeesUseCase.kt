package cmm.apps.esmorga.domain.event

import cmm.apps.esmorga.domain.event.model.EventAttendee
import cmm.apps.esmorga.domain.event.repository.EventRepository
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.EsmorgaResult

interface GetEventAttendeesUseCase {
    suspend operator fun invoke(eventId: String): EsmorgaResult<List<EventAttendee>>
}

class GetEventAttendeesUseCaseImpl(private val repo: EventRepository) : GetEventAttendeesUseCase {
    override suspend fun invoke(eventId: String): EsmorgaResult<List<EventAttendee>> {
        try {
            val result = repo.getEventAttendees(eventId)
            return EsmorgaResult.success(result)
        } catch (e: Exception) {
            if (e is EsmorgaException && e.code == ErrorCodes.NO_CONNECTION) {
                val localData = repo.getEventAttendees(eventId)
                return EsmorgaResult.noConnectionError(localData)
            } else {
                return EsmorgaResult.failure(e)
            }
        }
    }
}
