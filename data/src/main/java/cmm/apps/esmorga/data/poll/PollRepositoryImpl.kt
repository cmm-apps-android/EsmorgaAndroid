package cmm.apps.esmorga.data.poll

import cmm.apps.esmorga.data.poll.datasource.PollDatasource
import cmm.apps.esmorga.data.poll.mapper.toPollList
import cmm.apps.esmorga.data.poll.model.PollDataModel
import cmm.apps.esmorga.domain.poll.model.Poll
import cmm.apps.esmorga.domain.poll.repository.PollRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class PollRepositoryImpl(
    private val remotePollDs: PollDatasource,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default
) : PollRepository {

    override suspend fun getPolls(forceRefresh: Boolean, forceLocal: Boolean): List<Poll> {
        //TODO
//        val localList = localEventDs.getEvents()
//
//        if (forceLocal || forceRefresh.not() && localList.isNotEmpty() && CacheHelper.shouldReturnCache(localList[0].dataCreationTime)) {
//            return localList.toEventList()
//        }

        return getPollsFromRemote().toPollList()
    }

    private suspend fun getPollsFromRemote(): List<PollDataModel> {
        val remotePollList = remotePollDs.getPolls()

        //TODO cache polls in local
        // localEventDs.cacheEvents(combinedList)
        return remotePollList
    }

}