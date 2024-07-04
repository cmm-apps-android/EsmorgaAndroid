package cmm.apps.esmorga.view.eventlist.mapper

import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.view.eventdetails.model.EventDetailsUiState
import cmm.apps.esmorga.view.eventlist.model.EventListUiModel
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone

object EventUiMapper {

    private fun formatDate(date: ZonedDateTime) : String{
        return date.format(DateTimeFormatter.ofPattern("d' de 'MMMM' a las 'HH:mm").withZone(TimeZone.getDefault().toZoneId()))
    }

    private fun Event.toEventUi(): EventListUiModel {

        return EventListUiModel(
            id = this.id,
            imageUrl = this.imageUrl,
            cardTitle = this.name,
            cardSubtitle1 = formatDate(date),
            cardSubtitle2 = this.location.name
        )
    }
    fun List<Event>.toEventUiList() = this.map { ev -> ev.toEventUi() }

    fun Event.toEventUiDetails(): EventDetailsUiState = EventDetailsUiState(
        id = this.id,
        image = this.imageUrl,
        title = this.name,
        subtitle = formatDate(date),
        description = this.description,
        locationName = this.location.name,
        locationLat = this.location.lat,
        locationLng = this.location.long
    )
}
