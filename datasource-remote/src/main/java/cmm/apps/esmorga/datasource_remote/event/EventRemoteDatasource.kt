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
import cmm.apps.esmorga.domain.event.model.CreateEventForm
import cmm.apps.esmorga.domain.result.ErrorCodes
import cmm.apps.esmorga.domain.result.EsmorgaException
import cmm.apps.esmorga.domain.result.Source


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

    override suspend fun createEvent(eventForm: CreateEventForm) {
        try {
            eventApi.createEvent(eventForm.toCreateEventBody())
        } catch (e: Exception) {
            throw manageApiException(e)
        }
    }

    private fun CreateEventForm.toCreateEventBody(): Map<String, Any> {
        val eventName = this.name ?: throw EsmorgaException("Missing event name", Source.LOCAL, ErrorCodes.PARSE_ERROR)
        val eventDate = this.date ?: throw EsmorgaException("Missing event date", Source.LOCAL, ErrorCodes.PARSE_ERROR)
        val description = this.description ?: throw EsmorgaException("Missing event description", Source.LOCAL, ErrorCodes.PARSE_ERROR)
        val eventType = this.type ?: throw EsmorgaException("Missing event type", Source.LOCAL, ErrorCodes.PARSE_ERROR)
        val location = this.location ?: throw EsmorgaException("Missing event location", Source.LOCAL, ErrorCodes.PARSE_ERROR)

        val locationBody = mutableMapOf<String, Any>("name" to location.name)
        location.lat?.let { locationBody["lat"] = it }
        location.long?.let { locationBody["long"] = it }

        val body = mutableMapOf<String, Any>(
            "eventName" to eventName,
            "eventDate" to eventDate,
            "description" to description,
            "eventType" to eventType.name.lowercase().replaceFirstChar { it.titlecase() },
            "location" to locationBody
        )

        this.imageUrl?.takeIf { it.isNotBlank() }?.let { body["imageUrl"] = it }
        this.maxCapacity?.let { body["maxCapacity"] = it }

        return body
    }
}