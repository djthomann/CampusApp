package hsrm.mi.campusapp.domain.persistence

import androidx.room.Entity
import androidx.room.PrimaryKey
import hsrm.mi.campusapp.domain.model.Canteen
import kotlinx.serialization.Serializable
import org.maplibre.spatialk.geojson.Position

@Entity
@Serializable
class CanteenEntity(
    @PrimaryKey val name: String,
    val url: String
)

fun CanteenEntity.toDomain(): Canteen {
    return Canteen(
        name = name,
        campus = "",
        position = Position(0.0, 0.0),
        url = url,
    )
}