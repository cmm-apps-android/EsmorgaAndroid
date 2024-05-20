package cmm.apps.esmorga.view.di

import cmm.apps.esmorga.view.eventList.EventListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


object ViewDIModule {

    val module = module {
        viewModel {
            EventListViewModel(get(), get())
        }
    }
}