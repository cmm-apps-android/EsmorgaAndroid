package cmm.apps.esmorga.view.eventdetails.mapper

import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.view.eventdetails.model.EventDetailsUiState
import cmm.apps.esmorga.view.eventdetails.model.EventDetailsUiStateHelper.getPrimaryButtonTitle
import cmm.apps.esmorga.view.eventlist.mapper.EventListUiMapper.formatDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

object EventDetailsUiMapper {

    fun Event.toEventUiDetails(isAuthenticated: Boolean, userJoined: Boolean): EventDetailsUiState {
        return EventDetailsUiState(
            id = this.id,
            image = this.imageUrl,
            title = this.name,
            subtitle = formatDate(this.date),
            description = this.description,
            locationName = this.location.name,
            locationLat = this.location.lat,
            locationLng = this.location.long,
            primaryButtonTitle = getPrimaryButtonTitle(isAuthenticated, userJoined)
        )
    }
}