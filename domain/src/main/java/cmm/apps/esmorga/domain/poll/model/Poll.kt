package cmm.apps.esmorga.domain.poll.model

import kotlinx.serialization.Serializable

@Serializable
data class Poll(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String?,
    val voteDeadline: Long,
    val isMultipleChoice: Boolean,
    val options: List<PollOption>
)

@Serializable
data class PollOption(
    val optionId: String,
    val text: String,
    val voteCount: Int,
    val userSelected: Boolean
)
