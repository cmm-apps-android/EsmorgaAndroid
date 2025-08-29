package cmm.apps.esmorga.component.mock

import cmm.apps.esmorga.domain.event.model.EventType
import cmm.apps.esmorga.domain.event.model.CreateEventForm

object CreateEventFormModelMock {

    fun provide(name: String = "Test Event", description: String = "Description"): CreateEventForm =
        CreateEventForm(
            name = name,
            description = description,
            type = EventType.PARTY,
            imageUrl = "http://test.com/image.png",
            location = null,
            tags = listOf("tag1", "tag2")
        )
}