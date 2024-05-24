package cmm.apps.esmorga.view.mock

import cmm.apps.esmorga.domain.event.model.Event
import java.time.ZonedDateTime


object EventViewMock {

    fun provideEventList(nameList: List<String>): List<Event> = nameList.map { name -> provideEvent(name) }

    fun provideEvent(name: String): Event = Event(
        name = name,
        date = ZonedDateTime.now()
    )

}