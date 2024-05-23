package cmm.apps.esmorga.data.event.datasource

import cmm.apps.esmorga.data.event.model.EventDataModel
import cmm.apps.esmorga.domain.error.EsmorgaException
import cmm.apps.esmorga.domain.error.Source


interface EventDatasource {
    suspend fun getEvents(): List<EventDataModel>

    suspend fun cacheEvents(events: List<EventDataModel>) {
        throw EsmorgaException(message = "Unsupported operation", source = Source.UNSUPPORTED, code = -1)
    }

}