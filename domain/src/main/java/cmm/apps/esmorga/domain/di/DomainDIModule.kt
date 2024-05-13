package cmm.apps.esmorga.domain.di

import cmm.apps.esmorga.domain.GetEventListUseCase
import cmm.apps.esmorga.domain.GetEventListUseCaseImpl
import org.koin.dsl.module


object DomainDIModule {

    val module = module {
        factory<GetEventListUseCase> { GetEventListUseCaseImpl(get()) }
    }

}