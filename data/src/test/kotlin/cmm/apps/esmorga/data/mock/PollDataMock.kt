package cmm.apps.esmorga.data.mock

import cmm.apps.esmorga.data.poll.model.PollDataModel
import cmm.apps.esmorga.data.poll.model.PollOptionDataModel


object PollDataMock {

    fun providePollDataModelList(nameList: List<String>): List<PollDataModel> = nameList.map { name -> providePollDataModel(name) }

    fun providePollDataModel(name: String): PollDataModel = PollDataModel(
        dataId = "$name-${System.currentTimeMillis()}",
        dataName = name,
        dataDescription = "description",
        dataImageUrl = null,
        dataVoteDeadline = System.currentTimeMillis(),
        dataIsMultipleChoice = true,
        dataOptions = listOf(
            PollOptionDataModel("1", "1", 0, false),
            PollOptionDataModel("2", "2", 0, false)
        )
    )

}