package hsrm.mi.campusapp.domain.model

import hsrm.mi.campusapp.domain.persistence.stop.StopEntity
import org.maplibre.spatialk.geojson.Position

data class Stop(
    val id: String,
    val name: String,
    val campus: String?, // TODO() Change later
    val position: Position
)

fun Stop.toEntity(): StopEntity {
    return StopEntity(
        id = id,
        name = name,
        campus = campus ?: "",
        longitude = position.longitude,
        latitude = position.latitude
    )
}