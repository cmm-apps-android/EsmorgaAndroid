package cmm.apps.esmorga.data.mock

import cmm.apps.esmorga.data.event.model.EventDataModel
import cmm.apps.esmorga.data.event.model.EventLocationDataModel
import cmm.apps.esmorga.domain.event.model.EventType


object EventDataMock {

    fun provideEventDataModelList(nameList: List<String>): List<EventDataModel> = nameList.map { name -> provideEventDataModel(name) }

    fun provideEventDataModel(name: String, userJoined: Boolean = false): EventDataModel = EventDataModel(
        dataId = "$name-${System.currentTimeMillis()}",
        dataName = name,
        dataDate = System.currentTimeMillis(),
        dataDescription = "description",
        dataType = EventType.SPORT,
        dataLocation = EventLocationDataModel("Location"),
        dataUserJoined = userJoined,
        dataCurrentAttendeeCount = 0,
        dataMaxCapacity = 10
    )

}