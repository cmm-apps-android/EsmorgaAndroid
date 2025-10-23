package cmm.apps.esmorga.data.event.model


data class EventAttendeeDataModel(
    val dataName: String,
    val dataCreationTime: Long = System.currentTimeMillis()
)
