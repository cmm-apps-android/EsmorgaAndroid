package cmm.apps.esmorga.domain.event.model

import java.time.ZonedDateTime
import kotlinx.serialization.Serializable

enum class EventType {
    PARTY, SPORT, FOOD, CHARITY, GAMES
}

@Serializable
data class Event(
    val id: String,
    val name: String,
    @Serializable(KZonedDateTimeSerializer::class) val date: ZonedDateTime,
    val description: String,
    val type: EventType,
    val imageUrl: String? = null,
    val location: EventLocation,
    val tags: List<String> = listOf(),
    val userJoined: Boolean
)

@Serializable
data class EventLocation(
    val name: String,
    val lat: Double? = null,
    val long: Double? = null
)