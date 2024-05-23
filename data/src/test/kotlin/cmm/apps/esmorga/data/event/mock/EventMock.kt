package cmm.apps.esmorga.data.event.mock

import cmm.apps.esmorga.data.event.model.EventDataModel
import java.time.ZonedDateTime


object EventMock {

    fun provideEventDataModelList(nameList: List<String>): List<EventDataModel> = nameList.map { name -> provideEventDataModel(name) }

    fun provideEventDataModel(name: String): EventDataModel = EventDataModel(
        dataName = name,
        dataDate = ZonedDateTime.now()
    )

}