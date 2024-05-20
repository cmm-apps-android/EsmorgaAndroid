package cmm.apps.esmorga.datasource_remote.di

import cmm.apps.esmorga.data.datasource.EventDatasource
import cmm.apps.esmorga.datasource_remote.EventRemoteDatasourceImpl
import cmm.apps.esmorga.datasource_remote.api.EventApi
import cmm.apps.esmorga.datasource_remote.api.NetworkApiHelper
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module


object RemoteDIModule {

    val module = module {
        factory<EventDatasource> { EventRemoteDatasourceImpl(get()) }

        single {
            NetworkApiHelper(androidApplication()).provideApi(
                baseUrl = EventApi.baseUrl(),
                clazz = EventApi::class.java
            )
        }
    }

}