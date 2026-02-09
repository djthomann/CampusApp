package hsrm.mi.campusapp.domain.model

import kotlinx.datetime.LocalTime

data class Trip(
    val startTime: LocalTime,
    val arrivalTime: LocalTime,
    val legs: List<Leg>
)

data class Leg(
    val name: String,
    val origin: String,
    val startTime: LocalTime,
    val destination: String,
    val endTime: LocalTime
)