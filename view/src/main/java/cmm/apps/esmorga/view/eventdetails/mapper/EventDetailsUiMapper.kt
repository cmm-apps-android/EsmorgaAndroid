package cmm.apps.esmorga.view.eventdetails.mapper

import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.view.eventdetails.model.EventDetailsUiState
import cmm.apps.esmorga.view.eventdetails.model.EventDetailsUiStateHelper
import cmm.apps.esmorga.view.eventdetails.model.EventDetailsUiStateHelper.getPrimaryButtonTitle
import cmm.apps.esmorga.view.eventlist.mapper.EventListUiMapper.formatDate
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

object EventDetailsUiMapper {

    fun Event.toEventUiDetails(isAuthenticated: Boolean, userJoined: Boolean, eventFull: Boolean) = EventDetailsUiState(
        id = this.id,
        image = this.imageUrl,
        title = this.name,
        subtitle = formatDate(this.date),
        description = URLDecoder.decode(this.description, StandardCharsets.UTF_8.toString()),
        locationName = this.location.name,
        locationLat = this.location.lat,
        locationLng = this.location.long,
        primaryButtonTitle = EventDetailsUiStateHelper.getPrimaryButtonTitle(
            isAuthenticated = isAuthenticated,
            userJoined = userJoined,
            eventFull = eventFull
        ),
        currentAttendeeCount = this.currentAttendeeCount,
        maxCapacity = this.maxCapacity
    )
}