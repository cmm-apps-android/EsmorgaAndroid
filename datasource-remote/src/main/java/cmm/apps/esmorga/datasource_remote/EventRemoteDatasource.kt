package cmm.apps.esmorga.datasource_remote

import cmm.apps.esmorga.data.EventDataModel
import cmm.apps.esmorga.data.datasource.EventDatasource
import java.util.Date


class EventRemoteDatasourceImpl: EventDatasource {
    override fun getEvents(): List<EventDataModel> {
        val list = listOf(
            EventDataModel("Event001", Date(System.currentTimeMillis() + 24 * 3_600_000)),
            EventDataModel("Event002", Date(System.currentTimeMillis() + 48 * 3_600_000)),
            EventDataModel("Event003", Date(System.currentTimeMillis() + 72 * 3_600_000)),
            EventDataModel("Event004", Date(System.currentTimeMillis() + 96 * 3_600_000)),
            EventDataModel("Event005", Date(System.currentTimeMillis() + 120 * 3_600_000)),
            EventDataModel("Event006", Date(System.currentTimeMillis() + 144 * 3_600_000)),
        )

        return list
    }
}