package cmm.apps.esmorga.data.poll

import cmm.apps.esmorga.data.CacheHelper
import cmm.apps.esmorga.data.poll.datasource.PollDatasource
import cmm.apps.esmorga.data.poll.mapper.toPollList
import cmm.apps.esmorga.data.poll.model.PollDataModel
import cmm.apps.esmorga.domain.poll.model.Poll
import cmm.apps.esmorga.domain.poll.repository.PollRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class PollRepositoryImpl(
    private val localPollDs: PollDatasource,
    private val remotePollDs: PollDatasource
) : PollRepository {

    override suspend fun getPolls(forceRefresh: Boolean, forceLocal: Boolean): List<Poll> {
        val localList = localPollDs.getPolls()

        if (forceLocal || forceRefresh.not() && localList.isNotEmpty() && CacheHelper.shouldReturnCache(localList[0].dataCreationTime)) {
            return localList.toPollList()
        }

        return getPollsFromRemote().toPollList()
    }

    private suspend fun getPollsFromRemote(): List<PollDataModel> {
        val remotePollList = remotePollDs.getPolls()

        localPollDs.cachePolls(remotePollList)
        return remotePollList
    }

}