package cmm.apps.esmorga.domain.event.model

import java.time.ZonedDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

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

object KZonedDateTimeSerializer : KSerializer<ZonedDateTime> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ZonedDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ZonedDateTime) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): ZonedDateTime {
        val string = decoder.decodeString()
        return ZonedDateTime.parse(string)
    }
}