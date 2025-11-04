package cmm.apps.esmorga.component.mock

import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.domain.event.model.EventLocation
import cmm.apps.esmorga.domain.event.model.EventType
import java.time.ZonedDateTime


object EventMock {

    fun provideEventModel(name: String, userJoined: Boolean = false): Event = Event(
        id = "$name-${System.currentTimeMillis()}",
        name = name,
        date = System.currentTimeMillis(),
        description = "description",
        type = EventType.SPORT,
        location = EventLocation("Location"),
        userJoined = userJoined,
        currentAttendeeCount = 0,
        maxCapacity = 10,
        joinDeadline = ZonedDateTime.now().plusDays(7).toInstant().toEpochMilli()
    )

}