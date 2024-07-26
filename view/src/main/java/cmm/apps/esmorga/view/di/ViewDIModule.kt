package cmm.apps.esmorga.view.di

import cmm.apps.esmorga.view.eventdetails.EventDetailsViewModel
import cmm.apps.esmorga.view.eventlist.EventListViewModel
import cmm.apps.esmorga.view.login.LoginViewModel
import cmm.apps.esmorga.view.welcome.WelcomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


object ViewDIModule {

    val module = module {
        viewModel {
            EventListViewModel(get(), get())
        }
        viewModel { (eventId: String) ->
            EventDetailsViewModel(get(), get(), eventId)
        }
        viewModel {
            WelcomeViewModel(get())
        }
        viewModel {
            LoginViewModel(get(), get())
        }
    }
}