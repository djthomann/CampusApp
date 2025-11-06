package hsrm.mi.campusapp.model

import io.github.dellisd.spatialk.geojson.Position

data class Stop(
    val campus: String, // TODO() Change later
    val name: String,
    val position: Position
)