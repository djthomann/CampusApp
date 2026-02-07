package hsrm.mi.campusapp.domain.model

import org.maplibre.spatialk.geojson.Position

data class Stop(
    val id: String,
    val name: String,
    val campus: String?, // TODO() Change later
    val position: Position
)