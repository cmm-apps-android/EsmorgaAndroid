package cmm.apps.esmorga.datasource_local.poll

import cmm.apps.esmorga.data.poll.datasource.PollDatasource
import cmm.apps.esmorga.data.poll.model.PollDataModel
import cmm.apps.esmorga.datasource_local.database.dao.EventAttendeeDao
import cmm.apps.esmorga.datasource_local.database.dao.PollDao
import cmm.apps.esmorga.datasource_local.poll.mapper.toPollDataModelList
import cmm.apps.esmorga.datasource_local.poll.mapper.toPollLocalModelList
import cmm.apps.esmorga.datasource_local.poll.mapper.toPollOptionLocalModelList


class PollLocalDatasourceImpl(private val pollDao: PollDao) : PollDatasource {

    override suspend fun getPolls(): List<PollDataModel> {
        return pollDao.getPolls().toPollDataModelList()
    }

    override suspend fun cachePolls(polls: List<PollDataModel>) {
        deleteCachePolls()
        for (p in polls){
            pollDao.insertPollOptions(p.dataOptions.toPollOptionLocalModelList(p.dataId))
        }
        pollDao.insertPolls(polls.toPollLocalModelList())
    }

    override suspend fun deleteCachePolls() {
        pollDao.deleteAllPolls()
        pollDao.deleteAllPollOptions()
    }
}