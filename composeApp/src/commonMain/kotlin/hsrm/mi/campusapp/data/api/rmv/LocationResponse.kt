package hsrm.mi.campusapp.data.api.rmv

import hsrm.mi.campusapp.domain.model.Stop
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.maplibre.spatialk.geojson.Position

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

fun StopLocationDTO.toDomain(): Stop {
    return Stop(
        id = id,
        name = name,
        campus = null,
        position = Position(lon, lat)
    )
}