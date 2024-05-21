package cmm.apps.esmorga.data.event

import cmm.apps.esmorga.data.datasource.EventDatasource
import cmm.apps.esmorga.data.event.mapper.toEventList
import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.domain.repository.EventRepository

class EventRepositoryImpl(private val ds: EventDatasource) : EventRepository {

    override suspend fun getEvents(): List<Event> {
        val list = ds.getEvents()
        return list.toEventList()
    }

}