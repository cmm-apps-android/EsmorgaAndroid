package cmm.apps.esmorga.view.polldetails.mapper

import android.content.Context
import cmm.apps.esmorga.domain.poll.model.Poll
import cmm.apps.esmorga.domain.poll.model.PollOption
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.dateformatting.EsmorgaDateTimeFormatter
import cmm.apps.esmorga.view.polldetails.model.PollDetailsUiState
import cmm.apps.esmorga.view.polldetails.model.PollOptionUiModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object PollDetailsUiMapper : KoinComponent {

    private val context: Context by inject()
    private val dateFormatter: EsmorgaDateTimeFormatter by inject()

    fun Poll.toPollUiDetails(internalOptions: List<PollOption>): PollDetailsUiState {
        val pollHadOptionSelected = this.options.find { it.userSelected } != null
        val userHasSelectedOption = this.options != internalOptions && internalOptions.find { it.userSelected } != null
        val deadlinePassed = System.currentTimeMillis() > this.voteDeadline

        val primaryButtonTitle = getPrimaryButtonTitle(
            userAlreadyVoted = pollHadOptionSelected,
            deadlinePassed = deadlinePassed
        )

        return PollDetailsUiState(
            id = this.id,
            title = this.name,
            voteDeadline = context.getString(R.string.text_poll_vote_deadline).format(dateFormatter.formatDateforView((this.voteDeadline))),
            description = this.description,
            image = this.imageUrl,
            isPrimaryButtonEnabled = userHasSelectedOption && !deadlinePassed,
            primaryButtonTitle = primaryButtonTitle,
            isMultipleChoice = this.isMultipleChoice,
            options = internalOptions.map {
                PollOptionUiModel(
                    id = it.optionId,
                    text = context.getString(R.string.text_poll_vote_count).format(it.text, it.voteCount),
                    isSelected = it.userSelected)
            }
        )
    }

    private fun getPrimaryButtonTitle(
        userAlreadyVoted: Boolean,
        deadlinePassed: Boolean
    ): String {
        return when {
            deadlinePassed -> context.getString(R.string.button_deadline_passed)
            userAlreadyVoted -> context.getString(R.string.button_poll_change_vote)
            else -> context.getString(R.string.button_poll_vote)
        }
    }

}