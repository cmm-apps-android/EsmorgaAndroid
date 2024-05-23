package cmm.apps.esmorga.datasource_remote.event

import cmm.apps.esmorga.data.event.model.EventDataModel
import cmm.apps.esmorga.data.event.datasource.EventDatasource
import cmm.apps.esmorga.datasource_remote.api.EventApi
import cmm.apps.esmorga.datasource_remote.event.mapper.toEventDataModelList
import cmm.apps.esmorga.domain.error.EsmorgaException
import cmm.apps.esmorga.domain.error.Source
import retrofit2.HttpException
import java.time.format.DateTimeParseException


class EventRemoteDatasourceImpl(private val eventApi: EventApi) : EventDatasource {

    override suspend fun getEvents(): List<EventDataModel> {
        try {
            val eventList = eventApi.getEvents()

            return eventList.remoteEventList.toEventDataModelList()
        } catch (httpEx: HttpException) {
            throw EsmorgaException(message = httpEx.response()?.message().orEmpty(), source = Source.REMOTE, code = httpEx.code())
        } catch (parseEx: DateTimeParseException){
            throw EsmorgaException(message = "Date parse error: ${parseEx.message.orEmpty()}", source = Source.REMOTE, code = -1)
        } catch (e: Exception){
            throw EsmorgaException(message = "Unexpected error: ${e.message.orEmpty()}", source = Source.REMOTE, code = -1)
        }
    }
}