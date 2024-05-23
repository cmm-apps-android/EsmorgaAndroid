package cmm.apps.esmorga.datasource_local.di

import android.content.Context
import cmm.apps.esmorga.data.event.datasource.EventDatasource
import cmm.apps.esmorga.data.di.DataDIModule
import cmm.apps.esmorga.datasource_local.database.EsmorgaDatabase
import cmm.apps.esmorga.datasource_local.database.EsmorgaDatabaseHelper
import cmm.apps.esmorga.datasource_local.database.dao.EventDao
import cmm.apps.esmorga.datasource_local.event.EventLocalDatasourceImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

object LocalDIModule {

    private fun esmorgaDatabase(context: Context) = EsmorgaDatabaseHelper.getDatabase(context)

    val module = module {
        single { esmorgaDatabase(get()) }
        single<EventDao> { get<EsmorgaDatabase>().eventDao() }
        factory<EventDatasource>(named(DataDIModule.EVENT_LOCAL_DATASOURCE_INSTANCE_NAME)) { EventLocalDatasourceImpl(get()) }
    }

}