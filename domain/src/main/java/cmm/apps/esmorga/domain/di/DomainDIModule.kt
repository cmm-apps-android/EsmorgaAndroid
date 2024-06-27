package cmm.apps.esmorga.domain.di

import cmm.apps.esmorga.domain.event.GetEventDetailsUseCase
import cmm.apps.esmorga.domain.event.GetEventDetailsUseCaseImpl
import cmm.apps.esmorga.domain.event.GetEventListUseCase
import cmm.apps.esmorga.domain.event.GetEventListUseCaseImpl
import org.koin.dsl.module


object DomainDIModule {

    val module = module {
        factory<GetEventListUseCase> { GetEventListUseCaseImpl(get()) }
        factory<GetEventDetailsUseCase> { GetEventDetailsUseCaseImpl(get()) }
    }

}