package hsrm.mi.campusapp.data.api.rmv

import hsrm.mi.campusapp.domain.model.Journey
import hsrm.mi.campusapp.domain.model.JourneyStop
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class StopsWrapper(
    @SerialName("Stop")
    val stops: List<JourneyStopDTO>
)

@Serializable
@SerialName("Stop")
data class JourneyStopDTO(
    val name: String,
    val id: String
)

fun JourneyStopDTO.toDomain(): JourneyStop {
    return JourneyStop(
        name = name,
        id = id
    )
}

@Serializable
data class JourneyDTO(
    @SerialName("Stops")
    val journeyStops: StopsWrapper
) {
    @Transient
    val stops: List<JourneyStopDTO> = journeyStops.stops
}

fun JourneyDTO.toDomain(): Journey {
    return Journey(
        stops = stops.map { it.toDomain() }
    )
}