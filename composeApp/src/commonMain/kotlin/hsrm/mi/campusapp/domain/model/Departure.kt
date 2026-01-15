package hsrm.mi.campusapp.domain.model

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
data class DepartureResponse(
    @SerialName("Departure")
    val departure: List<Departure>
)

@Serializable
data class JourneyDetailRef(
    val ref: String
)

@Serializable
data class Departure(
    @SerialName("JourneyDetailRef")
    val journeyDetailRef: JourneyDetailRef,
    val name: String,
    @Serializable(with = LocalTimeSerializer::class)
    val time: LocalTime,
    val direction: String,

    @Transient
    var journey: Journey? = null
) { @Transient
    val ref: String = journeyDetailRef.ref
}

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