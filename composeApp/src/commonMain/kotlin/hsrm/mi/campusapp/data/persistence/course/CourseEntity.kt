package hsrm.mi.campusapp.data.persistence.course

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import hsrm.mi.campusapp.data.persistence.building.BuildingEntity
import kotlinx.serialization.Serializable

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = BuildingEntity::class,
            parentColumns = ["id"],
            childColumns = ["buildingId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
@Serializable
data class CourseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val dayOfWeek: String,
    val start: String,
    val durationInMinutes: Int,
    val lecturer: String? = null,
    val room: String,
    val courseType: String,
    val buildingId: String
)