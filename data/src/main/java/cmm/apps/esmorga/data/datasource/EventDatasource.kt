package cmm.apps.esmorga.data.datasource

import cmm.apps.esmorga.data.EventDataModel


interface EventDatasource {
    fun getEvents(): List<EventDataModel>
}