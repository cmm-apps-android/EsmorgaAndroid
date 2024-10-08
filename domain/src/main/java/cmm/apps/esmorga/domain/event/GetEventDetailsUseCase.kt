package cmm.apps.esmorga.domain.event

import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.domain.event.repository.EventRepository
import cmm.apps.esmorga.domain.result.EsmorgaResult

interface GetEventDetailsUseCase {
    suspend operator fun invoke(id: String): EsmorgaResult<Event>
}

class GetEventDetailsUseCaseImpl(private val repo: EventRepository) : GetEventDetailsUseCase {
    override suspend fun invoke(id: String): EsmorgaResult<Event> {
        return try {
            EsmorgaResult.success(repo.getEventDetails(id))
        } catch (e: Exception) {
            EsmorgaResult.failure(e)
        }
    }
}