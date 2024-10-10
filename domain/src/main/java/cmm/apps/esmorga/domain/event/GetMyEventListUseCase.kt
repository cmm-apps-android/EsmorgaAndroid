package cmm.apps.esmorga.domain.event

import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.domain.event.repository.EventRepository
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.domain.result.Success
import cmm.apps.esmorga.domain.user.repository.UserRepository

interface GetMyEventListUseCase {
    suspend operator fun invoke(forceRefresh: Boolean = false): EsmorgaResult<List<Event>>
}

class GetMyEventListUseCaseImpl(private val eventRepository: EventRepository, private val userRepository: UserRepository) : GetMyEventListUseCase {
    override suspend fun invoke(forceRefresh: Boolean): EsmorgaResult<List<Event>> {
        try {
            userRepository.getUser()
            val result = eventRepository.getEvents(forceRefresh)
            return EsmorgaResult(data = result.filter { it.userJoined })
        } catch (e: Exception) {
            if (e is EsmorgaException && e.code == ErrorCodes.NO_CONNECTION) {
                val localData = eventRepository.getEvents(forceLocal = true)
                return EsmorgaResult.noConnectionError(localData.filter { it.userJoined })
            } else {
                return EsmorgaResult.failure(e)
            }
        }
    }
}