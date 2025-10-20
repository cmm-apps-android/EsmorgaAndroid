package cmm.apps.esmorga.datasource_remote.event.mapper

import cmm.apps.esmorga.data.event.model.EventAttendeeDataModel
import cmm.apps.esmorga.data.event.model.EventDataModel
import cmm.apps.esmorga.data.event.model.EventLocationDataModel
import cmm.apps.esmorga.datasource_remote.event.model.EventLocationRemoteModel
import cmm.apps.esmorga.datasource_remote.event.model.EventRemoteModel
import cmm.apps.esmorga.domain.event.model.EventAttendee
import cmm.apps.esmorga.domain.event.model.EventType
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.Source
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException


fun String.toEventAttendeeDataModel(): EventAttendeeDataModel {

    return EventAttendeeDataModel(
        dataName = this
    )
}

fun List<String>.toEventAttendeeDataModelList(): List<EventAttendeeDataModel> = this.map { earm -> earm.toEventAttendeeDataModel() }
