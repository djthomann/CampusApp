package hsrm.mi.campusapp.domain.persistence

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class MenuEntity(
    @PrimaryKey val date: String,
    val dateString: String,
)