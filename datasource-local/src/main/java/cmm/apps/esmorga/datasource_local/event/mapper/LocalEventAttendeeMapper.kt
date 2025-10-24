package cmm.apps.esmorga.datasource_local.event.mapper

import cmm.apps.esmorga.data.event.model.EventAttendeeDataModel
import cmm.apps.esmorga.datasource_local.event.model.EventAttendeeLocalModel


fun EventAttendeeLocalModel.toEventAttendeeDataModel() = EventAttendeeDataModel(
    dataEventId = this.localEventId,
    dataName = this.localName,
    dataAlreadyPaid = this.localAlreadyPaid
)

fun List<EventAttendeeLocalModel>.toEventAttendeeDataModelList(): List<EventAttendeeDataModel> = this.map { erm -> erm.toEventAttendeeDataModel() }

fun EventAttendeeDataModel.toEventAttendeeLocalModel() = EventAttendeeLocalModel(
    localEventId = this.dataEventId,
    localName = this.dataName,
    localAlreadyPaid = this.dataAlreadyPaid
)
