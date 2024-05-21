package cmm.apps.esmorga.datasource_local.event.mapper

import cmm.apps.esmorga.data.event.model.EventDataModel
import cmm.apps.esmorga.datasource_local.event.model.EventLocalModel


fun EventLocalModel.toEventDataModel(): EventDataModel {
    return EventDataModel(dataName = this.localName, dataDate = localDate, creationTime = System.currentTimeMillis())
}

fun List<EventLocalModel>.toEventDataModelList(): List<EventDataModel> = this.map { erm -> erm.toEventDataModel() }