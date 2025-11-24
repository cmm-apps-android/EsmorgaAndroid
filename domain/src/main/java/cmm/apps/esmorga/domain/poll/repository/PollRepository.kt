package cmm.apps.esmorga.domain.poll.repository

import cmm.apps.esmorga.domain.poll.model.Poll


interface PollRepository {
    suspend fun getPolls(forceRefresh: Boolean = false, forceLocal: Boolean = false): List<Poll>
    suspend fun votePoll(pollId:String, selectedOptions:List<String>): Poll
}