package cmm.apps.esmorga.view.viewmodel.mock

import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.domain.event.model.EventLocation
import cmm.apps.esmorga.domain.event.model.EventType
import cmm.apps.esmorga.view.eventlist.model.EventListUiModel
import cmm.apps.esmorga.view.eventlist.model.EventUILocation
import java.time.ZonedDateTime


object EventViewMock {

    fun provideEventList(nameList: List<String>): List<Event> = nameList.map { name -> provideEvent(name) }

    fun provideEvent(name: String, userJoined: Boolean = false): Event = Event(
        id = "$name-${System.currentTimeMillis()}",
        name = name,
        date = ZonedDateTime.now(),
        description = "description",
        type = EventType.SPORT,
        location = EventLocation("Location"),
        userJoined = userJoined
    )

    fun provideEventUiList(nameList: List<String>): List<EventListUiModel> = nameList.map { name -> provideEventUiModel(name) }

    fun provideEventUiModel(name: String, userJoined: Boolean = false): EventListUiModel = EventListUiModel(
        id = "$name-${System.currentTimeMillis()}",
        name = name,
        date = "2025-03-08T10:05:30.915Z",
        dateFormatted = "11 de Marzo de 2024",
        description = "description",
        type = EventType.SPORT,
        location = EventUILocation("Location"),
        userJoined = userJoined
    )

}