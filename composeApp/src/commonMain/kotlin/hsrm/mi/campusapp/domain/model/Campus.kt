package hsrm.mi.campusapp.domain.model

import org.maplibre.spatialk.geojson.Position

data class Campus(
    val name: String,
    val center: Position,
    val tilt: Double
)