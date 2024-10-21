package cmm.apps.esmorga.view.di

import cmm.apps.esmorga.view.MainViewModel
import cmm.apps.esmorga.view.eventdetails.EventDetailsViewModel
import cmm.apps.esmorga.view.eventlist.EventListViewModel
import cmm.apps.esmorga.view.eventlist.MyEventListViewModel
import cmm.apps.esmorga.view.eventlist.model.EventListUiModel
import cmm.apps.esmorga.view.login.LoginViewModel
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
        viewModel { (event: EventListUiModel) ->
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
    }
}