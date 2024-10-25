package cmm.apps.esmorga.view.viewmodel.mock

import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.domain.event.model.EventLocation
import cmm.apps.esmorga.domain.event.model.EventType
import java.time.ZonedDateTime


object EventViewMock {

    fun provideEventList(nameList: List<String>): List<Event> = nameList.map { name -> provideEvent(name) }

    fun provideEvent(name: String, userJoined: Boolean = false): Event = Event(
        id = "$name-${System.currentTimeMillis()}",
        name = name,
        date = ZonedDateTime.parse("2022-03-14T09:33:52Z"),
        description = "description",
        type = EventType.SPORT,
        location = EventLocation("Location"),
        userJoined = userJoined
    )

}