package cmm.apps.esmorga.view.eventdetails.mapper

import cmm.apps.esmorga.view.eventdetails.model.EventDetailsUiState
import cmm.apps.esmorga.view.eventdetails.model.EventDetailsUiStateHelper.getPrimaryButtonTitle
import cmm.apps.esmorga.view.eventlist.model.EventListUiModel

object EventDetailsUiMapper {

    fun EventListUiModel.toEventUiDetails(isAuthenticated: Boolean, userJoined: Boolean): EventDetailsUiState = EventDetailsUiState(
        id = this.id,
        image = this.imageUrl,
        title = this.name,
        subtitle = this.dateFormatted,
        description = this.description,
        locationName = this.location.name,
        locationLat = this.location.lat,
        locationLng = this.location.long,
        primaryButtonTitle = getPrimaryButtonTitle(isAuthenticated, userJoined)
    )
}