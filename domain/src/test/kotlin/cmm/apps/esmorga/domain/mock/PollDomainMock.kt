package cmm.apps.esmorga.domain.mock

import cmm.apps.esmorga.domain.poll.model.Poll
import cmm.apps.esmorga.domain.poll.model.PollOption


object PollDomainMock {

    fun providePollList(nameList: List<String>): List<Poll> = nameList.map { name -> providePoll(name) }

    fun providePoll(name: String): Poll = Poll(
        id = "$name-${System.currentTimeMillis()}",
        name = name,
        description = "description",
        imageUrl = null,
        voteDeadline = System.currentTimeMillis() + 5000,
        isMultipleChoice = true,
        options = listOf(
            PollOption(optionId = "1", text = "1", voteCount = 0, userSelected = false),
            PollOption(optionId = "2", text = "2", voteCount = 0, userSelected = false),
        )
    )

}