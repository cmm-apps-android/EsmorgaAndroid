package cmm.apps.esmorga.data.mock

import cmm.apps.esmorga.data.event.model.EventAttendeeDataModel


object EventAttendeeDataMock {

    fun provideEventAttendeeDataModelList(nameList: List<String>): List<EventAttendeeDataModel> = nameList.map { name -> provideEventAttendeeDataModel(name) }

    fun provideEventAttendeeDataModel(name: String, alreadyPaid: Boolean = false): EventAttendeeDataModel {
        val eventAttendeeDataModel = EventAttendeeDataModel(
            dataEventId = "Event",
            dataName = name,
            dataAlreadyPaid = alreadyPaid
        )
        return eventAttendeeDataModel
    }

}