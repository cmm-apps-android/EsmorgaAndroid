package cmm.apps.esmorga.datasource_local.mock

import cmm.apps.esmorga.datasource_local.event.model.EventLocalModel
import cmm.apps.esmorga.datasource_local.poll.model.PollLocalModel
import cmm.apps.esmorga.datasource_local.poll.model.PollOptionLocalModel
import cmm.apps.esmorga.domain.event.model.EventType
import java.time.ZonedDateTime


object PollLocalMock {

    fun providePollMap(nameList: List<String>): Map<PollLocalModel, List<PollOptionLocalModel>> =
        nameList.associate { name -> providePoll(name) to providePollOptionList(nameList) }

    fun providePoll(name: String): PollLocalModel =
        PollLocalModel(
            localId = "$name-${System.currentTimeMillis()}",
            localName = name,
            localDescription = "Description",
            localImageUrl = null,
            localVoteDeadline = System.currentTimeMillis() + 10000,
            localIsMultipleChoice = true,
            localCreationTime = System.currentTimeMillis()
        )

    fun providePollOptionList(nameList: List<String>): List<PollOptionLocalModel> = nameList.map { name -> providePollOption(name) }

    fun providePollOption(text: String): PollOptionLocalModel =
        PollOptionLocalModel(
            localPollId = "Poll1",
            localOptionId = "Option1",
            localText = text,
            localVoteCount = 0,
            localUserSelected = false
        )

}