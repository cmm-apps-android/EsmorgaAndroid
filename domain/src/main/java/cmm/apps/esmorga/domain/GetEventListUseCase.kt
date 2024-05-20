package cmm.apps.esmorga.domain

import cmm.apps.esmorga.domain.repository.EventRepository
import kotlinx.coroutines.delay

interface GetEventListUseCase {
    suspend operator fun invoke(): Result<List<Event>>
}

class GetEventListUseCaseImpl(private val repo: EventRepository) : GetEventListUseCase {
    override suspend fun invoke(): Result<List<Event>> {

        delay(1000)

        try {
            val result = repo.getEvents()
            return Result.success(result)
        } catch (e: Exception){
            return Result.failure(e)
        }
    }
}