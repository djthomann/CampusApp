package hsrm.mi.campusapp.domain.persistence

import androidx.room.Embedded
import androidx.room.Relation

data class MenuWithDishes(
    @Embedded val menu: MenuEntity,
    @Relation(
        parentColumn = "date",
        entityColumn = "menuId"
    )
    val dishes: List<DishEntity>
)