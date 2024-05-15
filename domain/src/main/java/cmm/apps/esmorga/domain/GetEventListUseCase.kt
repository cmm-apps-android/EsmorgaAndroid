package cmm.apps.esmorga.domain

import cmm.apps.esmorga.domain.repository.EventRepository
import kotlinx.coroutines.delay

interface GetEventListUseCase {
    suspend operator fun invoke(): List<Event>
}

class GetEventListUseCaseImpl(private val repo: EventRepository) : GetEventListUseCase {
    override suspend fun invoke(): List<Event> {

        delay(1000)

        val result = repo.getEvents()

        return result
    }
}