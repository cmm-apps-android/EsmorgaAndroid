package cmm.apps.esmorga.datasource_local.mock

import cmm.apps.esmorga.datasource_local.event.model.EventLocalModel
import cmm.apps.esmorga.domain.event.model.EventType
import java.time.ZonedDateTime


object EventLocalMock {

    fun provideEventList(nameList: List<String>): List<EventLocalModel> = nameList.map { name -> provideEvent(name) }

    fun provideEvent(name: String): EventLocalModel = EventLocalModel(
        localId = "$name-${System.currentTimeMillis()}",
        localName = name,
        localDate = ZonedDateTime.now(),
        localDescription = "Description",
        localType = EventType.SPORT.name,
        localLocationName = "Location",
        localCreationTime = System.currentTimeMillis(),
        localUserJoined = false
    )

}