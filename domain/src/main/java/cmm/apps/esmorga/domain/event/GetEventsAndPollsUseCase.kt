package cmm.apps.esmorga.domain.event

import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.domain.event.repository.EventRepository
import cmm.apps.esmorga.domain.poll.model.Poll
import cmm.apps.esmorga.domain.poll.repository.PollRepository
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.EsmorgaResult
import cmm.apps.esmorga.domain.user.model.User
import cmm.apps.esmorga.domain.user.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.util.concurrent.atomic.AtomicBoolean

interface GetEventsAndPollsUseCase {
    suspend operator fun invoke(forceRefresh: Boolean = false): EsmorgaResult<Pair<List<Event>, List<Poll>>>
}

class GetEventListUseCaseImpl(private val eventRepo: EventRepository, private val pollRepo: PollRepository, private val userRepository: UserRepository) : GetEventsAndPollsUseCase {
    override suspend fun invoke(forceRefresh: Boolean): EsmorgaResult<Pair<List<Event>, List<Poll>>> = try {
        coroutineScope {
            val isNoConnection = AtomicBoolean(false)

            val eventsDeferred = async {
                try {
                    eventRepo.getEvents(forceRefresh = forceRefresh)
                } catch (e: EsmorgaException) {
                    if (e.code == ErrorCodes.NO_CONNECTION) {
                        isNoConnection.set(true)
                        eventRepo.getEvents(forceLocal = true)
                    } else {
                        throw e
                    }
                }
            }


            val pollsDeferred = async {
                var user: User? = null
                try {
                    user = userRepository.getUser()
                } catch (_: Exception) {
                    //Do nothing, user not logged in
                }

                if (user != null) {
                    try {
                        pollRepo.getPolls(forceRefresh = forceRefresh)
                    } catch (e: EsmorgaException) {
                        if (e.code == ErrorCodes.NO_CONNECTION) {
                            isNoConnection.set(true)
                            pollRepo.getPolls(forceLocal = true)
                        } else {
                            throw e
                        }
                    }
                } else {
                    emptyList()
                }
            }

            val events = eventsDeferred.await()
            val polls = pollsDeferred.await()

            if (isNoConnection.get()) {
                EsmorgaResult.noConnectionError(Pair(events, polls))
            } else {
                EsmorgaResult.success(Pair(events, polls))
            }
        }
    } catch (e: Exception) {
        EsmorgaResult.failure(e)
    }
}
