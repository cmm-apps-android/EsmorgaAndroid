package cmm.apps.esmorga.data.poll.model


data class PollDataModel(
    val dataId: String,
    val dataName: String,
    val dataDescription: String,
    val dataImageUrl: String?,
    val dataVoteDeadline: Long,
    val dataIsMultipleChoice: Boolean,
    val dataOptions: List<PollOptionDataModel>,
    val dataCreationTime: Long = System.currentTimeMillis()
)

data class PollOptionDataModel(
    val optionId: String,
    val text: String,
    val voteCount: Int,
    val userSelected: Boolean
)
