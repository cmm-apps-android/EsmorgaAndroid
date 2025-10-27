package cmm.apps.esmorga.data.event.mapper

import cmm.apps.esmorga.data.event.model.EventAttendeeDataModel
import cmm.apps.esmorga.domain.event.model.EventAttendee


fun EventAttendeeDataModel.toEventAttendee(): EventAttendee = EventAttendee(
    eventId = this.dataEventId,
    name = this.dataName,
    alreadyPaid = this.dataAlreadyPaid
)

fun List<EventAttendeeDataModel>.toEventAttendeeList(): List<EventAttendee> = map { adm -> adm.toEventAttendee() }

fun EventAttendee.toEventAttendeeDataModel(): EventAttendeeDataModel = EventAttendeeDataModel(
    dataEventId = this.eventId,
    dataName = this.name,
    dataAlreadyPaid = this.alreadyPaid
)

