package hsrm.mi.campusapp.domain.persistence

import androidx.room.TypeConverter
import hsrm.mi.campusapp.domain.model.SideDishType

class SideDishTypeConverter {

    @TypeConverter
    fun fromString(value: String?): SideDishType? =
        value?.let { SideDishType.valueOf(it) }

    @TypeConverter
    fun toString(type: SideDishType?): String? =
        type?.name
}
