package hsrm.mi.campusapp.data.api.rmv

import hsrm.mi.campusapp.domain.model.Departure
import hsrm.mi.campusapp.domain.model.Journey
import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

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



fun DepartureDTO.toDomain(): Departure {
    return Departure(
        journeyDetailRef = journeyDetailRef.ref,
        name = name,
        time = time,
        direction = direction,
        journey = journey
    )
}