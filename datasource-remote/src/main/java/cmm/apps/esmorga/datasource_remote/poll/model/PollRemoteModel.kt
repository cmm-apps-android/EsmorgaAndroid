package cmm.apps.esmorga.datasource_remote.poll.model

import com.google.gson.annotations.SerializedName

data class PollListWrapperRemoteModel(
    @SerializedName("totalPolls") val remoteTotalPolls: Int,
    @SerializedName("polls") val remotePollList: List<PollRemoteModel>
)

data class PollRemoteModel(
    @SerializedName("pollId") val remoteId: String,
    @SerializedName("pollName") val remoteName: String,
    @SerializedName("description") val remoteDescription: String,
    @SerializedName("imageUrl") val remoteImageUrl: String?,
    @SerializedName("voteDeadline") val remoteVoteDeadline: String,
    @SerializedName("isMultipleChoice") val remoteIsMultipleChoice: Boolean,
    @SerializedName("options") val remotePollOptions: List<PollOptionRemoteModel>,
    @SerializedName("userSelectedOptions") val remoteUserSelectedOptions: List<String>
)

data class PollOptionRemoteModel(
    @SerializedName("optionId") val optionId: String,
    @SerializedName("option") val optionText: String,
    @SerializedName("voteCount") val optionVoteCount: Int
)

