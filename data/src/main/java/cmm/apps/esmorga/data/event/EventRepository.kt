package cmm.apps.esmorga.data.event

import cmm.apps.esmorga.data.datasource.EventDatasource
import cmm.apps.esmorga.data.error.RemoteHttpException
import cmm.apps.esmorga.data.event.mapper.toEventList
import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.domain.error.DataException
import cmm.apps.esmorga.domain.repository.EventRepository
import java.time.format.DateTimeParseException

class EventRepositoryImpl(private val ds: EventDatasource) : EventRepository {

    override suspend fun getEvents(): List<Event> {
        try {
            val list = ds.getEvents()

            return list.toEventList()
        } catch (parseEx: DateTimeParseException){
            throw DataException(code = -1, message = parseEx.message ?: "Date parsing Error")
        } catch (httpEx: RemoteHttpException){
            throw DataException(code = httpEx.code, message = "HTTP Error: response = ${httpEx.message}")
        }
    }

}