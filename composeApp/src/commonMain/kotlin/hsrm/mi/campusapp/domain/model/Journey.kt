package hsrm.mi.campusapp.domain.model


data class Journey(
    val stops: List<JourneyStop>
)


data class JourneyStop(
    val name: String,
    val id: String
)