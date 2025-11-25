package cmm.apps.esmorga.view.viewmodel.mock

import cmm.apps.esmorga.domain.poll.model.Poll
import cmm.apps.esmorga.domain.poll.model.PollOption


object PollViewMock {

    fun providePollList(nameList: List<String>): List<Poll> = nameList.map { name -> providePoll(name) }

    fun providePoll(
        name: String,
        id: String = "PollId",
        isMultipleChoice: Boolean = true,
        options: Map<String, Boolean> = mapOf("1" to false, "2" to false)
    ): Poll = Poll(
        id = id,
        name = name,
        description = "description",
        imageUrl = null,
        voteDeadline = System.currentTimeMillis() + 100000,
        isMultipleChoice = isMultipleChoice,
        options = options.map { (id, userVoted) -> PollOption(id, "1", 0, userVoted) }
    )

}