package hsrm.mi.campusapp.data.api.canteen

import hsrm.mi.campusapp.domain.model.SideDishType

object SideDishMapper {

    private val mapping = mapOf(
        "Warme Beilagen" to SideDishType.GARNISH,
        "Desserts" to SideDishType.DESSERT,
        "Salate" to SideDishType.SALAD
    )

    fun sideDishTypeFromHTML(html: String): SideDishType? {
        return mapping[html]
    }

}