package hsrm.mi.campusapp.domain.persistence

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import hsrm.mi.campusapp.domain.model.Dish

@Entity(
    tableName = "DishEntity",
    indices = [Index(value = ["menuId", "name"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = MenuEntity::class,
            parentColumns = ["id"],
            childColumns = ["menuId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DishEntity(
    @PrimaryKey(autoGenerate = true)
    val dishId: Long = 0,
    val menuId: Long,
    val name: String,
    val description: String?,
    val price: String,
    val dishAllergens: String?
)

fun DishEntity.toDomain(): Dish {
    return Dish(
        name = name,
        description = description,
        price = price,
        dishAllergens = dishAllergens
    )
}