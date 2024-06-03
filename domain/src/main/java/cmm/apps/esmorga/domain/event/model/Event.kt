package cmm.apps.esmorga.domain.event.model

import java.time.ZonedDateTime

enum class EventType {
    PARTY, SPORT, FOOD, CHARITY, GAMES
}

data class Event(
    val id: String,
    val name: String,
    val date: ZonedDateTime,
    val description: String,
    val type: EventType,
    val imageUrl: String? = null,
    val location: EventLocation,
    val tags: List<String> = listOf(),
)

data class EventLocation(
    val name: String,
    val lat: Double? = null,
    val long: Double? = null
)