package hsrm.mi.campusapp.domain.model

import hsrm.mi.campusapp.domain.persistence.MenuEntity
import kotlinx.datetime.LocalDate

data class Menu (
    val canteen: String,
    val date: LocalDate,
    val dateString: String,
    val dishes: List<Dish>,
    val sideDishes: Map<SideDishType, List<String>>
)

fun Menu.toEntity(): MenuEntity {
    return MenuEntity(
        canteen = canteen,
        date = date.toString(),
        dateString = dateString
    )
}