package cmm.apps.esmorga.domain.event

import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.domain.event.repository.EventRepository
import cmm.apps.esmorga.domain.result.Success
import kotlinx.coroutines.delay

interface GetEventListUseCase {
    suspend operator fun invoke(forceRefresh: Boolean = false): Result<Success<List<Event>>>
}

class GetEventListUseCaseImpl(private val repo: EventRepository) : GetEventListUseCase {
    override suspend fun invoke(forceRefresh: Boolean): Result<Success<List<Event>>> {
        try {
            val result = repo.getEvents(forceRefresh)
            return Result.success(result)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}