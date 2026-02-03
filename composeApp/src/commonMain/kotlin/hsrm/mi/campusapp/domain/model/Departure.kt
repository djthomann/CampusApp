package hsrm.mi.campusapp.domain.model

import kotlinx.datetime.LocalTime

data class Departure(
    val journeyDetailRef: String,
    val name: String,
    val time: LocalTime,
    val direction: String,
    var journey: Journey? = null
)