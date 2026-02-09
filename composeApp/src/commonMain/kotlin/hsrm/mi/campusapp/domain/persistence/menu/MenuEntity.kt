package hsrm.mi.campusapp.domain.persistence.menu

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index(value = ["canteen", "date"], unique = true)],
)
data class MenuEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val canteen: String, // TODO() Change later
    val date: String,
    val dateString: String,
)