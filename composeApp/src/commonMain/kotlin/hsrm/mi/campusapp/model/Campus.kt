package hsrm.mi.campusapp.model

import io.github.dellisd.spatialk.geojson.Position

data class Campus(
    val name: String,
    val center: Position,
    val tilt: Double
)