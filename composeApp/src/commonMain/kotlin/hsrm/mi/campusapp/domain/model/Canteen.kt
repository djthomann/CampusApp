package hsrm.mi.campusapp.domain.model

import hsrm.mi.campusapp.domain.persistence.canteen.CanteenEntity
import org.maplibre.spatialk.geojson.Position

data class Canteen(
    val name: String,
    val campus: String, // TODO() Change later
    val position: Position,
    val url: String
)

fun Canteen.toEntity(): CanteenEntity {
    return CanteenEntity(
        name = name,
        campus = campus,
        longitude = position.longitude,
        latitude = position.latitude,
        url = url
    )
}