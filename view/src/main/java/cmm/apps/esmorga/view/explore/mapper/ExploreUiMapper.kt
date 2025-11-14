package cmm.apps.esmorga.view.explore.mapper

import android.content.Context
import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.domain.poll.model.Poll
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.dateformatting.EsmorgaDateTimeFormatter
import cmm.apps.esmorga.view.explore.model.ListCardUiModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object ExploreUiMapper : KoinComponent {
    private val context: Context by inject()

    private fun formatDate(date: Long): String {
        val formatter: EsmorgaDateTimeFormatter by inject()
        return formatter.formatEventDate(date)
    }

    private fun Event.toCardUi(): ListCardUiModel {

        return ListCardUiModel(
            id = this.id,
            imageUrl = this.imageUrl,
            cardTitle = this.name,
            cardSubtitle1 = formatDate(this.date),
            cardSubtitle2 = this.location.name
        )
    }

    fun List<Event>.eventListToCardUiList() = this.map { ev -> ev.toCardUi() }

    private fun Poll.toCardUi(): ListCardUiModel {
        return ListCardUiModel(
            id = this.id,
            imageUrl = this.imageUrl,
            cardTitle = this.name,
            cardSubtitle1 = context.getString(R.string.text_poll_vote_deadline).format(formatDate(this.voteDeadline)),
            cardSubtitle2 = null
        )
    }

    fun List<Poll>.pollListToCardUiList() = this.map { poll -> poll.toCardUi() }
}
