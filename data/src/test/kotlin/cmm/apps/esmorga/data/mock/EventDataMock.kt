package cmm.apps.esmorga.data.mock

import cmm.apps.esmorga.data.event.model.EventDataModel
import cmm.apps.esmorga.data.event.model.EventLocationDataModel
import cmm.apps.esmorga.domain.event.model.EventType
import java.time.ZonedDateTime


object EventDataMock {

    fun provideEventDataModelList(nameList: List<String>): List<EventDataModel> = nameList.map { name -> provideEventDataModel(name) }

    fun provideEventDataModel(name: String, userJoined: Boolean = false): EventDataModel = EventDataModel(
        dataId = "$name-${System.currentTimeMillis()}",
        dataName = name,
        dataDate = ZonedDateTime.now(),
        dataDescription = "description",
        dataType = EventType.SPORT,
        dataLocation = EventLocationDataModel("Location"),
        dataUserJoined = userJoined
    )

}