package hsrm.mi.campusapp.data.api.canteen

import hsrm.mi.campusapp.domain.model.SideDishType

data class SideDishDTO (
    val type: SideDishType,
    val sideDishes: List<String>
)