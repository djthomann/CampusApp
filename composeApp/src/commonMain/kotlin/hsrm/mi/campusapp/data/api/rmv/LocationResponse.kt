package hsrm.mi.campusapp.data.api.rmv

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationResponse(
    @SerialName("stopLocationOrCoordLocation")
    val stopsWrapper: List<StopLocationWrapper>
)

@Serializable
data class StopLocationWrapper(
    @SerialName("StopLocation")
    val stopLocation: StopLocationDTO
)

@Serializable
class StopLocationDTO(
    val id: String,
    val name: String,
    val lon: Double,
    val lat: Double
)