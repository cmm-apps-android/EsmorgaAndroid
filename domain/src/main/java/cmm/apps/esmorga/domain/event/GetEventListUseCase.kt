package cmm.apps.esmorga.domain.event

import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.domain.repository.EventRepository

interface GetEventListUseCase {
    suspend operator fun invoke(): Result<List<Event>>
}

class GetEventListUseCaseImpl(private val repo: EventRepository) : GetEventListUseCase {
    override suspend fun invoke(): Result<List<Event>> {
        try {
            val result = repo.getEvents()
            return Result.success(result)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}