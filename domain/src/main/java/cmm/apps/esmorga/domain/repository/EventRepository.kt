package cmm.apps.esmorga.domain.repository

import cmm.apps.esmorga.domain.event.model.Event


interface EventRepository {
    suspend fun getEvents(): List<Event>
}