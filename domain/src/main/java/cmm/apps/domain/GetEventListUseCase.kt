package cmm.apps.domain

import kotlinx.coroutines.delay
import java.util.Date

interface GetEventListUseCase {
    suspend operator fun invoke(): List<Event>
}

class GetEventListUseCaseImpl : GetEventListUseCase {
    override suspend fun invoke(): List<Event> {

        delay(5000)

        return listOf(
            Event("Event01", Date(System.currentTimeMillis() + 24 * 3_600_000)),
            Event("Event02", Date(System.currentTimeMillis() + 48 * 3_600_000)),
            Event("Event03", Date(System.currentTimeMillis() + 72 * 3_600_000)),
            Event("Event04", Date(System.currentTimeMillis() + 96 * 3_600_000)),
            Event("Event05", Date(System.currentTimeMillis() + 120 * 3_600_000)),
            Event("Event06", Date(System.currentTimeMillis() + 144 * 3_600_000)),
        )
    }
}