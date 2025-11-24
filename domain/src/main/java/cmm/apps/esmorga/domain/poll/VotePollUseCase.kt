package cmm.apps.esmorga.domain.poll

import cmm.apps.esmorga.domain.poll.model.Poll
import cmm.apps.esmorga.domain.poll.model.PollOption
import cmm.apps.esmorga.domain.result.EsmorgaResult

interface VotePollUseCase {
    suspend operator fun invoke(): EsmorgaResult<Poll>
}

class VotePollUseCaseImpl() : VotePollUseCase {
    override suspend fun invoke(): EsmorgaResult<Poll> {
        //TODO
        val mockPoll = Poll(
            id = "1",
            name = "Poll",
            description = "description",
            imageUrl = null,
            voteDeadline = System.currentTimeMillis() + 5000,
            isMultipleChoice = true,
            options = listOf(
                PollOption(optionId = "1", text = "1", voteCount = 0, userSelected = true),
                PollOption(optionId = "2", text = "2", voteCount = 0, userSelected = false),
            )
        )
        return EsmorgaResult.success(mockPoll)
    }

}