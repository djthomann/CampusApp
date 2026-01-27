package hsrm.mi.campusapp.domain.model

import io.github.dellisd.spatialk.geojson.Position

data class Canteen(
    val name: String,
    val campus: String, // TODO() Change later
    val position: Position
)