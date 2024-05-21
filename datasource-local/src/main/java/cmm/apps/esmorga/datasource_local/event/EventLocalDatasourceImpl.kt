package cmm.apps.esmorga.datasource_local.event

import cmm.apps.esmorga.data.datasource.EventDatasource
import cmm.apps.esmorga.data.event.model.EventDataModel
import cmm.apps.esmorga.datasource_local.database.EsmorgaDatabase
import cmm.apps.esmorga.datasource_local.event.mapper.toEventDataModelList


class EventLocalDatasourceImpl(private val database: EsmorgaDatabase):EventDatasource {

    override suspend fun getEvents(): List<EventDataModel> {
        return database.eventDao().getEvents().toEventDataModelList()
    }

}