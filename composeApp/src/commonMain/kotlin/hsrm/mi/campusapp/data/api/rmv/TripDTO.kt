package hsrm.mi.campusapp.data.api.rmv

import hsrm.mi.campusapp.domain.model.Leg
import hsrm.mi.campusapp.domain.model.Trip
import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TripDTO(
    @SerialName("Origin")
    val origin: OriginDTO,
    @SerialName("Destination")
    val destination: DestinationDTO,
    @SerialName("LegList")
    val legList: LegListDTO
)

fun TripDTO.toDomain(): Trip {
    return Trip(
        startTime = origin.time,
        arrivalTime = destination.time,
        legs = legList.legs.map { it.toDomain()}
    )
}

@Serializable
data class LegListDTO(
    @SerialName("Leg")
    val legs: List<LegDTO>
)

@Serializable
data class LegDTO(
    val name: String,
    @SerialName("Origin")
    val origin: OriginDTO,
    @SerialName("Destination")
    val destination: DestinationDTO
)

fun LegDTO.toDomain(): Leg {
    return Leg(
        name = name,
        origin = origin.name,
        destination = destination.name
    )
}

@Serializable
data class OriginDTO(
    val name: String,
    @Serializable(with = LocalTimeSerializer::class)
    val time: LocalTime
)

@Serializable
data class DestinationDTO(
    val name: String,
    @Serializable(with = LocalTimeSerializer::class)
    val time: LocalTime
)