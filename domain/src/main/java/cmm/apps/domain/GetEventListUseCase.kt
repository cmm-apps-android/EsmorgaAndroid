package cmm.apps.domain

import cmm.apps.data.EventDataModel
import cmm.apps.data.EventsRepositoryImpl
import kotlinx.coroutines.delay
import java.util.Date

interface GetEventListUseCase {
    suspend operator fun invoke(): List<Event>
}

class GetEventListUseCaseImpl : GetEventListUseCase {
    override suspend fun invoke(): List<Event> {

        delay(5000)

        val result = EventsRepositoryImpl().getEvents()

        return result.map { edm -> Event(name = edm.dataName, date = edm.dataDate)}
    }
}