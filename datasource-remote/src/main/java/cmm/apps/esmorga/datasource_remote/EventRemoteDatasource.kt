package cmm.apps.esmorga.datasource_remote

import android.content.Context
import cmm.apps.esmorga.data.EventDataModel
import cmm.apps.esmorga.data.datasource.EventDatasource
import cmm.apps.esmorga.datasource_remote.api.EventApi
import cmm.apps.esmorga.datasource_remote.api.NetworkApiHelper
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException


class EventRemoteDatasourceImpl(private val context: Context) : EventDatasource {
    override suspend fun getEvents(): List<EventDataModel> {
        val eventApi = NetworkApiHelper(context).provideApi(
            baseUrl = EventApi.baseUrl(),
            clazz = EventApi::class.java
        )

        val eventList = eventApi.getEvents()

        return eventList.remoteEventList.map { rev ->
            val parsedDate = try {
                ZonedDateTime.from(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSVV").parse(rev.remoteDate))
            } catch (e: Exception) {
                throw DateTimeParseException("Error parsing date in EventRemoteModel", rev.remoteDate, 0, e)
            }
            EventDataModel(dataName = rev.remoteName, dataDate = parsedDate)
        }
    }
}