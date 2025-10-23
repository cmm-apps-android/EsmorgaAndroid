package cmm.apps.esmorga.datasource_remote.mock

import cmm.apps.esmorga.datasource_remote.event.model.EventAttendeeWrapperRemoteModel


object EventAttendeesRemoteMock {

    fun provideEventAttendeeListWrapper(nameList: List<String>): EventAttendeeWrapperRemoteModel {
        val list = provideEventAttendeeList(nameList)
        return EventAttendeeWrapperRemoteModel(list.size, list)
    }

    fun provideEventAttendeeList(nameList: List<String>): List<String> = nameList

}