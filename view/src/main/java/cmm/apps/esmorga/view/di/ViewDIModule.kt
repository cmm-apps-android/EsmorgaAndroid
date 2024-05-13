package cmm.apps.esmorga.view.di

import cmm.apps.esmorga.view.EventListViewModel
import org.koin.androidx.compose.get
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module


object ViewDIModule {

    val module = module {
        viewModel {
            EventListViewModel(get(), get())
        }
    }
}