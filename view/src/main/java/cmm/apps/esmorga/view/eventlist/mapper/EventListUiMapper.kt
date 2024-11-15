package cmm.apps.esmorga.view.eventlist.mapper

import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.view.eventlist.model.EventListUiModel
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone

object EventListUiMapper {

    fun formatDate(date: Long): String {
        val zonedDateTime: ZonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(date), TimeZone.getDefault().toZoneId())
        return zonedDateTime.format(DateTimeFormatter.ofPattern("d' de 'MMMM' a las 'HH:mm").withZone(TimeZone.getDefault().toZoneId()))
    }

    private fun Event.toEventUi(): EventListUiModel {

        return EventListUiModel(
            id = this.id,
            imageUrl = this.imageUrl?.let { URLDecoder.decode(it, StandardCharsets.UTF_8.toString()) },
            cardTitle = this.name,
            cardSubtitle1 = formatDate(this.date),
            cardSubtitle2 = this.location.name
        )
    }

    fun List<Event>.toEventUiList() = this.map { ev -> ev.toEventUi() }
}
