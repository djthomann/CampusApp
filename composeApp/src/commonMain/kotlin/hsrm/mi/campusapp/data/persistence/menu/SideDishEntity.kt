package hsrm.mi.campusapp.data.persistence.menu

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
            parentColumns = ["id"],
            childColumns = ["menuId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
)
data class SideDishEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val menuId: Long,
    val type: SideDishType,
    val name: String
)