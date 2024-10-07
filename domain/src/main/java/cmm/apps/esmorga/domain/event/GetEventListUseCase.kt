package cmm.apps.esmorga.domain.event

import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.domain.event.repository.EventRepository
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.EsmorgaResult

interface GetEventListUseCase {
    suspend operator fun invoke(forceRefresh: Boolean = false): EsmorgaResult<List<Event>>
}

class GetEventListUseCaseImpl(private val repo: EventRepository) : GetEventListUseCase {
    override suspend fun invoke(forceRefresh: Boolean): EsmorgaResult<List<Event>> {
        try {
            val result = repo.getEvents(forceRefresh)
            return EsmorgaResult.success(result)
        } catch (e: Exception) {
            if (e is EsmorgaException && e.code == ErrorCodes.NO_CONNECTION) {
                val localData = repo.getEvents(forceLocal = true)
                return EsmorgaResult.noConnectionError(localData)
            } else {
                return EsmorgaResult.failure(e)
            }
        }
    }
}
