package hsrm.mi.campusapp.domain.persistence

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "DishEntity",
    indices = [Index(value = ["menuId", "name"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = MenuEntity::class,
            parentColumns = ["date"],
            childColumns = ["menuId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DishEntity(
    @PrimaryKey(autoGenerate = true)
    val dishId: Long = 0,
    val menuId: String,
    val name: String,
    val description: String?,
    val price: String,
    val dishAllergens: String?
)