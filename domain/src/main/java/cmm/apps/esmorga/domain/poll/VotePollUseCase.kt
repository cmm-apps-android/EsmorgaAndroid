package cmm.apps.esmorga.domain.poll

import cmm.apps.esmorga.domain.poll.model.Poll
import cmm.apps.esmorga.domain.poll.repository.PollRepository
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.EsmorgaResult

interface VotePollUseCase {
    suspend operator fun invoke(pollId: String, selectedOptions: List<String>): EsmorgaResult<Poll>
}

class VotePollUseCaseImpl(val pollRepo: PollRepository) : VotePollUseCase {
    override suspend fun invoke(pollId: String, selectedOptions: List<String>): EsmorgaResult<Poll> {
        try {
            return EsmorgaResult.success(pollRepo.votePoll(pollId, selectedOptions))
        } catch (e: EsmorgaException) {
            return EsmorgaResult.failure(e)
        }
    }

}