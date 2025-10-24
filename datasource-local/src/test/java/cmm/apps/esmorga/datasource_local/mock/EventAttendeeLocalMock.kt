package cmm.apps.esmorga.datasource_local.mock

import cmm.apps.esmorga.datasource_local.event.model.EventAttendeeLocalModel
import cmm.apps.esmorga.datasource_local.event.model.EventLocalModel
import cmm.apps.esmorga.domain.event.model.EventType
import java.time.ZonedDateTime


object EventAttendeeLocalMock {

    fun provideAttendeeList(nameList: List<String>): List<EventAttendeeLocalModel> = nameList.map { name -> provideAttendee(name = name) }

    fun provideAttendee(eventId: String = "event", name: String = "name", alreadyPaid: Boolean = false): EventAttendeeLocalModel = EventAttendeeLocalModel(
        localEventId = eventId,
        localName = name,
        localAlreadyPaid = alreadyPaid
    )

}