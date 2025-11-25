package cmm.apps.esmorga.datasource_remote.di

import cmm.apps.esmorga.data.di.DataDIModule
import cmm.apps.esmorga.data.event.datasource.EventDatasource
import cmm.apps.esmorga.data.poll.datasource.PollDatasource
import cmm.apps.esmorga.data.user.datasource.AuthDatasource
import cmm.apps.esmorga.data.user.datasource.UserDatasource
import cmm.apps.esmorga.datasource_remote.api.ConnectionInterceptor
import cmm.apps.esmorga.datasource_remote.api.EsmorgaAccountAuthenticatedApi
import cmm.apps.esmorga.datasource_remote.api.EsmorgaEventAuthenticatedApi
import cmm.apps.esmorga.datasource_remote.api.EsmorgaAccountOpenApi
import cmm.apps.esmorga.datasource_remote.api.EsmorgaPollAuthenticatedApi
import cmm.apps.esmorga.datasource_remote.api.EsmorgaEventOpenApi
import cmm.apps.esmorga.datasource_remote.api.NetworkApiHelper
import cmm.apps.esmorga.datasource_remote.api.authenticator.EsmorgaAuthInterceptor
import cmm.apps.esmorga.datasource_remote.api.authenticator.EsmorgaAuthenticator
import cmm.apps.esmorga.datasource_remote.api.device.DeviceInterceptor
import cmm.apps.esmorga.datasource_remote.dateformatting.EsmorgaRemoteDateFormatter
import cmm.apps.esmorga.datasource_remote.dateformatting.RemoteDateFormatterImpl
import cmm.apps.esmorga.datasource_remote.event.EventRemoteDatasourceImpl
import cmm.apps.esmorga.datasource_remote.poll.PollRemoteDatasourceImpl
import cmm.apps.esmorga.datasource_remote.user.AuthRemoteDatasourceImpl
import cmm.apps.esmorga.datasource_remote.user.UserRemoteDatasourceImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

object RemoteDIModule {

    val module = module {
        factory<AuthDatasource> { AuthRemoteDatasourceImpl(get(), get()) }
        single<EsmorgaAccountOpenApi> {
            NetworkApiHelper().provideApi(
                baseUrl = NetworkApiHelper.esmorgaApiBaseUrl(),
                clazz = EsmorgaAccountOpenApi::class.java,
                authenticator = null,
                authInterceptor = null,
                deviceInterceptor = DeviceInterceptor(get(named(DataDIModule.LOCAL_DATASOURCE_INSTANCE_NAME))),
                connectionInterceptor = ConnectionInterceptor
            )
        }
        single<EsmorgaAccountAuthenticatedApi> {
            NetworkApiHelper().provideApi(
                baseUrl = NetworkApiHelper.esmorgaApiBaseUrl(),
                clazz = EsmorgaAccountAuthenticatedApi::class.java,
                authenticator = EsmorgaAuthenticator(get()),
                authInterceptor = EsmorgaAuthInterceptor(get()),
                deviceInterceptor = DeviceInterceptor(get(named(DataDIModule.LOCAL_DATASOURCE_INSTANCE_NAME))),
                connectionInterceptor = ConnectionInterceptor
            )
        }
        single<EsmorgaEventAuthenticatedApi> {
            NetworkApiHelper().provideApi(
                baseUrl = NetworkApiHelper.esmorgaApiBaseUrl(),
                clazz = EsmorgaEventAuthenticatedApi::class.java,
                authenticator = EsmorgaAuthenticator(get()),
                authInterceptor = EsmorgaAuthInterceptor(get()),
                deviceInterceptor = DeviceInterceptor(get(named(DataDIModule.LOCAL_DATASOURCE_INSTANCE_NAME))),
                connectionInterceptor = ConnectionInterceptor
            )
        }
        single<EsmorgaEventOpenApi> {
            NetworkApiHelper().provideApi(
                baseUrl = NetworkApiHelper.esmorgaApiBaseUrl(),
                clazz = EsmorgaEventOpenApi::class.java,
                authenticator = null,
                authInterceptor = null,
                deviceInterceptor = DeviceInterceptor(get(named(DataDIModule.LOCAL_DATASOURCE_INSTANCE_NAME))),
                connectionInterceptor = ConnectionInterceptor
            )
        }
        single<EsmorgaPollAuthenticatedApi> {
            NetworkApiHelper().provideApi(
                baseUrl = NetworkApiHelper.esmorgaApiBaseUrl(),
                clazz = EsmorgaPollAuthenticatedApi::class.java,
                authenticator = EsmorgaAuthenticator(get()),
                authInterceptor = EsmorgaAuthInterceptor(get()),
                deviceInterceptor = DeviceInterceptor(get(named(DataDIModule.LOCAL_DATASOURCE_INSTANCE_NAME))),
                connectionInterceptor = ConnectionInterceptor
            )
        }
        factory<EventDatasource>(named(DataDIModule.REMOTE_DATASOURCE_INSTANCE_NAME)) { EventRemoteDatasourceImpl(get(), get(), get()) }
        factory<UserDatasource>(named(DataDIModule.REMOTE_DATASOURCE_INSTANCE_NAME)) { UserRemoteDatasourceImpl(get(), get(), get()) }
        factory<PollDatasource>(named(DataDIModule.REMOTE_DATASOURCE_INSTANCE_NAME)) { PollRemoteDatasourceImpl(get(), get()) }

        single<EsmorgaRemoteDateFormatter> { RemoteDateFormatterImpl() }
    }
}