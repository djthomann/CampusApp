package hsrm.mi.campusapp.domain.persistence

import androidx.room.Entity
import androidx.room.PrimaryKey
import hsrm.mi.campusapp.domain.model.Campus
import kotlinx.serialization.Serializable
import org.maplibre.spatialk.geojson.Position

@Entity
@Serializable
class CampusEntity (
    @PrimaryKey val name: String,
    val longitude: Double,
    val latitude: Double,
    val tilt: Double,
    val jsonPath: String,
)

fun CampusEntity.toDomain(): Campus {
    return Campus(
        name = name,
        center = Position(longitude, latitude),
        tilt = tilt,
        jsonPath = jsonPath
    )
}