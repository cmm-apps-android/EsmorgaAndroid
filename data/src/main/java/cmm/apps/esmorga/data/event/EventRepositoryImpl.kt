package cmm.apps.esmorga.data.event

import cmm.apps.esmorga.data.CacheHelper
import cmm.apps.esmorga.data.event.datasource.EventDatasource
import cmm.apps.esmorga.data.event.mapper.toEventList
import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.domain.event.repository.EventRepository
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.Success

class EventRepositoryImpl(private val localDs: EventDatasource, private val remoteDs: EventDatasource) : EventRepository {

    override suspend fun getEvents(forceRefresh: Boolean): Success<List<Event>> {
        val localList = localDs.getEvents()

        if (forceRefresh.not() && localList.isNotEmpty() && CacheHelper.shouldReturnCache(localList[0].dataCreationTime)) {
            return Success(localList.toEventList())
        }

        try {
            val remoteList = remoteDs.getEvents()
            localDs.cacheEvents(remoteList)

            return Success(remoteList.toEventList())
        } catch (esmorgaEx: EsmorgaException) {
            if (esmorgaEx.code == ErrorCodes.NO_CONNECTION) {
                return Success(localList.toEventList(), ErrorCodes.NO_CONNECTION)
            } else {
                throw esmorgaEx
            }
        }
    }

}