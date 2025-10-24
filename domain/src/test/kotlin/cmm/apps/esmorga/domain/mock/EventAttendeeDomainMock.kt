package cmm.apps.esmorga.domain.mock

import cmm.apps.esmorga.domain.event.model.EventAttendee


object EventAttendeeDomainMock {

    fun provideEventAttendeeList(nameList: List<String>): List<EventAttendee> = nameList.map { name -> provideAttendee(name) }

    fun provideAttendee(name: String): EventAttendee = EventAttendee(
        eventId = "eventId",
        name = name,
        alreadyPaid = true
    )

}