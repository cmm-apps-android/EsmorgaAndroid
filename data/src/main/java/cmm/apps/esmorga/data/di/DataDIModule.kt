package cmm.apps.esmorga.data.di

import cmm.apps.esmorga.data.event.EventRepositoryImpl
import cmm.apps.esmorga.domain.repository.EventRepository
import org.koin.dsl.module


object DataDIModule {

    val module = module {
        factory<EventRepository> { EventRepositoryImpl(get()) }
    }

}