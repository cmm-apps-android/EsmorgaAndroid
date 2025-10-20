package cmm.apps.esmorga.view.di

import cmm.apps.esmorga.domain.event.model.CreateEventForm
import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.view.MainViewModel
import cmm.apps.esmorga.view.activateaccount.ActivateAccountViewModel
import cmm.apps.esmorga.view.changepassword.ChangePasswordViewModel
import cmm.apps.esmorga.view.createevent.CreateEventFormTitleViewModel
import cmm.apps.esmorga.view.createeventdate.CreateEventFormDateViewModel
import cmm.apps.esmorga.view.createeventtype.CreateEventFormTypeViewModel
import cmm.apps.esmorga.view.dateformatting.DateFormatterImpl
import cmm.apps.esmorga.view.dateformatting.EsmorgaDateTimeFormatter
import cmm.apps.esmorga.view.eventattendees.EventAttendeesViewModel
import cmm.apps.esmorga.view.eventdetails.EventDetailsViewModel
import cmm.apps.esmorga.view.eventlist.EventListViewModel
import cmm.apps.esmorga.view.eventlist.MyEventListViewModel
import cmm.apps.esmorga.view.login.LoginViewModel
import cmm.apps.esmorga.view.password.RecoverPasswordViewModel
import cmm.apps.esmorga.view.password.ResetPasswordViewModel
import cmm.apps.esmorga.view.profile.ProfileViewModel
import cmm.apps.esmorga.view.registration.RegistrationConfirmationViewModel
import cmm.apps.esmorga.view.registration.RegistrationViewModel
import cmm.apps.esmorga.view.welcome.WelcomeViewModel
import org.koin.core.module.dsl.viewModel
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
            MyEventListViewModel(get(), get())
        }
        viewModel { (event: Event) ->
            EventDetailsViewModel(get(), get(), get(), event)
        }
        viewModel { (event: Event) ->
            EventAttendeesViewModel(get(), event.id)
        }
        viewModel {
            WelcomeViewModel(get())
        }
        viewModel { (message: String?) ->
            LoginViewModel(get(), message)
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

        viewModel { ResetPasswordViewModel(get()) }

        viewModel { ChangePasswordViewModel(get()) }

        viewModel { CreateEventFormTitleViewModel() }

        viewModel { (eventForm: CreateEventForm) ->
            CreateEventFormTypeViewModel(eventForm)
        }

        viewModel { (eventForm: CreateEventForm) ->
            CreateEventFormDateViewModel(eventForm, get())
        }

        single<EsmorgaDateTimeFormatter> { DateFormatterImpl() }
    }
}