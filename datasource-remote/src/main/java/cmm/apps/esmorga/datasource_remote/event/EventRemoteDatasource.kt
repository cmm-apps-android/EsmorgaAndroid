package cmm.apps.esmorga.datasource_remote.event

import cmm.apps.esmorga.data.event.datasource.EventDatasource
import cmm.apps.esmorga.data.event.model.EventDataModel
import cmm.apps.esmorga.datasource_remote.api.EsmorgaApi
import cmm.apps.esmorga.datasource_remote.api.EsmorgaGuestApi
import cmm.apps.esmorga.datasource_remote.api.ExceptionHandler.manageApiException
import cmm.apps.esmorga.datasource_remote.event.mapper.toEventDataModelList


class EventRemoteDatasourceImpl(private val eventApi: EsmorgaApi, private val guestApi: EsmorgaGuestApi) : EventDatasource {

    override suspend fun getEvents(): List<EventDataModel> {
        try {
            val eventList = guestApi.getEvents()
            return eventList.remoteEventList.toEventDataModelList()
        } catch (e: Exception) {
            throw manageApiException(e)
        }
    }

    override suspend fun getMyEvents(): List<EventDataModel> {
        try {
            val myEventList = eventApi.getMyEvents()
            return myEventList.remoteEventList.toEventDataModelList()
        } catch (e: Exception) {
            throw manageApiException(e)
        }
    }


}