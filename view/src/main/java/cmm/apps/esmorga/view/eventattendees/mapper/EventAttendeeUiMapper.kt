package cmm.apps.esmorga.view.eventattendees.mapper

import cmm.apps.esmorga.domain.event.model.EventAttendee
import cmm.apps.esmorga.view.eventattendees.model.AttendeeUiModel
import org.koin.core.component.KoinComponent

object EventAttendeeUiMapper : KoinComponent {

    fun EventAttendee.toAttendeeUi(position: Int): AttendeeUiModel {
        return AttendeeUiModel(
            name = "${position + 1}. ${this.name}",
            checked = this.alreadyPaid
        )
    }

}
