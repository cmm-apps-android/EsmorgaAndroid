package cmm.apps.esmorga.datasource_local.event

import cmm.apps.esmorga.data.event.datasource.EventDatasource
import cmm.apps.esmorga.data.event.model.EventAttendeeDataModel
import cmm.apps.esmorga.data.event.model.EventDataModel
import cmm.apps.esmorga.datasource_local.database.dao.EventAttendeeDao
import cmm.apps.esmorga.datasource_local.database.dao.EventDao
import cmm.apps.esmorga.datasource_local.event.mapper.toEventAttendeeDataModelList
import cmm.apps.esmorga.datasource_local.event.mapper.toEventDataModel
import cmm.apps.esmorga.datasource_local.event.mapper.toEventDataModelList
import cmm.apps.esmorga.datasource_local.event.mapper.toEventLocalModel
import cmm.apps.esmorga.datasource_local.event.mapper.toEventLocalModelList


class EventLocalDatasourceImpl(private val eventDao: EventDao, private val eventAttendeeDao: EventAttendeeDao) : EventDatasource {

    override suspend fun getEvents(): List<EventDataModel> {
        return eventDao.getEvents().toEventDataModelList()
    }

    override suspend fun getEventAttendees(eventId: String): List<EventAttendeeDataModel> {
        return eventAttendeeDao.getEventAttendees(eventId).toEventAttendeeDataModelList()
    }

    override suspend fun cacheEvents(events: List<EventDataModel>) {
        eventDao.deleteAll()
        eventDao.insertEvents(events.toEventLocalModelList())
    }

    override suspend fun getEventById(eventId: String): EventDataModel {
        return eventDao.getEventById(eventId).toEventDataModel()
    }

    override suspend fun deleteCacheEvents() {
        eventDao.deleteAll()
    }

    override suspend fun joinEvent(event: EventDataModel) {
        eventDao.updateEvent(event.toEventLocalModel())
    }

    override suspend fun leaveEvent(event: EventDataModel) {
        eventDao.updateEvent(event.toEventLocalModel())
    }
}