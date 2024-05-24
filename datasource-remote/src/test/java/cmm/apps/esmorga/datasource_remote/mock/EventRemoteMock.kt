package cmm.apps.esmorga.datasource_remote.mock

import cmm.apps.esmorga.datasource_remote.event.model.EventListWrapperRemoteModel
import cmm.apps.esmorga.datasource_remote.event.model.EventRemoteModel


object EventRemoteMock {

    fun provideEventListWrapper(nameList: List<String>): EventListWrapperRemoteModel {
        val list = provideEventList(nameList)
        return EventListWrapperRemoteModel(list.size, list)
    }

    fun provideEventList(nameList: List<String>): List<EventRemoteModel> = nameList.map { name -> provideEvent(name) }

    fun provideEvent(name: String): EventRemoteModel = EventRemoteModel(
        remoteName = name,
        remoteDate = "2030-12-31T23:59:59.000Z"
    )

}