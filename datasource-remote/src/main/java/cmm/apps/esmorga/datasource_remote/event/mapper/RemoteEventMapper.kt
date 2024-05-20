package cmm.apps.esmorga.datasource_remote.event.mapper

import cmm.apps.esmorga.data.event.model.EventDataModel
import cmm.apps.esmorga.datasource_remote.event.model.EventRemoteModel
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException


fun EventRemoteModel.toEventDataModel(): EventDataModel {
    val parsedDate = try {
        ZonedDateTime.from(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSVV").parse(this.remoteDate))
    } catch (e: Exception) {
        throw DateTimeParseException("Error parsing date in EventRemoteModel", this.remoteDate, 0, e)
    }
    return EventDataModel(dataName = this.remoteName, dataDate = parsedDate)
}

fun List<EventRemoteModel>.toEventDataModelList(): List<EventDataModel> = this.map { erm -> erm.toEventDataModel() }