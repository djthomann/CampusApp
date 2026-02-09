package hsrm.mi.campusapp.domain.model

import hsrm.mi.campusapp.domain.persistence.campus.CampusEntity
import org.maplibre.spatialk.geojson.Position

data class Campus(
    val name: String,
    val center: Position,
    val tilt: Double,
    val jsonPath: String,
)

fun Campus.toEntity(): CampusEntity {
    return CampusEntity(
        name = name,
        longitude = center.longitude,
        latitude = center.latitude,
        tilt = tilt,
        jsonPath = jsonPath
    )
}