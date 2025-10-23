package cmm.apps.esmorga.data.mock

import cmm.apps.esmorga.data.event.model.EventAttendeeDataModel


object EventAttendeeDataMock {

    fun provideEventDataModelList(nameList: List<String>): List<EventAttendeeDataModel> = nameList.map { name -> provideEventAttendeeDataModel(name) }

    fun provideEventAttendeeDataModel(name: String): EventAttendeeDataModel {
        val eventAttendeeDataModel = EventAttendeeDataModel(
            dataName = name
        )
        return eventAttendeeDataModel
    }

}