package cmm.apps.esmorga.datasource_remote.di

import cmm.apps.esmorga.data.di.DataDIModule
import cmm.apps.esmorga.data.event.datasource.EventDatasource
import cmm.apps.esmorga.data.user.datasource.UserDatasource
import cmm.apps.esmorga.datasource_remote.api.EsmorgaApi
import cmm.apps.esmorga.datasource_remote.api.NetworkApiHelper
import cmm.apps.esmorga.datasource_remote.event.EventRemoteDatasourceImpl
import cmm.apps.esmorga.datasource_remote.user.UserRemoteDatasourceImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

object RemoteDIModule {

    val module = module {
        factory<EventDatasource>(named(DataDIModule.REMOTE_DATASOURCE_INSTANCE_NAME)) { EventRemoteDatasourceImpl(get()) }
        factory<UserDatasource>(named(DataDIModule.REMOTE_DATASOURCE_INSTANCE_NAME)) { UserRemoteDatasourceImpl(get()) }
        single {
            NetworkApiHelper().provideApi(
                baseUrl = EsmorgaApi.baseUrl(),
                clazz = EsmorgaApi::class.java
            )
        }
    }

}