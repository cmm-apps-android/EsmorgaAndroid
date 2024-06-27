package cmm.apps.esmorga.view.di

import cmm.apps.esmorga.view.eventdetails.EventDetailsViewModel
import cmm.apps.esmorga.view.eventlist.EventListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


object ViewDIModule {

    val module = module {
        viewModel {
            EventListViewModel(get(), get())
        }
        viewModel {
            EventDetailsViewModel(get(), get())
        }
    }
}