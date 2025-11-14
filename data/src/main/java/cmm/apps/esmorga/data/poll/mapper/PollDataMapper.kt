package cmm.apps.esmorga.data.poll.mapper

import cmm.apps.esmorga.data.poll.model.PollDataModel
import cmm.apps.esmorga.data.poll.model.PollOptionDataModel
import cmm.apps.esmorga.domain.poll.model.Poll
import cmm.apps.esmorga.domain.poll.model.PollOption


fun PollDataModel.toPoll(): Poll = Poll(
    id = this.dataId,
    name = this.dataName,
    description = this.dataDescription,
    imageUrl = this.dataImageUrl,
    voteDeadline = this.dataVoteDeadline,
    isMultipleChoice = this.dataIsMultipleChoice,
    options = this.dataOptions.toPollOptionList()
)

private fun PollOptionDataModel.toPollOption() = PollOption(
    optionId = this.optionId,
    text = this.text,
    voteCount = this.voteCount,
    userSelected = this.userSelected
)

private fun List<PollOptionDataModel>.toPollOptionList() = this.map { prm -> prm.toPollOption() }

fun List<PollDataModel>.toPollList(): List<Poll> = map { edm -> edm.toPoll() }

fun Poll.toPollDataModel(): PollDataModel =
    PollDataModel(
        dataId = this.id,
        dataName = this.name,
        dataDescription = this.description,
        dataImageUrl = this.imageUrl,
        dataVoteDeadline = this.voteDeadline,
        dataIsMultipleChoice = this.isMultipleChoice,
        dataOptions = this.options.toPollOptionDataModelList()
    )

private fun PollOption.toPollOptionDataModel() = PollOptionDataModel(
    optionId = this.optionId,
    text = this.text,
    voteCount = this.voteCount,
    userSelected = this.userSelected
)

private fun List<PollOption>.toPollOptionDataModelList() = this.map { prm -> prm.toPollOptionDataModel() }