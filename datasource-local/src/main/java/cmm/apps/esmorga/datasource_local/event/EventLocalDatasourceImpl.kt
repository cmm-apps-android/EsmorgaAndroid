package cmm.apps.esmorga.datasource_local.event

import cmm.apps.esmorga.data.event.datasource.EventDatasource
import cmm.apps.esmorga.data.event.model.EventDataModel
import cmm.apps.esmorga.datasource_local.database.dao.EventDao
import cmm.apps.esmorga.datasource_local.event.mapper.toEventDataModel
import cmm.apps.esmorga.datasource_local.event.mapper.toEventDataModelList
import cmm.apps.esmorga.datasource_local.event.mapper.toEventLocalModelList


class EventLocalDatasourceImpl(private val eventDao: EventDao) : EventDatasource {

    override suspend fun getEvents(): List<EventDataModel> {
        return eventDao.getEvents().toEventDataModelList()
    }

    override suspend fun cacheEvents(events: List<EventDataModel>) {
        eventDao.deleteAll()
        eventDao.insertEvent(events.toEventLocalModelList())
    }

    override suspend fun getEventById(eventId: String): EventDataModel {
        return eventDao.getEventById(eventId).toEventDataModel()
    }

    override suspend fun deleteCacheEvents() {
        eventDao.deleteAll()
    }

    override suspend fun joinEvent(eventId: String) {
        val events = eventDao.getEvents().map {
            if (it.localId == eventId) {
                it.copy(localUserJoined = true)
            } else {
                it
            }
        }
        eventDao.insertEvent(events)
    }

    override suspend fun leaveEvent(eventId: String) {
        val events = eventDao.getEvents().map {
            if (it.localId == eventId) {
                it.copy(localUserJoined = false)
            } else {
                it
            }
        }
        eventDao.insertEvent(events)
    }
}