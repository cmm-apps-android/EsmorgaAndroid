package cmm.apps.esmorga.datasource_remote.event

import cmm.apps.esmorga.data.event.datasource.EventDatasource
import cmm.apps.esmorga.data.event.model.EventAttendeeDataModel
import cmm.apps.esmorga.data.event.model.EventDataModel
import cmm.apps.esmorga.datasource_remote.api.EsmorgaEventAuthenticatedApi
import cmm.apps.esmorga.datasource_remote.api.EsmorgaEventOpenApi
import cmm.apps.esmorga.datasource_remote.api.ExceptionHandler.manageApiException
import cmm.apps.esmorga.datasource_remote.dateformatting.EsmorgaRemoteDateFormatter
import cmm.apps.esmorga.datasource_remote.event.mapper.toEventAttendeeDataModelList
import cmm.apps.esmorga.datasource_remote.event.mapper.toEventDataModelList


class EventRemoteDatasourceImpl(private val eventApi: EsmorgaEventAuthenticatedApi, private val publicEventApi: EsmorgaEventOpenApi, private val dateFormatter: EsmorgaRemoteDateFormatter) : EventDatasource {

    override suspend fun getEvents(): List<EventDataModel> {
        try {
            val eventList = publicEventApi.getEvents()
            return eventList.remoteEventList.toEventDataModelList(dateFormatter)
        } catch (e: Exception) {
            throw manageApiException(e)
        }
    }

    override suspend fun getEventAttendees(eventId: String): List<EventAttendeeDataModel> {
        try {
            val eventAttendeeList = eventApi.getEventAttendees(eventId)
            return eventAttendeeList.remoteEventAttendeeList.toEventAttendeeDataModelList(eventId)
        } catch (e: Exception) {
            throw manageApiException(e)
        }
    }

    override suspend fun getMyEvents(): List<EventDataModel> {
        try {
            val myEventList = eventApi.getMyEvents()
            return myEventList.remoteEventList.toEventDataModelList(dateFormatter)
        } catch (e: Exception) {
            throw manageApiException(e)
        }
    }

    override suspend fun joinEvent(event: EventDataModel) {
        try {
            val eventBody = mapOf("eventId" to event.dataId)
            eventApi.joinEvent(eventBody)
        } catch (e: Exception) {
            throw manageApiException(e)
        }
    }

    override suspend fun leaveEvent(event: EventDataModel) {
        try {
            val eventBody = mapOf("eventId" to event.dataId)
            eventApi.leaveEvent(eventBody)
        } catch (e: Exception) {
            throw manageApiException(e)
        }
    }
}