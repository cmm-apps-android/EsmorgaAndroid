package cmm.apps.esmorga.data.event.mapper

import cmm.apps.esmorga.data.event.model.EventDataModel
import cmm.apps.esmorga.domain.event.model.Event


fun EventDataModel.toEvent(): Event = Event(name = this.dataName, date = this.dataDate)

fun List<EventDataModel>.toEventList(): List<Event> = map { edm -> edm.toEvent() }