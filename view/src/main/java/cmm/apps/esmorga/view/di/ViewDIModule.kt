package cmm.apps.esmorga.view.di

import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.view.MainViewModel
import cmm.apps.esmorga.view.activateaccount.ActivateAccountViewModel
import cmm.apps.esmorga.view.eventdetails.EventDetailsViewModel
import cmm.apps.esmorga.view.eventlist.EventListViewModel
import cmm.apps.esmorga.view.eventlist.MyEventListViewModel
import cmm.apps.esmorga.view.login.LoginViewModel
import cmm.apps.esmorga.view.password.RecoverPasswordViewModel
import cmm.apps.esmorga.view.profile.ProfileViewModel
import cmm.apps.esmorga.view.registration.RegistrationConfirmationViewModel
import cmm.apps.esmorga.view.registration.RegistrationViewModel
import cmm.apps.esmorga.view.welcome.WelcomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


object ViewDIModule {

    val module = module {
        viewModel {
            MainViewModel(get())
        }
        viewModel {
            EventListViewModel(get())
        }
        viewModel {
            MyEventListViewModel(get())
        }
        viewModel { (event: Event) ->
            EventDetailsViewModel(get(), get(), get(), event)
        }
        viewModel {
            WelcomeViewModel()
        }
        viewModel {
            LoginViewModel(get())
        }
        viewModel {
            RegistrationViewModel(get())
        }

        viewModel {
            RegistrationConfirmationViewModel(get())
        }

        viewModel { ProfileViewModel(get(), get()) }

        viewModel { RecoverPasswordViewModel(get()) }

        viewModel { (verificationCode: String) ->
            ActivateAccountViewModel(verificationCode, get())
        }
    }
}