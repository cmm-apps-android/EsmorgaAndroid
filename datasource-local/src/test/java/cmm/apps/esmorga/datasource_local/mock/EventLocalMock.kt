package cmm.apps.esmorga.datasource_local.mock

import cmm.apps.esmorga.datasource_local.event.model.EventLocalModel
import java.time.ZonedDateTime


object EventLocalMock {

    fun provideEventList(nameList: List<String>): List<EventLocalModel> = nameList.map { name -> provideEvent(name) }

    fun provideEvent(name: String): EventLocalModel = EventLocalModel(
        localName = name,
        localDate = ZonedDateTime.now(),
        localCreationTime = System.currentTimeMillis()
    )

}