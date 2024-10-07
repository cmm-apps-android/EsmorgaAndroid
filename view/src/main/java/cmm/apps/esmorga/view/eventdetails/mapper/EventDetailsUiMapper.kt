package cmm.apps.esmorga.view.eventdetails.mapper

import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.view.eventdetails.model.EventDetailsUiState
import cmm.apps.esmorga.view.eventlist.mapper.EventListUiMapper.formatDate

object EventDetailsUiMapper {

    fun Event.toEventUiDetails(isAuthenticated: Boolean): EventDetailsUiState = EventDetailsUiState(
        id = this.id,
        image = this.imageUrl,
        title = this.name,
        subtitle = formatDate(date),
        description = this.description,
        locationName = this.location.name,
        locationLat = this.location.lat,
        locationLng = this.location.long,
        userJoined = this.userJoined,
        isAuthenticated = isAuthenticated
    )
}