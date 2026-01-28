package hsrm.mi.campusapp.domain.model

import org.maplibre.spatialk.geojson.Position

data class Canteen(
    val name: String,
    val campus: String, // TODO() Change later
    val position: Position
)