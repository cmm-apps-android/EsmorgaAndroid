package cmm.apps.esmorga.datasource_local.event.mapper

import cmm.apps.esmorga.data.event.model.EventAttendeeDataModel
import cmm.apps.esmorga.datasource_local.event.model.EventAttendeeLocalModel


fun EventAttendeeLocalModel.toEventAttendeeDataModel(): EventAttendeeDataModel {
    return EventAttendeeDataModel(
        dataName = this.localName,
        dataCreationTime = localCreationTime,
    )
}

fun List<EventAttendeeLocalModel>.toEventAttendeeDataModelList(): List<EventAttendeeDataModel> = this.map { ealm -> ealm.toEventAttendeeDataModel() }
