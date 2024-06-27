package cmm.apps.esmorga.datasource_remote.event

import cmm.apps.esmorga.data.event.datasource.EventDatasource
import cmm.apps.esmorga.data.event.model.EventDataModel
import cmm.apps.esmorga.datasource_remote.api.EventApi
import cmm.apps.esmorga.datasource_remote.event.mapper.toEventDataModelList
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.Source
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException
import java.time.format.DateTimeParseException


class EventRemoteDatasourceImpl(private val eventApi: EventApi) : EventDatasource {

    override suspend fun getEvents(): List<EventDataModel> {
        try {
            val eventList = eventApi.getEvents()
            return eventList.remoteEventList.toEventDataModelList()
        } catch (e: Exception) {
            when (e) {
                is HttpException -> throw EsmorgaException(message = e.response()?.message().orEmpty(), source = Source.REMOTE, code = e.code())
                is DateTimeParseException -> throw EsmorgaException(message = "Date parse error: ${e.message.orEmpty()}", source = Source.REMOTE, code = ErrorCodes.PARSE_ERROR)
                is ConnectException,
                is UnknownHostException -> throw EsmorgaException(message = "No connection error: ${e.message.orEmpty()}", source = Source.REMOTE, code = ErrorCodes.NO_CONNECTION)
                is EsmorgaException -> throw e
                else -> throw EsmorgaException(message = "Unexpected error: ${e.message.orEmpty()}", source = Source.REMOTE, code = ErrorCodes.UNKNOWN_ERROR)
            }
        }
    }

    override suspend fun getEventById(eventId: String): EventDataModel {
        throw EsmorgaException(message = "Unsupported operation", source = Source.UNSUPPORTED, code = ErrorCodes.UNSUPPORTED_OPERATION)
    }
}