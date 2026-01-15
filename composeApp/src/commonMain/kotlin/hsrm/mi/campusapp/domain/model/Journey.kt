package hsrm.mi.campusapp.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient


@Serializable
data class Journey(
    @SerialName("Stops")
    val journeyStops: StopsWrapper
) {
    @Transient
    val stops: List<JourneyStop> = journeyStops.stops
}

@Serializable
data class StopsWrapper(
    @SerialName("Stop")
    val stops: List<JourneyStop>
)

@Serializable
@SerialName("Stop")
data class JourneyStop(
    val name: String
)