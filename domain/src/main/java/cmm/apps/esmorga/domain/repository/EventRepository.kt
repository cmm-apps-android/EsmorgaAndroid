package cmm.apps.esmorga.domain.repository

import cmm.apps.esmorga.domain.Event


interface EventRepository {
    fun getEvents(): List<Event>
}