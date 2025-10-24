package cmm.apps.esmorga.datasource_remote.event.mapper

import cmm.apps.esmorga.data.event.model.EventAttendeeDataModel


fun String.toEventAttendeeDataModel(): EventAttendeeDataModel {

    return EventAttendeeDataModel(
        dataName = this
    )
}

fun List<String>.toEventAttendeeDataModelList(): List<EventAttendeeDataModel> = this.map { earm -> earm.toEventAttendeeDataModel() }
