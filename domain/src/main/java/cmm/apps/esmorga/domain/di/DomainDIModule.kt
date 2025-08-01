package cmm.apps.esmorga.domain.di

import cmm.apps.esmorga.domain.account.ActivateAccountUseCase
import cmm.apps.esmorga.domain.account.ActivateAccountUseCaseImpl
import cmm.apps.esmorga.domain.device.GetDeviceIdUseCase
import cmm.apps.esmorga.domain.device.GetDeviceIdUseCaseImpl
import cmm.apps.esmorga.domain.device.ShowDeviceIdIfNeededUseCase
import cmm.apps.esmorga.domain.device.ShowDeviceIdIfNeededUseCaseImpl
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
import cmm.apps.esmorga.domain.user.LogOutUseCase
import cmm.apps.esmorga.domain.user.LogOutUseCaseImpl
import cmm.apps.esmorga.domain.user.PerformChangePasswordUseCase
import cmm.apps.esmorga.domain.user.PerformChangePasswordUseCaseImpl
import cmm.apps.esmorga.domain.user.PerformLoginUseCase
import cmm.apps.esmorga.domain.user.PerformLoginUseCaseImpl
import cmm.apps.esmorga.domain.user.PerformRecoverPasswordUseCase
import cmm.apps.esmorga.domain.user.PerformRecoverPasswordUseCaseImpl
import cmm.apps.esmorga.domain.user.PerformRegistrationConfirmationUseCase
import cmm.apps.esmorga.domain.user.PerformRegistrationConfirmationUseCaseImpl
import cmm.apps.esmorga.domain.user.PerformRegistrationUserCase
import cmm.apps.esmorga.domain.user.PerformRegistrationUserCaseImpl
import cmm.apps.esmorga.domain.user.repository.PerformResetPasswordUseCase
import cmm.apps.esmorga.domain.user.repository.PerformResetPasswordUseCaseImpl
import org.koin.dsl.module


object DomainDIModule {

    val module = module {
        factory<GetEventListUseCase> { GetEventListUseCaseImpl(get()) }
        factory<GetEventDetailsUseCase> { GetEventDetailsUseCaseImpl(get()) }
        factory<PerformLoginUseCase> { PerformLoginUseCaseImpl(get()) }
        factory<PerformRegistrationUserCase> { PerformRegistrationUserCaseImpl(get()) }
        factory<PerformRegistrationConfirmationUseCase> { PerformRegistrationConfirmationUseCaseImpl(get()) }
        factory<GetSavedUserUseCase> { GetSavedUserUseCaseImpl(get()) }
        factory<JoinEventUseCase> { JoinEventUseCaseImpl(get()) }
        factory<GetMyEventListUseCase> { GetMyEventListUseCaseImpl(get(), get()) }
        factory<LeaveEventUseCase> { LeaveEventUseCaseImpl(get()) }
        factory<LogOutUseCase> { LogOutUseCaseImpl(get()) }
        factory<PerformRecoverPasswordUseCase> { PerformRecoverPasswordUseCaseImpl(get()) }
        factory<ActivateAccountUseCase> { ActivateAccountUseCaseImpl(get()) }
        factory<PerformResetPasswordUseCase> { PerformResetPasswordUseCaseImpl(get()) }
        factory<GetDeviceIdUseCase> { GetDeviceIdUseCaseImpl(get()) }
        factory<ShowDeviceIdIfNeededUseCase> { ShowDeviceIdIfNeededUseCaseImpl(get()) }
        factory<PerformChangePasswordUseCase> { PerformChangePasswordUseCaseImpl(get()) }
    }
}