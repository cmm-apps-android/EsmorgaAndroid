package cmm.apps.esmorga.datasource_remote.mock

import cmm.apps.esmorga.datasource_remote.poll.model.PollListWrapperRemoteModel
import cmm.apps.esmorga.datasource_remote.poll.model.PollOptionRemoteModel
import cmm.apps.esmorga.datasource_remote.poll.model.PollRemoteModel


object PollRemoteMock {

    fun providePollListWrapper(nameList: List<String>): PollListWrapperRemoteModel {
        val list = providePollList(nameList)
        return PollListWrapperRemoteModel(list.size, list)
    }

    fun providePollList(nameList: List<String>): List<PollRemoteModel> = nameList.map { name -> providePoll(name) }

    fun providePoll(name: String): PollRemoteModel = PollRemoteModel(
        remoteId = "$name-${System.currentTimeMillis()}",
        remoteName = name,
        remoteDescription = "description",
        remoteImageUrl = null,
        remoteVoteDeadline = "2077-12-31T23:00:00.000Z",
        remoteIsMultipleChoice = true,
        remoteUserSelectedOptions = listOf(),
        remotePollOptions = listOf(
            PollOptionRemoteModel("1", "1", 0),
            PollOptionRemoteModel("2", "2", 0)
        )
    )

}