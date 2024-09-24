package cmm.apps.esmorga.data.event.datasource

import cmm.apps.esmorga.data.event.model.EventDataModel
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.Source


interface EventDatasource {
    suspend fun getEvents(): List<EventDataModel>

    suspend fun getMyEvents(): List<EventDataModel> {
        throw EsmorgaException(message = "Unsupported operation", source = Source.UNSUPPORTED, code = ErrorCodes.UNSUPPORTED_OPERATION)
    }

    suspend fun cacheEvents(events: List<EventDataModel>) {
        throw EsmorgaException(message = "Unsupported operation", source = Source.UNSUPPORTED, code = ErrorCodes.UNSUPPORTED_OPERATION)
    }

    suspend fun getEventById(eventId: String): EventDataModel {
        throw EsmorgaException(message = "Unsupported operation", source = Source.UNSUPPORTED, code = ErrorCodes.UNSUPPORTED_OPERATION)
    }
}