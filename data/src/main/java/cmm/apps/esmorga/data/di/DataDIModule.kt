package cmm.apps.esmorga.data.di

import cmm.apps.esmorga.data.event.EventRepositoryImpl
import cmm.apps.esmorga.domain.event.repository.EventRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module


object DataDIModule {

    const val EVENT_LOCAL_DATASOURCE_INSTANCE_NAME = "eventLocalDatasourceInstance"
    const val EVENT_REMOTE_DATASOURCE_INSTANCE_NAME = "eventRemoteDatasourceInstance"

    val module = module {
        factory<EventRepository> { EventRepositoryImpl(get(named(EVENT_LOCAL_DATASOURCE_INSTANCE_NAME)), get(named(EVENT_REMOTE_DATASOURCE_INSTANCE_NAME))) }
    }

}