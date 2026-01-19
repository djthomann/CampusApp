package hsrm.mi.campusapp.domain.model

data class Dish(
    val name: String,
    val description: String?,
    val price: String,
    val dishAllergens: String?
)