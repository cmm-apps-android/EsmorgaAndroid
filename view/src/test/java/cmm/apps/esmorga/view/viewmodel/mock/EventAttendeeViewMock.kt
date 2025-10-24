package cmm.apps.esmorga.view.viewmodel.mock

import cmm.apps.esmorga.domain.event.model.EventAttendee


object EventAttendeeViewMock {

    fun provideEventAttendeeList(nameList: List<String>): List<EventAttendee> = nameList.map { name -> provideAttendee(name) }

    fun provideAttendee(name: String): EventAttendee = EventAttendee(
        eventId = "event",
        name = name,
        alreadyPaid = false
    )

}