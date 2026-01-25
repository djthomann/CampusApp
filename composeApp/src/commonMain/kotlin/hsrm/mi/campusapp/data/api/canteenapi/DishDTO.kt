package hsrm.mi.campusapp.data.api.canteenapi

import hsrm.mi.campusapp.domain.model.Dish

data class DishDTO(
    val name: String,
    val description: String?,
    val price: String,
    val dishAllergens: String?
)

fun DishDTO.toDomain(): Dish {
    return Dish(
        name = name,
        description = description,
        price = price,
        dishAllergens = dishAllergens
    )
}
