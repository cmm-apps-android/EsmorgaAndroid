package cmm.apps.esmorga.domain.event

import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.domain.event.repository.EventRepository

interface GetEventListUseCase {
    suspend operator fun invoke(forceRefresh: Boolean = false): Result<List<Event>>
}

class GetEventListUseCaseImpl(private val repo: EventRepository) : GetEventListUseCase {
    override suspend fun invoke(forceRefresh: Boolean): Result<List<Event>> {
        try {
            val result = repo.getEvents(forceRefresh)
            return Result.success(result)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}