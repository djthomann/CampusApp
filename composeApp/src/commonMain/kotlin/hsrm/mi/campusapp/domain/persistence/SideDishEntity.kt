package hsrm.mi.campusapp.domain.persistence

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import hsrm.mi.campusapp.domain.model.SideDishType

@Entity(
    tableName = "SideDishEntity",
    indices = [Index(value = ["menuId", "type", "name"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = MenuEntity::class,
            parentColumns = ["date"],
            childColumns = ["menuId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
)
data class SideDishEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val menuId: String,
    val type: SideDishType,
    val name: String
)