package cmm.apps.esmorga.data.event

import cmm.apps.esmorga.data.CacheHelper
import cmm.apps.esmorga.data.event.datasource.EventDatasource
import cmm.apps.esmorga.data.event.mapper.toEventList
import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.domain.event.repository.EventRepository

class EventRepositoryImpl(private val localDs: EventDatasource, private val remoteDs: EventDatasource) : EventRepository {

    override suspend fun getEvents(forceRefresh: Boolean): List<Event> {
        val localList = localDs.getEvents()

        if(forceRefresh.not() && localList.isNotEmpty() && CacheHelper.shouldReturnCache(localList[0].dataCreationTime)){
            return localList.toEventList()
        }

        val remoteList = remoteDs.getEvents()
        localDs.cacheEvents(remoteList)

        return remoteList.toEventList()
    }

}