package cmm.apps.esmorga.component.mock

import cmm.apps.esmorga.data.event.model.EventDataModel
import cmm.apps.esmorga.data.event.model.EventLocationDataModel
import cmm.apps.esmorga.domain.event.model.EventType
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


object EventDataMock {

    fun provideEventDataModelList(nameList: List<String>): List<EventDataModel> = nameList.map { name -> provideEventDataModel(name) }

    fun provideEventDataModel(name: String): EventDataModel = EventDataModel(
        dataId = "$name-${System.currentTimeMillis()}",
        dataName = name,
        dataDate = System.currentTimeMillis(),
        dataDescription = "description",
        dataType = EventType.SPORT,
        dataLocation = EventLocationDataModel("Location"),
        dataUserJoined = false,
        dataCurrentAttendeeCount = 0,
        dataMaxCapacity = 10,
        joinDeadline = ZonedDateTime.now().plusDays(7).format(DateTimeFormatter.ISO_INSTANT)
    )

}