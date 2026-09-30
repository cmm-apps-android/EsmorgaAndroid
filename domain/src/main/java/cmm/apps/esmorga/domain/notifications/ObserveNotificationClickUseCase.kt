package cmm.apps.esmorga.domain.notifications

import cmm.apps.esmorga.domain.notifications.model.NotificationPayload
import cmm.apps.esmorga.domain.notifications.repository.NotificationRepository
import kotlinx.coroutines.flow.SharedFlow

fun interface ObserveNotificationClickUseCase {
    operator fun invoke(): SharedFlow<NotificationPayload>
}

class ObserveNotificationClickUseCaseImpl(
    private val notificationRepository: NotificationRepository
) : ObserveNotificationClickUseCase {
    override fun invoke(): SharedFlow<NotificationPayload> {
        return notificationRepository.notificationClickFlow
    }
}