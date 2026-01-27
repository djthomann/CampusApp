package hsrm.mi.campusapp.domain.model

import kotlinx.datetime.LocalDate

data class Menu (
    val date: LocalDate,
    val dateString: String,
    val dishes: List<Dish>,
    val sideDishes: Map<SideDishType, List<String>>
)