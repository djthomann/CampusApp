package hsrm.mi.campusapp.data.api.rmv

import hsrm.mi.campusapp.domain.model.Departure
import hsrm.mi.campusapp.domain.model.Journey
import hsrm.mi.campusapp.domain.model.Vehicle
import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class JourneyDetailRef(
    val ref: String
)

@Serializable
data class VehicleDTO(
    val catOut: String
)

fun VehicleDTO.toDomain(): Vehicle {
    return when(catOut) {
        "Bus" -> Vehicle.BUS
        "S" -> Vehicle.S_BAHN
        "RB" -> Vehicle.REGIONAL_TRAIN
        else -> Vehicle.UNKNOWN
    }
}

@Serializable
data class DepartureDTO(
    @SerialName("JourneyDetailRef")
    val journeyDetailRef: JourneyDetailRef,
    @SerialName("ProductAtStop")
    val prodcutAtStop: VehicleDTO,
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
        vehicle = prodcutAtStop.toDomain(),
        name = name,
        time = time,
        direction = direction,
        journey = journey
    )
}