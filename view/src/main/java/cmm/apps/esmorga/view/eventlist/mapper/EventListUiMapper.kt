package cmm.apps.esmorga.view.eventlist.mapper

import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.domain.event.model.EventLocation
import cmm.apps.esmorga.view.eventlist.model.EventListUiModel
import cmm.apps.esmorga.view.eventlist.model.EventUILocation
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.TimeZone

private const val DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSVV"

object EventListUiMapper {

    fun formatDate(date: ZonedDateTime): String {
        return date.format(DateTimeFormatter.ofPattern("d' de 'MMMM' a las 'HH:mm").withZone(TimeZone.getDefault().toZoneId()))
    }

    fun Event.toEventUi(): EventListUiModel {

        return EventListUiModel(
            id = this.id,
            name = this.name,
            date = DateTimeFormatter.ofPattern(DATE_PATTERN).format(this.date),
            dateFormatted = formatDate(this.date),
            description = this.description,
            type = this.type,
            imageUrl = this.imageUrl,
            location = EventUILocation(name = this.location.name, lat = this.location.lat, long = this.location.long),
            tags = this.tags,
            userJoined = this.userJoined
        )
    }

    fun List<Event>.toEventUiList() = this.map { ev -> ev.toEventUi() }

    fun EventListUiModel.toEvent(): Event {
        val parsedDate = try {
            ZonedDateTime.from(DateTimeFormatter.ofPattern(DATE_PATTERN).parse(this.date))
        } catch (e: Exception) {
            throw DateTimeParseException("Error parsing date in EventListUiModel", this.date, 0, e)
        }
        return Event(
            id = this.id,
            name = this.name,
            date = parsedDate,
            description = this.description,
            type = this.type,
            imageUrl = this.imageUrl,
            location = EventLocation(name = this.location.name, lat = this.location.lat, long = this.location.long),
            tags = this.tags,
            userJoined = this.userJoined
        )
    }
}
