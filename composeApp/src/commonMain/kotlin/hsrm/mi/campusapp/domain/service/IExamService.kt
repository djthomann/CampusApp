package hsrm.mi.campusapp.domain.service

import hsrm.mi.campusapp.data.persistence.exam.toDomain
import hsrm.mi.campusapp.domain.model.Course
import hsrm.mi.campusapp.domain.model.Exam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

interface IExamService {

    fun getAllExams(): Flow<List<Exam>>

    fun getExamsOnDay(day: LocalDate): Flow<List<Exam>>

}