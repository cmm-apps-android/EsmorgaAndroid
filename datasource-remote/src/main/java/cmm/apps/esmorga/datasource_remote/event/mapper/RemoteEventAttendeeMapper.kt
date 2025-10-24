package cmm.apps.esmorga.datasource_remote.event.mapper

import cmm.apps.esmorga.data.event.model.EventAttendeeDataModel


fun String.toEventAttendeeDataModel(eventId: String): EventAttendeeDataModel {
    return EventAttendeeDataModel(
        dataEventId = eventId,
        dataName = this,
        dataAlreadyPaid = false
    )
}

fun List<String>.toEventAttendeeDataModelList(eventId: String): List<EventAttendeeDataModel> = this.map { earm -> earm.toEventAttendeeDataModel(eventId) }
