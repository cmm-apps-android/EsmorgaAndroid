package cmm.apps.esmorga.domain.di

import cmm.apps.esmorga.domain.event.GetEventDetailsUseCase
import cmm.apps.esmorga.domain.event.GetEventDetailsUseCaseImpl
import cmm.apps.esmorga.domain.event.GetEventListUseCase
import cmm.apps.esmorga.domain.event.GetEventListUseCaseImpl
import cmm.apps.esmorga.domain.user.GetSavedUserUseCase
import cmm.apps.esmorga.domain.user.GetSavedUserUseCaseImpl
import cmm.apps.esmorga.domain.user.PerformLoginUseCase
import cmm.apps.esmorga.domain.user.PerformLoginUseCaseImpl
import cmm.apps.esmorga.domain.user.PerformRegistrationUserCaseImpl
import cmm.apps.esmorga.domain.user.PerformRegistrationUserCase
import org.koin.dsl.module


object DomainDIModule {

    val module = module {
        factory<GetEventListUseCase> { GetEventListUseCaseImpl(get()) }
        factory<GetEventDetailsUseCase> { GetEventDetailsUseCaseImpl(get()) }
        factory<PerformLoginUseCase> { PerformLoginUseCaseImpl(get()) }
        factory<PerformRegistrationUserCase> { PerformRegistrationUserCaseImpl(get()) }
        factory<GetSavedUserUseCase> { GetSavedUserUseCaseImpl(get()) }
    }

}