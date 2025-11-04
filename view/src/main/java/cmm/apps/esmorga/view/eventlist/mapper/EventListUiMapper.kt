package cmm.apps.esmorga.view.eventlist.mapper

import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.view.dateformatting.EsmorgaDateTimeFormatter
import cmm.apps.esmorga.view.eventlist.model.EventListUiModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object EventListUiMapper : KoinComponent {

    private fun formatDate(date: Long): String {
        val formatter: EsmorgaDateTimeFormatter by inject()
        return formatter.formatEventDate(date)
    }

    private fun Event.toEventUi(): EventListUiModel {

        return EventListUiModel(
            id = this.id,
            imageUrl = this.imageUrl,
            cardTitle = this.name,
            cardSubtitle1 = formatDate(this.date),
            cardSubtitle2 = this.location.name
        )
    }

    fun List<Event>.toEventUiList() = this.map { ev -> ev.toEventUi() }
}
