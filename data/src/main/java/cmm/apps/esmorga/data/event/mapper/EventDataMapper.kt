package cmm.apps.esmorga.data.event.mapper

import cmm.apps.esmorga.data.event.model.EventDataModel
import cmm.apps.esmorga.data.event.model.EventLocationDataModel
import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.domain.event.model.EventLocation


fun EventDataModel.toEvent(): Event = Event(
    id = this.dataId,
    name = this.dataName,
    date = this.dataDate,
    description = this.dataDescription,
    type = this.dataType,
    imageUrl = this.dataImageUrl,
    location = EventLocation(this.dataLocation.name, this.dataLocation.lat, this.dataLocation.long),
    tags = this.dataTags,
    userJoined = this.dataUserJoined
)

fun List<EventDataModel>.toEventList(): List<Event> = map { edm -> edm.toEvent() }

fun Event.toEventDataModel(): EventDataModel {
    return EventDataModel(
        dataId = this.id,
        dataName = this.name,
        dataDate = this.date,
        dataDescription = this.description,
        dataType = this.type,
        dataImageUrl = this.imageUrl,
        dataLocation = EventLocationDataModel(this.location.name, this.location.lat, this.location.long),
        dataTags = this.tags,
        dataUserJoined = this.userJoined
    )
}