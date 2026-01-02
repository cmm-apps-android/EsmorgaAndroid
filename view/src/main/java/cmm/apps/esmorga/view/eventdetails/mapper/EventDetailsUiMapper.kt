package cmm.apps.esmorga.view.eventdetails.mapper

import android.content.Context
import cmm.apps.designsystem.Disabled
import cmm.apps.designsystem.Enabled
import cmm.apps.esmorga.domain.event.model.Event
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.dateformatting.EsmorgaDateTimeFormatter
import cmm.apps.esmorga.view.eventdetails.model.EventDetailsUiState
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object EventDetailsUiMapper : KoinComponent {

    private val context: Context by inject()
    private val dateFormatter: EsmorgaDateTimeFormatter by inject()

    fun Event.toEventUiDetails(
        isAuthenticated: Boolean
    ): EventDetailsUiState {
        val isDeadlinePassed = System.currentTimeMillis() > this.joinDeadline
        val isEventFull = this.maxCapacity?.let { this.currentAttendeeCount >= it } ?: false

        val primaryButtonTitle = getPrimaryButtonTitle(
            isAuthenticated = isAuthenticated,
            userJoined = this.userJoined,
            eventFull = isEventFull,
            isDeadlinePassed = isDeadlinePassed
        )

        val isPrimaryButtonEnabled = getButtonEnableStatus(
            isAuthenticated = isAuthenticated,
            userJoined = this.userJoined,
            eventFull = isEventFull,
            isDeadlinePassed = isDeadlinePassed
        )

        return EventDetailsUiState(
            id = this.id,
            title = this.name,
            date = dateFormatter.formatDateforView(this.date),
            description = this.description,
            image = this.imageUrl,
            locationName = this.location.name,
            showNavigateButton = this.location.lat != null && this.location.long != null,
            primaryButtonState = if (isPrimaryButtonEnabled) Enabled(primaryButtonTitle) else Disabled(primaryButtonTitle),
            currentAttendeeCountText = getCurrentAttendeeCountString(this.maxCapacity, this.currentAttendeeCount),
            joinDeadline = dateFormatter.formatDateforView(this.joinDeadline),
            showViewAttendeesButton = this.currentAttendeeCount > 0 && isAuthenticated
        )
    }

    private fun getPrimaryButtonTitle(
        isAuthenticated: Boolean,
        userJoined: Boolean,
        eventFull: Boolean,
        isDeadlinePassed: Boolean
    ): String {
        return when {
            !isAuthenticated -> context.getString(R.string.button_login_to_join)
            userJoined -> context.getString(R.string.button_leave_event)
            isDeadlinePassed -> context.getString(R.string.button_deadline_passed)
            !userJoined && eventFull -> context.getString(R.string.button_join_event_disabled)
            else -> context.getString(R.string.button_join_event)
        }
    }

    private fun getButtonEnableStatus(
        eventFull: Boolean,
        userJoined: Boolean,
        isDeadlinePassed: Boolean,
        isAuthenticated: Boolean
    ): Boolean {
        if (!isAuthenticated) return true
        return userJoined || (!eventFull && !isDeadlinePassed)
    }

    private fun getCurrentAttendeeCountString(maxCapacity: Int?, attendees: Int): String? = maxCapacity?.let {
        context.getString(R.string.screen_event_details_capacity, attendees, maxCapacity)
    }

}