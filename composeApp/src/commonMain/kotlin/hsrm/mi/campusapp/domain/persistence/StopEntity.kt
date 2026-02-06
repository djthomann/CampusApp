package hsrm.mi.campusapp.domain.persistence

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
class StopEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String
)