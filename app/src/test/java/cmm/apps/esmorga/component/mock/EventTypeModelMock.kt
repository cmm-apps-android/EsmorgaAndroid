package cmm.apps.esmorga.component.mock

import cmm.apps.esmorga.domain.event.model.EventType
import cmm.apps.esmorga.view.createevent.model.CreateEventFormUiModel

object EventTypeModelMock {

    fun provide(name: String = "Test Event", description: String = "Description"): CreateEventFormUiModel =
        CreateEventFormUiModel(
            name = name,
            description = description,
            type = EventType.PARTY,
            imageUrl = "http://test.com/image.png",
            location = null,
            tags = listOf("tag1", "tag2")
        )

    fun provideList(nameList: List<String>): List<CreateEventFormUiModel> =
        nameList.map { name -> provide(name, "Desc for $name") }
}
