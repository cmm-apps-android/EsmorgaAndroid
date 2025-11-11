package cmm.apps.esmorga.domain.poll

import cmm.apps.esmorga.domain.poll.model.Poll
import cmm.apps.esmorga.domain.poll.repository.PollRepository
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.EsmorgaResult

interface GetPollListUseCase {
    suspend operator fun invoke(forceRefresh: Boolean = false): EsmorgaResult<List<Poll>>
}

class GetPollListUseCaseImpl(private val repo: PollRepository) : GetPollListUseCase {
    override suspend fun invoke(forceRefresh: Boolean): EsmorgaResult<List<Poll>> {
        try {
            val result = repo.getPolls(forceRefresh)
            return EsmorgaResult.success(result)
        } catch (e: Exception) {
            if (e is EsmorgaException && e.code == ErrorCodes.NO_CONNECTION) {
                val localData = repo.getPolls(forceLocal = true)
                return EsmorgaResult.noConnectionError(localData)
            } else {
                return EsmorgaResult.failure(e)
            }
        }
    }
}
