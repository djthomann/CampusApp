package hsrm.mi.campusapp.domain.model

import io.github.dellisd.spatialk.geojson.Position

data class Stop(
    val id: String,
    val campus: String, // TODO() Change later
    val name: String,
    val position: Position
)