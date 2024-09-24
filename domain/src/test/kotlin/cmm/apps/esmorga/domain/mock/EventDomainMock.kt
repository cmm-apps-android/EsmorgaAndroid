package cmm.apps.esmorga.domain.mock

import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.domain.event.model.EventLocation
import cmm.apps.esmorga.domain.event.model.EventType
import java.time.ZonedDateTime


object EventDomainMock {

    fun provideEventList(nameList: List<String>): List<Event> = nameList.map { name -> provideEvent(name) }

    fun provideEvent(name: String): Event = Event(
        id = "$name-${System.currentTimeMillis()}",
        name = name,
        date = ZonedDateTime.now(),
        description = "description",
        type = EventType.SPORT,
        location = EventLocation("Location"),
        userJoined = false
    )

}