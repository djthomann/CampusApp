package hsrm.mi.campusapp.domain.model

import hsrm.mi.campusapp.domain.persistence.menu.DishEntity

data class Dish(
    val name: String,
    val description: String?,
    val price: String,
    val dishAllergens: String?
)

fun Dish.toEntity(menuId: Long): DishEntity {
    return DishEntity(
        menuId = menuId,
        name = name,
        description = description,
        price = price,
        dishAllergens = dishAllergens
    )
}