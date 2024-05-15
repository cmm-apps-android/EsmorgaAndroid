package cmm.apps.esmorga.data.datasource

import cmm.apps.esmorga.data.EventDataModel


interface EventDatasource {
    suspend fun getEvents(): List<EventDataModel>
}