package hsrm.mi.campusapp.domain.model

import hsrm.mi.campusapp.data.persistence.building.BuildingEntity

data class Building (
    val id: String,
    val name: String,
    val longitude: Double,
    val latitude: Double
)

fun Building.toEntity(): BuildingEntity {
    return BuildingEntity(
        id = id,
        name = name,
        longitude = longitude,
        latitude = latitude
    )
}
