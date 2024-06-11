package cmm.apps.esmorga.view.eventlist.mapper

import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.view.eventlist.model.EventUiModel
import java.time.format.DateTimeFormatter
import java.util.TimeZone


fun Event.toEventUi(): EventUiModel {
    val date = this.date.format(DateTimeFormatter.ofPattern("d' de 'MMMM' a las 'HH:mm").withZone(TimeZone.getDefault().toZoneId()))

    return EventUiModel(
        imageUrl = this.imageUrl,
        cardTitle = this.name,
        cardSubtitle1 = date,
        cardSubtitle2 = this.location.name
    )
}

fun List<Event>.toEventUiList() = this.map { ev -> ev.toEventUi() }