package hsrm.mi.campusapp.domain.persistence

import androidx.room.Entity
import androidx.room.PrimaryKey
import hsrm.mi.campusapp.domain.model.Stop
import kotlinx.serialization.Serializable
import org.maplibre.spatialk.geojson.Position

@Entity
@Serializable
class StopEntity (
    @PrimaryKey val id: String,
    val name: String,
    val campus: String,
    val longitude: Double,
    val latitude: Double
)

fun StopEntity.toDomain(): Stop {
    return Stop(
        id = id,
        campus = campus,
        name = name,
        position = Position(longitude, latitude)
    )
}