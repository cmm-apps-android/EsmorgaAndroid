package cmm.apps.esmorga.domain.di

import cmm.apps.esmorga.domain.event.GetEventDetailsUseCase
import cmm.apps.esmorga.domain.event.GetEventDetailsUseCaseImpl
import cmm.apps.esmorga.domain.event.GetEventListUseCase
import cmm.apps.esmorga.domain.event.GetEventListUseCaseImpl
import cmm.apps.esmorga.domain.event.GetMyEventListUseCase
import cmm.apps.esmorga.domain.event.GetMyEventListUseCaseImpl
import cmm.apps.esmorga.domain.event.JoinEventUseCase
import cmm.apps.esmorga.domain.event.JoinEventUseCaseImpl
import cmm.apps.esmorga.domain.event.LeaveEventUseCase
import cmm.apps.esmorga.domain.event.LeaveEventUseCaseImpl
import cmm.apps.esmorga.domain.user.GetSavedUserUseCase
import cmm.apps.esmorga.domain.user.GetSavedUserUseCaseImpl
import cmm.apps.esmorga.domain.user.PerformLoginUseCase
import cmm.apps.esmorga.domain.user.PerformLoginUseCaseImpl
import cmm.apps.esmorga.domain.user.PerformRegistrationUserCase
import cmm.apps.esmorga.domain.user.PerformRegistrationUserCaseImpl
import org.koin.dsl.module


object DomainDIModule {

    val module = module {
        factory<GetEventListUseCase> { GetEventListUseCaseImpl(get()) }
        factory<GetEventDetailsUseCase> { GetEventDetailsUseCaseImpl(get()) }
        factory<PerformLoginUseCase> { PerformLoginUseCaseImpl(get()) }
        factory<PerformRegistrationUserCase> { PerformRegistrationUserCaseImpl(get()) }
        factory<GetSavedUserUseCase> { GetSavedUserUseCaseImpl(get()) }
        factory<JoinEventUseCase> { JoinEventUseCaseImpl(get()) }
        factory<GetMyEventListUseCase> { GetMyEventListUseCaseImpl(get(), get()) }
        factory<LeaveEventUseCase> { LeaveEventUseCaseImpl(get()) }
    }

}