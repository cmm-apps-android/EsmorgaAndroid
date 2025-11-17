package cmm.apps.esmorga.view.eventattendees.mapper

import cmm.apps.esmorga.domain.event.model.EventAttendee
import cmm.apps.esmorga.view.eventattendees.model.AttendeeUiModel
import org.koin.core.component.KoinComponent

object EventAttendeeUiMapper : KoinComponent {

    private fun EventAttendee.toAttendeeUi(): AttendeeUiModel {
        return AttendeeUiModel(
            name = this.name,
            checked = this.alreadyPaid
        )
    }

    fun List<EventAttendee>.toAttendeeUiList() = this.map { it.toAttendeeUi() }

}
