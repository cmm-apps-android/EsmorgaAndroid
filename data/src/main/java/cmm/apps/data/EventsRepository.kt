package cmm.apps.data

import java.util.Date

interface EventsRepository {
    fun getEvents(): List<EventDataModel>
}

class EventsRepositoryImpl: EventsRepository{

    override fun getEvents(): List<EventDataModel> {
        val list = listOf(
            EventDataModel("Event01", Date(System.currentTimeMillis() + 24 * 3_600_000)),
            EventDataModel("Event02", Date(System.currentTimeMillis() + 48 * 3_600_000)),
            EventDataModel("Event03", Date(System.currentTimeMillis() + 72 * 3_600_000)),
            EventDataModel("Event04", Date(System.currentTimeMillis() + 96 * 3_600_000)),
            EventDataModel("Event05", Date(System.currentTimeMillis() + 120 * 3_600_000)),
            EventDataModel("Event06", Date(System.currentTimeMillis() + 144 * 3_600_000)),
        )

        return list //TODO change type to return Event
    }

}