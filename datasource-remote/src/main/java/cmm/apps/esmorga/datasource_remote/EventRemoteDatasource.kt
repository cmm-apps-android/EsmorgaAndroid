package cmm.apps.esmorga.datasource_remote

import cmm.apps.esmorga.data.EventDataModel
import cmm.apps.esmorga.data.datasource.EventDatasource
import cmm.apps.esmorga.data.error.RemoteHttpException
import cmm.apps.esmorga.datasource_remote.api.EventApi
import retrofit2.HttpException
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException


class EventRemoteDatasourceImpl(private val eventApi: EventApi) : EventDatasource {

    override suspend fun getEvents(): List<EventDataModel> {
        try {
            val eventList = eventApi.getEvents()

            return eventList.remoteEventList.map { rev ->
                val parsedDate = try {
                    ZonedDateTime.from(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSVV").parse(rev.remoteDate))
                } catch (e: Exception) {
                    throw DateTimeParseException("Error parsing date in EventRemoteModel", rev.remoteDate, 0, e)
                }
                EventDataModel(dataName = rev.remoteName, dataDate = parsedDate)
            }
        } catch (httpEx: HttpException) {
            throw RemoteHttpException(code = httpEx.code(), message = httpEx.response()?.message().orEmpty())
        } catch (e: Exception){
            throw RemoteHttpException(code = -1, message = e.message.orEmpty())
        }
    }
}