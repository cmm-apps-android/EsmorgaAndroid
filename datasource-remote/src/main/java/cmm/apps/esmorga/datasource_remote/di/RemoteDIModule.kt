package cmm.apps.esmorga.datasource_remote.di

import cmm.apps.esmorga.data.datasource.EventDatasource
import cmm.apps.esmorga.datasource_remote.EventRemoteDatasourceImpl
import org.koin.dsl.module


object RemoteDIModule {

    val module = module {
        factory<EventDatasource> { EventRemoteDatasourceImpl() }
    }

}