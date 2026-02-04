package hsrm.mi.campusapp.data.api.rmv

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class TripResponse(
    @SerialName("Trip")
    val trips: List<TripDTO>
)