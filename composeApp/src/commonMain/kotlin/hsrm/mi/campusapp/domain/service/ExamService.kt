package hsrm.mi.campusapp.domain.service

import hsrm.mi.campusapp.data.persistence.course.toDomain
import hsrm.mi.campusapp.data.persistence.exam.ExamDao
import hsrm.mi.campusapp.data.persistence.exam.toDomain
import hsrm.mi.campusapp.domain.model.Course
import hsrm.mi.campusapp.domain.model.Exam
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

class ExamService(
    private val dao: ExamDao
): IExamService {

    private val allExamsFlow = dao.getAllWithBuildingAsFlow()
        .map { entities -> entities.map { it.toDomain() }}
        .stateIn(
            scope = CoroutineScope(Dispatchers.Default + SupervisorJob()),
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    override fun getAllExams(): Flow<List<Exam>> {
        return dao.getAllWithBuildingAsFlow().map { entities -> entities.map { it.toDomain() } }
    }

    override fun getExamsOnDay(day: LocalDate): Flow<List<Exam>> {
        return allExamsFlow.map { exams ->
            exams.filter { it.date == day }
        }
    }

}