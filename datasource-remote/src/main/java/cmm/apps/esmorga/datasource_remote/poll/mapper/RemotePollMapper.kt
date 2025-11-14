package cmm.apps.esmorga.datasource_remote.poll.mapper

import cmm.apps.esmorga.data.poll.model.PollDataModel
import cmm.apps.esmorga.data.poll.model.PollOptionDataModel
import cmm.apps.esmorga.datasource_remote.dateformatting.EsmorgaRemoteDateFormatter
import cmm.apps.esmorga.datasource_remote.poll.model.PollOptionRemoteModel
import cmm.apps.esmorga.datasource_remote.poll.model.PollRemoteModel


fun PollRemoteModel.toPollDataModel(dateFormatter: EsmorgaRemoteDateFormatter): PollDataModel {
    val parsedDateline = dateFormatter.parseIsoDateTime(this.remoteVoteDeadline)

    return PollDataModel(
        dataId = this.remoteId,
        dataName = this.remoteName,
        dataDescription = this.remoteDescription,
        dataImageUrl = this.remoteImageUrl,
        dataVoteDeadline = parsedDateline.toInstant().toEpochMilli(),
        dataIsMultipleChoice = this.remoteIsMultipleChoice,
        dataOptions = this.remotePollOptions.toDataModelList(remoteUserSelectedOptions)
    )
}

private fun PollOptionRemoteModel.toPollOptionDataModel(userSelectedOptions: List<String>) = PollOptionDataModel(
    optionId = this.optionId,
    text = this.optionText,
    voteCount = this.optionVoteCount,
    userSelected = userSelectedOptions.contains(this.optionId)
)

private fun List<PollOptionRemoteModel>.toDataModelList(userSelectedOptions: List<String>) = this.map { prm -> prm.toPollOptionDataModel(userSelectedOptions) }

fun List<PollRemoteModel>.toPollDataModelList(dateFormatter: EsmorgaRemoteDateFormatter): List<PollDataModel> = this.map { erm -> erm.toPollDataModel(dateFormatter) }
