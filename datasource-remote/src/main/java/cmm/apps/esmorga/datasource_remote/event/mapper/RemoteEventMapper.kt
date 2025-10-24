package cmm.apps.esmorga.datasource_remote.event.mapper

import cmm.apps.esmorga.data.event.model.EventDataModel
import cmm.apps.esmorga.data.event.model.EventLocationDataModel
import cmm.apps.esmorga.datasource_remote.dateformatting.EsmorgaRemoteDateFormatter
import cmm.apps.esmorga.datasource_remote.event.model.EventLocationRemoteModel
import cmm.apps.esmorga.datasource_remote.event.model.EventRemoteModel
import cmm.apps.esmorga.domain.event.model.EventType
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.Source

fun EventRemoteModel.toEventDataModel(dateFormatter: EsmorgaRemoteDateFormatter): EventDataModel {

    val parsedDate = dateFormatter.parseIsoDateTime(this.remoteDate)

    val parsedType = try {
        EventType.valueOf(this.remoteType.uppercase())
    } catch (e: Exception) {
        throw EsmorgaException(message = "Error parsing type [${this.remoteType.uppercase()}] in EventRemoteModel", source = Source.REMOTE, code = ErrorCodes.PARSE_ERROR)
    }

    val parsedJoinDeadline = dateFormatter.parseIsoDateTime(this.remoteJoinDeadline)

    return EventDataModel(
        dataId = this.remoteId,
        dataName = this.remoteName,
        dataDate = parsedDate.toInstant().toEpochMilli(),
        dataDescription = this.remoteDescription,
        dataType = parsedType,
        dataImageUrl = this.remoteImageUrl,
        dataLocation = this.remoteLocation.toEventLocationDataModel(),
        dataTags = this.remoteTags ?: listOf(),
        dataUserJoined = false,
        dataCurrentAttendeeCount = this.remoteCurrentAttendeeCount,
        dataMaxCapacity = this.remoteMaxCapacity,
        dataJoinDeadline = parsedJoinDeadline.toInstant().toEpochMilli()
    )
}

fun List<EventRemoteModel>.toEventDataModelList(dateFormatter: EsmorgaRemoteDateFormatter): List<EventDataModel> = this.map { erm -> erm.toEventDataModel(dateFormatter) }

fun EventLocationRemoteModel.toEventLocationDataModel(): EventLocationDataModel = EventLocationDataModel(
    name = this.remoteLocationName,
    lat = this.remoteLat,
    long = this.remoteLong
)