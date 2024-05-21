package cmm.apps.esmorga.data.event

import cmm.apps.esmorga.data.CacheHelper
import cmm.apps.esmorga.data.datasource.EventDatasource
import cmm.apps.esmorga.data.event.mapper.toEventList
import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.domain.repository.EventRepository

class EventRepositoryImpl(private val localDs: EventDatasource, private val remoteDs: EventDatasource) : EventRepository {

    override suspend fun getEvents(): List<Event> {
        val localList = localDs.getEvents()

        if(localList.isNotEmpty() && CacheHelper.shouldReturnCache(localList[0].creationTime)){
            return localList.toEventList()
        }

        val remoteList = remoteDs.getEvents()
        localDs.cacheEvents(remoteList)

        return remoteList.toEventList()
    }

}