package hsrm.mi.campusapp.data.persistence.menu

import androidx.room.Embedded
import androidx.room.Relation
import hsrm.mi.campusapp.domain.model.Menu
import kotlinx.datetime.LocalDate

data class MenuWithDishes(
    @Embedded val menu: MenuEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "menuId"
    )
    val dishes: List<DishEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "menuId"
    )
    val sideDishes: List<SideDishEntity>
)

fun MenuWithDishes.toDomain(): Menu {
    return Menu(
        canteen = menu.canteen,
        date = LocalDate.parse(menu.date),
        dateString = menu.dateString,
        dishes = dishes.map { dishEntity -> dishEntity.toDomain() },
        sideDishes = sideDishes.groupBy { it.type }.mapValues { (_, entities) -> entities.map { it.name } }
    )
}