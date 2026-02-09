package hsrm.mi.campusapp.domain.service

import hsrm.mi.campusapp.data.persistence.DatabaseHolder
import hsrm.mi.campusapp.data.persistence.course.toDomain
import hsrm.mi.campusapp.domain.model.Course
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.DayOfWeek

object CourseService {

    val dao = DatabaseHolder.db.getCourseDao()

    private val allCourseFlow = dao.getAllAsFlow()
        .map { entities -> entities.map { it.toDomain() }}
        .stateIn(
            scope = CoroutineScope(Dispatchers.Default + SupervisorJob()),
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun getAllCourses(): Flow<List<Course>> {
        return dao.getAllAsFlow().map { entities -> entities.map { it.toDomain() } }
    }

    fun getCoursesForDayOfWeek(dayOfWeek: DayOfWeek): Flow<List<Course>> {
        return allCourseFlow.map { courses ->
            courses.filter { it.dayOfWeek == dayOfWeek }
        }
    }

}