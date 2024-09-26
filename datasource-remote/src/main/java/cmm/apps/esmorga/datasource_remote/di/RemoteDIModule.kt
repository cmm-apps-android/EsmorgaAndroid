package cmm.apps.esmorga.datasource_remote.di

import cmm.apps.esmorga.data.di.DataDIModule
import cmm.apps.esmorga.data.event.datasource.EventDatasource
import cmm.apps.esmorga.data.user.datasource.AuthDatasource
import cmm.apps.esmorga.data.user.datasource.UserDatasource
import cmm.apps.esmorga.datasource_remote.api.EsmorgaApi
import cmm.apps.esmorga.datasource_remote.api.EsmorgaAuthApi
import cmm.apps.esmorga.datasource_remote.api.EsmorgaGuestApi
import cmm.apps.esmorga.datasource_remote.api.NetworkApiHelper
import cmm.apps.esmorga.datasource_remote.api.authenticator.EsmorgaAuthInterceptor
import cmm.apps.esmorga.datasource_remote.api.authenticator.EsmorgaAuthenticator
import cmm.apps.esmorga.datasource_remote.event.EventRemoteDatasourceImpl
import cmm.apps.esmorga.datasource_remote.user.AuthRemoteDatasourceImpl
import cmm.apps.esmorga.datasource_remote.user.UserRemoteDatasourceImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

object RemoteDIModule {

    val module = module {
        single {
            NetworkApiHelper().provideApi(
                baseUrl = NetworkApiHelper.esmorgaApiBaseUrl(),
                clazz = EsmorgaAuthApi::class.java,
                authenticator = null,
                authInterceptor = null
            )
        }
        factory<AuthDatasource> { AuthRemoteDatasourceImpl(get(), get()) }
        single {
            NetworkApiHelper().provideApi(
                baseUrl = NetworkApiHelper.esmorgaApiBaseUrl(),
                clazz = EsmorgaApi::class.java,
                authenticator = EsmorgaAuthenticator(get(), get()),
                authInterceptor = EsmorgaAuthInterceptor(get(), get())
            )
        }
        single {
            NetworkApiHelper().provideApi(
                baseUrl = NetworkApiHelper.esmorgaApiBaseUrl(),
                clazz = EsmorgaGuestApi::class.java,
                authenticator = null,
                authInterceptor = null
            )
        }
        factory<EventDatasource>(named(DataDIModule.REMOTE_DATASOURCE_INSTANCE_NAME)) { EventRemoteDatasourceImpl(get(), get()) }
        factory<UserDatasource>(named(DataDIModule.REMOTE_DATASOURCE_INSTANCE_NAME)) { UserRemoteDatasourceImpl(get()) }
    }
}