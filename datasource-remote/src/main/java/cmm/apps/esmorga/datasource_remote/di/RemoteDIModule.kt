package cmm.apps.esmorga.datasource_remote.di

import cmm.apps.esmorga.data.di.DataDIModule
import cmm.apps.esmorga.data.event.datasource.EventDatasource
import cmm.apps.esmorga.data.poll.datasource.PollDatasource
import cmm.apps.esmorga.data.user.datasource.AuthDatasource
import cmm.apps.esmorga.data.user.datasource.UserDatasource
import cmm.apps.esmorga.datasource_remote.api.ConnectionInterceptor
import cmm.apps.esmorga.datasource_remote.api.EsmorgaEventApi
import cmm.apps.esmorga.datasource_remote.api.EsmorgaAccountApi
import cmm.apps.esmorga.datasource_remote.api.EsmorgaPollApi
import cmm.apps.esmorga.datasource_remote.api.EsmorgaPublicEventApi
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
        single<EsmorgaAccountApi> {
            NetworkApiHelper().provideApi(
                baseUrl = NetworkApiHelper.esmorgaApiBaseUrl(),
                clazz = EsmorgaAccountApi::class.java,
                authenticator = null,
                authInterceptor = null,
                deviceInterceptor = DeviceInterceptor(get(named(DataDIModule.LOCAL_DATASOURCE_INSTANCE_NAME))),
                connectionInterceptor = ConnectionInterceptor
            )
        }
        single<EsmorgaEventApi> {
            NetworkApiHelper().provideApi(
                baseUrl = NetworkApiHelper.esmorgaApiBaseUrl(),
                clazz = EsmorgaEventApi::class.java,
                authenticator = EsmorgaAuthenticator(get()),
                authInterceptor = EsmorgaAuthInterceptor(get()),
                deviceInterceptor = DeviceInterceptor(get(named(DataDIModule.LOCAL_DATASOURCE_INSTANCE_NAME))),
                connectionInterceptor = ConnectionInterceptor
            )
        }
        single<EsmorgaPublicEventApi> {
            NetworkApiHelper().provideApi(
                baseUrl = NetworkApiHelper.esmorgaApiBaseUrl(),
                clazz = EsmorgaPublicEventApi::class.java,
                authenticator = null,
                authInterceptor = null,
                deviceInterceptor = DeviceInterceptor(get(named(DataDIModule.LOCAL_DATASOURCE_INSTANCE_NAME))),
                connectionInterceptor = ConnectionInterceptor
            )
        }
        single<EsmorgaPollApi> {
            NetworkApiHelper().provideApi(
                baseUrl = NetworkApiHelper.esmorgaApiBaseUrl(),
                clazz = EsmorgaPollApi::class.java,
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