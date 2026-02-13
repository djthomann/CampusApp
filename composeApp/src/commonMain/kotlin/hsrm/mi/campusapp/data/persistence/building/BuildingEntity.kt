package hsrm.mi.campusapp.data.persistence.building

import androidx.room.Entity
import androidx.room.PrimaryKey
import hsrm.mi.campusapp.domain.model.Building
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class BuildingEntity(
    @PrimaryKey val id: String,
    val name: String,
    val longitude: Double,
    val latitude: Double
)

fun BuildingEntity.toDomain(): Building {
    return Building(
        id = id,
        name = name,
        longitude = longitude,
        latitude = latitude
    )
}