package hsrm.mi.campusapp.data.api.rmv

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DepartureResponse(
    @SerialName("Departure")
    val departures: List<DepartureDTO>
)