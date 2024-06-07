package cmm.apps.esmorga.datasource_local.event.mapper

import cmm.apps.esmorga.data.event.model.EventDataModel
import cmm.apps.esmorga.data.event.model.EventLocationDataModel
import cmm.apps.esmorga.datasource_local.event.model.EventLocalModel
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.Source
import cmm.apps.esmorga.domain.event.model.EventType


fun EventLocalModel.toEventDataModel(): EventDataModel {
    val parsedType = try {
        EventType.valueOf(this.localType)
    } catch (e: Exception) {
        throw EsmorgaException(message = "Error parsing type [${this.localType.uppercase()}] in EventRemoteModel", source = Source.LOCAL, code = ErrorCodes.PARSE_ERROR)
    }

    return EventDataModel(
        dataId = this.localId,
        dataName = this.localName,
        dataDate = this.localDate,
        dataDescription = this.localDescription,
        dataType = parsedType,
        dataImageUrl = this.localImageUrl,
        dataLocation = EventLocationDataModel(this.localLocationName, this.localLocationLat, this.localLocationLong),
        dataTags = this.localTags,
        dataCreationTime = localCreationTime
    )
}

fun List<EventLocalModel>.toEventDataModelList(): List<EventDataModel> = this.map { erm -> erm.toEventDataModel() }

fun EventDataModel.toEventLocalModel(): EventLocalModel {
    return EventLocalModel(
        localId = this.dataId,
        localName = this.dataName,
        localDate = this.dataDate,
        localDescription = this.dataDescription,
        localType = this.dataType.name,
        localImageUrl = this.dataImageUrl,
        localLocationName = this.dataLocation.name,
        localLocationLat = this.dataLocation.lat,
        localLocationLong = this.dataLocation.long,
        localTags = this.dataTags,
        localCreationTime = dataCreationTime
    )
}

fun List<EventDataModel>.toEventLocalModelList(): List<EventLocalModel> = this.map { elm -> elm.toEventLocalModel() }