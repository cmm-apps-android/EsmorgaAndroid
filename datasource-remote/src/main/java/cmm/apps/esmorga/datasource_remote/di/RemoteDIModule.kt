package cmm.apps.esmorga.datasource_remote.di

import cmm.apps.esmorga.data.event.datasource.EventDatasource
import cmm.apps.esmorga.data.di.DataDIModule
import cmm.apps.esmorga.datasource_remote.event.EventRemoteDatasourceImpl
import cmm.apps.esmorga.datasource_remote.api.EventApi
import cmm.apps.esmorga.datasource_remote.api.NetworkApiHelper
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module


object RemoteDIModule {

    val module = module {
        factory<EventDatasource>(named(DataDIModule.EVENT_REMOTE_DATASOURCE_INSTANCE_NAME)) { EventRemoteDatasourceImpl(get()) }

        single {
            NetworkApiHelper(androidApplication()).provideApi(
                baseUrl = EventApi.baseUrl(),
                clazz = EventApi::class.java
            )
        }
    }

}