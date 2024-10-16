package cmm.apps.esmorga.data.event

import cmm.apps.esmorga.data.CacheHelper
import cmm.apps.esmorga.data.event.datasource.EventDatasource
import cmm.apps.esmorga.data.event.mapper.toEvent
import cmm.apps.esmorga.data.event.mapper.toEventList
import cmm.apps.esmorga.data.event.model.EventDataModel
import cmm.apps.esmorga.data.user.datasource.UserDatasource
import cmm.apps.esmorga.data.user.model.UserDataModel
import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.domain.event.repository.EventRepository

class EventRepositoryImpl(private val localUserDs: UserDatasource, private val localEventDs: EventDatasource, private val remoteEventDs: EventDatasource) : EventRepository {

    override suspend fun getEvents(forceRefresh: Boolean, forceLocal: Boolean): List<Event> {
        val localList = localEventDs.getEvents()

        if (forceLocal || forceRefresh.not() && localList.isNotEmpty() && CacheHelper.shouldReturnCache(localList[0].dataCreationTime)) {
            return localList.toEventList()
        }

        return getEventsFromRemote().toEventList()
    }

    override suspend fun getEventDetails(eventId: String): Event {
        return localEventDs.getEventById(eventId).toEvent()
    }

    override suspend fun joinEvent(eventId: String) {
        val localEvent = localEventDs.getEventById(eventId).copy(dataUserJoined = true)
        remoteEventDs.joinEvent(localEvent)
        localEventDs.joinEvent(localEvent)
    }

    override suspend fun leaveEvent(eventId: String) {
        val localEvent = localEventDs.getEventById(eventId).copy(dataUserJoined = false)
        remoteEventDs.leaveEvent(localEvent)
        localEventDs.leaveEvent(localEvent)
    }

    private suspend fun getEventsFromRemote(): List<EventDataModel> {
        val combinedList = mutableListOf<EventDataModel>()
        val remoteEventList = remoteEventDs.getEvents()

        var user: UserDataModel? = null
        try {
            user = localUserDs.getUser()
        } catch (_: Exception) {
            //Do nothing, either user not logged in
        }

        if (user != null) {
            val myEvents = remoteEventDs.getMyEvents()

            combinedList.addAll(
                remoteEventList.map { event -> event.copy(dataUserJoined = myEvents.firstOrNull { me -> event.dataId == me.dataId } != null) }
            )
        } else {
            combinedList.addAll(remoteEventList)
        }

        localEventDs.cacheEvents(combinedList)
        return combinedList
    }

}