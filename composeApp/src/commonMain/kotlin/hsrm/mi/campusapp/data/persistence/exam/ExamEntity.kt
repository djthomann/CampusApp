package hsrm.mi.campusapp.data.persistence.exam

import androidx.room.Entity
import androidx.room.PrimaryKey
import hsrm.mi.campusapp.domain.model.Exam
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class ExamEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val studentEnrolled: Boolean,
    val date: String,
    val time: String,
    val buildingId: String,
    val room: String
)