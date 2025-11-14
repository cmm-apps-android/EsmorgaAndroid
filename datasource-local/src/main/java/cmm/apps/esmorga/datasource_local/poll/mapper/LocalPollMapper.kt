package cmm.apps.esmorga.datasource_local.poll.mapper

import cmm.apps.esmorga.data.poll.model.PollDataModel
import cmm.apps.esmorga.data.poll.model.PollOptionDataModel
import cmm.apps.esmorga.datasource_local.poll.model.PollLocalModel
import cmm.apps.esmorga.datasource_local.poll.model.PollOptionLocalModel


fun PollLocalModel.toPollDataModel(options: List<PollOptionLocalModel>): PollDataModel {
    return PollDataModel(
        dataId = this.localId,
        dataName = this.localName,
        dataDescription = this.localDescription,
        dataImageUrl = this.localImageUrl,
        dataVoteDeadline = this.localVoteDeadline,
        dataIsMultipleChoice = this.localIsMultipleChoice,
        dataOptions = options.toPollOptionDataModelList(),
        dataCreationTime = localCreationTime,
    )
}

fun PollOptionLocalModel.toPollOptionDataModel(): PollOptionDataModel {
    return PollOptionDataModel(
        optionId = this.localOptionId,
        text = this.localText,
        voteCount = this.localVoteCount,
        userSelected = this.localUserSelected
    )
}

fun List<PollOptionLocalModel>.toPollOptionDataModelList(): List<PollOptionDataModel> = this.map { o -> o.toPollOptionDataModel() }

fun Map<PollLocalModel, List<PollOptionLocalModel>>.toPollDataModelList(): List<PollDataModel> = this.map { (plm, options) -> plm.toPollDataModel(options) }

fun PollDataModel.toPollLocalModel(): PollLocalModel {
    return PollLocalModel(
        localId = this.dataId,
        localName = this.dataName,
        localDescription = this.dataDescription,
        localImageUrl = this.dataImageUrl,
        localVoteDeadline = this.dataVoteDeadline,
        localIsMultipleChoice = this.dataIsMultipleChoice,
        localCreationTime = this.dataCreationTime
    )
}

fun List<PollDataModel>.toPollLocalModelList(): List<PollLocalModel> = this.map { plm -> plm.toPollLocalModel() }

fun PollOptionDataModel.toPollOptionLocalModel(pollId: String): PollOptionLocalModel {
    return PollOptionLocalModel(
        localPollId = pollId,
        localOptionId = this.optionId,
        localText = this.text,
        localVoteCount = this.voteCount,
        localUserSelected = this.userSelected
    )
}

fun List<PollOptionDataModel>.toPollOptionLocalModelList(pollId: String): List<PollOptionLocalModel> = this.map { plm -> plm.toPollOptionLocalModel(pollId) }