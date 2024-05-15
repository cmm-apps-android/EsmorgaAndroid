package cmm.apps.esmorga.data

import cmm.apps.esmorga.data.datasource.EventDatasource
import cmm.apps.esmorga.domain.Event
import cmm.apps.esmorga.domain.repository.EventRepository

class EventRepositoryImpl(private val ds: EventDatasource) : EventRepository {

    override suspend fun getEvents(): List<Event> {
        val list = ds.getEvents()

        return list.map { edm -> Event(name = edm.dataName, date = edm.dataDate) } //TODO create mapper
    }

}