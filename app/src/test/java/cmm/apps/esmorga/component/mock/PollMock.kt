package cmm.apps.esmorga.component.mock

import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.domain.event.model.EventLocation
import cmm.apps.esmorga.domain.event.model.EventType
import cmm.apps.esmorga.domain.poll.model.Poll
import cmm.apps.esmorga.domain.poll.model.PollOption
import java.time.ZonedDateTime


object PollMock {

    fun providePollModel(): Poll = Poll(
        id = "Poll-${System.currentTimeMillis()}",
        name = "Poll",
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