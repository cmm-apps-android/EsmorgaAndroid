package cmm.apps.esmorga.domain.event.repository

import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.domain.result.Success


interface EventRepository {
    suspend fun getEvents(forceRefresh: Boolean = false): Success<List<Event>>
    suspend fun getEventDetails(eventId: String): Success<Event>
}