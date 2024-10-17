package cmm.apps.esmorga.domain.event.repository

import cmm.apps.esmorga.domain.event.model.Event


interface EventRepository {
    suspend fun getEvents(forceRefresh: Boolean = false, forceLocal: Boolean = false): List<Event>
    suspend fun getEventDetails(eventId: String): Event
    suspend fun joinEvent(event: Event)
    suspend fun leaveEvent(event: Event)
}