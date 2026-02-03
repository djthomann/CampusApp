package hsrm.mi.campusapp.data.api.rmv

import hsrm.mi.campusapp.domain.model.Departure
import hsrm.mi.campusapp.domain.model.Journey
import kotlinx.datetime.LocalTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class JourneyDetailRef(
    val ref: String
)

@Serializable
data class DepartureDTO(
    @SerialName("JourneyDetailRef")
    val journeyDetailRef: JourneyDetailRef,
    val name: String,
    @Serializable(with = LocalTimeSerializer::class)
    val time: LocalTime,
    val direction: String,

    @Transient
    var journey: Journey? = null
)

object LocalTimeSerializer : KSerializer<LocalTime> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalTime) {
        encoder.encodeString(value.toString()) // "HH:mm:ss"
    }

    override fun deserialize(decoder: Decoder): LocalTime {
        return LocalTime.parse(decoder.decodeString())
    }
}

fun DepartureDTO.toDomain(): Departure {
    return Departure(
        journeyDetailRef = journeyDetailRef.ref,
        name = name,
        time = time,
        direction = direction,
        journey = journey
    )
}