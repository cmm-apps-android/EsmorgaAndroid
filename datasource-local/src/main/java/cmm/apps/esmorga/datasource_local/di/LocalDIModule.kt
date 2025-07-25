package cmm.apps.esmorga.datasource_local.di

import android.content.Context
import android.content.SharedPreferences
import cmm.apps.esmorga.data.device.datasource.DeviceDataSource
import cmm.apps.esmorga.data.di.DataDIModule
import cmm.apps.esmorga.data.event.datasource.EventDatasource
import cmm.apps.esmorga.data.user.datasource.UserDatasource
import cmm.apps.esmorga.datasource_local.database.EsmorgaDatabase
import cmm.apps.esmorga.datasource_local.database.EsmorgaDatabaseHelper
import cmm.apps.esmorga.datasource_local.database.dao.EventDao
import cmm.apps.esmorga.datasource_local.database.dao.UserDao
import cmm.apps.esmorga.datasource_local.device.DeviceLocalDataSourceImpl
import cmm.apps.esmorga.datasource_local.event.EventLocalDatasourceImpl
import cmm.apps.esmorga.datasource_local.user.UserLocalDatasourceImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

object LocalDIModule {

    private fun esmorgaDatabase(context: Context) = EsmorgaDatabaseHelper.getDatabase(context)

    val module = module {
        single { esmorgaDatabase(get()) }
        single<SharedPreferences> {
            val context: Context = get()
            context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        }
        single<EventDao> { get<EsmorgaDatabase>().eventDao() }
        factory<EventDatasource>(named(DataDIModule.LOCAL_DATASOURCE_INSTANCE_NAME)) { EventLocalDatasourceImpl(get()) }

        single<UserDao> { get<EsmorgaDatabase>().userDao() }
        factory<UserDatasource>(named(DataDIModule.LOCAL_DATASOURCE_INSTANCE_NAME)) { UserLocalDatasourceImpl(get()) }

        factory<DeviceDataSource>(named(DataDIModule.LOCAL_DATASOURCE_INSTANCE_NAME)) { DeviceLocalDataSourceImpl(get(), get()) }
    }

}