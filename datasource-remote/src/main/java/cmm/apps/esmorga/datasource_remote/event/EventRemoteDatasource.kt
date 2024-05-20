package cmm.apps.esmorga.datasource_remote.event

import cmm.apps.esmorga.data.event.model.EventDataModel
import cmm.apps.esmorga.data.datasource.EventDatasource
import cmm.apps.esmorga.data.error.RemoteHttpException
import cmm.apps.esmorga.datasource_remote.api.EventApi
import cmm.apps.esmorga.datasource_remote.event.mapper.toEventDataModelList
import retrofit2.HttpException
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException


class EventRemoteDatasourceImpl(private val eventApi: EventApi) : EventDatasource {

    override suspend fun getEvents(): List<EventDataModel> {
        try {
            val eventList = eventApi.getEvents()

            return eventList.remoteEventList.toEventDataModelList()
        } catch (httpEx: HttpException) {
            throw RemoteHttpException(code = httpEx.code(), message = httpEx.response()?.message().orEmpty())
        } catch (e: Exception){
            throw RemoteHttpException(code = -1, message = e.message.orEmpty())
        }
    }
}