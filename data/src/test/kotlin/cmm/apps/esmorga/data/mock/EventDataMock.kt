package cmm.apps.esmorga.data.mock

import cmm.apps.esmorga.data.event.model.EventDataModel
import cmm.apps.esmorga.data.event.model.EventLocationDataModel
import cmm.apps.esmorga.domain.event.model.EventType
import java.time.ZonedDateTime


object EventDataMock {

    fun provideEventDataModelList(nameList: List<String>): List<EventDataModel> = nameList.map { name -> provideEventDataModel(name) }

    fun provideEventDataModel(
        name: String, userJoined: Boolean = false, joinDeadline: Long = ZonedDateTime.now().plusDays(7).toInstant().toEpochMilli()
    ): EventDataModel = EventDataModel(
        dataId = "$name-${System.currentTimeMillis()}",
        dataName = name,
        dataDate = System.currentTimeMillis(),
        dataDescription = "description",
        dataType = EventType.SPORT,
        dataLocation = EventLocationDataModel("Location"),
        dataUserJoined = userJoined,
        dataCurrentAttendeeCount = 0,
        dataMaxCapacity = 10,
        dataJoinDeadline = joinDeadline
    )

}