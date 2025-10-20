package cmm.apps.esmorga.data.event.mapper

import cmm.apps.esmorga.data.event.model.EventAttendeeDataModel
import cmm.apps.esmorga.domain.event.model.EventAttendee


fun EventAttendeeDataModel.toEventAttendee(): EventAttendee = EventAttendee(
    name = this.dataName
)

fun List<EventAttendeeDataModel>.toEventAttendeeList(): List<EventAttendee> = map { adm -> adm.toEventAttendee() }