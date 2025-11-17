package cmm.apps.esmorga.view.polldetails.mapper

import android.content.Context
import cmm.apps.esmorga.domain.poll.model.Poll
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.dateformatting.EsmorgaDateTimeFormatter
import cmm.apps.esmorga.view.polldetails.model.PollDetailsUiState
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object PollDetailsUiMapper : KoinComponent {

    private val context: Context by inject()
    private val dateFormatter: EsmorgaDateTimeFormatter by inject()

    fun Poll.toPollUiDetails(): PollDetailsUiState {
        val userHasSelectedOption = this.options.find { it.userSelected } != null //TODO ensure this does not change until vote has been recorded

        val primaryButtonTitle = getPrimaryButtonTitle(
            userAlreadyVoted = userHasSelectedOption
        )

        return PollDetailsUiState(
            id = this.id,
            title = this.name,
            voteDeadline = context.getString(R.string.text_poll_vote_deadline).format(dateFormatter.formatDateforView((this.voteDeadline))),
            description = this.description,
            image = this.imageUrl,
            isPrimaryButtonEnabled = userHasSelectedOption,
            primaryButtonTitle = primaryButtonTitle,
            isMultipleChoice = this.isMultipleChoice,
            options = this.options.map { it.text } //TODO ensure the right option is sent to backend
        )
    }

    private fun getPrimaryButtonTitle(
        userAlreadyVoted: Boolean
    ): String {
        return when {
            userAlreadyVoted -> context.getString(R.string.button_poll_change_vote)
            else -> context.getString(R.string.button_poll_vote)
        }
    }

}