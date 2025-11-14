package cmm.apps.esmorga.view.viewmodel.mock

import cmm.apps.esmorga.domain.event.model.EventLocation
import cmm.apps.esmorga.domain.event.model.EventType
import cmm.apps.esmorga.domain.poll.model.Poll
import cmm.apps.esmorga.domain.poll.model.PollOption
import java.time.ZonedDateTime


object PollViewMock {

    fun providePollList(nameList: List<String>): List<Poll> = nameList.map { name -> providePoll(name) }

    fun providePoll(
        name: String,
        id: String = "PollId"
    ): Poll = Poll(
        id = id,
        name = name,
        description = "description",
        imageUrl = null,
        voteDeadline = System.currentTimeMillis(),
        isMultipleChoice = true,
        options = listOf(PollOption("1", "1", 0, false), PollOption("2", "2", 0, false))
    )

}