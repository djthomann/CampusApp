package hsrm.mi.campusapp.domain.model

import io.github.dellisd.spatialk.geojson.Position

data class Campus(
    val name: String,
    val center: Position,
    val tilt: Double
)