package cmm.apps.esmorga.domain.event.repository

import cmm.apps.esmorga.domain.event.model.Event


interface EventRepository {
    suspend fun getEvents(forceRefresh: Boolean = false): List<Event>
}